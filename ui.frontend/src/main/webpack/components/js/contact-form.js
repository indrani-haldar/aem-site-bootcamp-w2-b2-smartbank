(function () {
    var storageKey = "smartbank.contact-submission";
    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;

    function showToast(toast, message, status) {
        toast.textContent = message;
        toast.dataset.status = status;
        toast.hidden = false;

        if (toast.dismissTimer) {
            window.clearTimeout(toast.dismissTimer);
        }

        toast.dismissTimer = window.setTimeout(function () {
            toast.hidden = true;
        }, 6000);
    }

    function addErrorElement(root, input) {
        var error = document.createElement("span");
        var errorId = (root.id || "contact-form") + "-" + input.name + "-error";
        var wrapper = input.closest(".cmp-form-text") || input.parentElement;
        var describedBy = (input.getAttribute("aria-describedby") || "")
            .split(/\s+/)
            .filter(Boolean);

        error.className = "sb-contact__field-error";
        error.id = errorId;
        error.hidden = true;
        wrapper.appendChild(error);

        if (describedBy.indexOf(errorId) === -1) {
            describedBy.push(errorId);
            input.setAttribute("aria-describedby", describedBy.join(" "));
        }

        return error;
    }

    function clearFieldError(field) {
        field.input.removeAttribute("aria-invalid");
        field.input.classList.remove("is-invalid");
        field.error.hidden = true;
        field.error.textContent = "";
    }

    function validateField(field) {
        var value = field.input.value.trim();
        var message = field.validate(value);

        field.input.setAttribute("aria-invalid", message ? "true" : "false");
        field.input.classList.toggle("is-invalid", Boolean(message));
        field.error.textContent = message || "";
        field.error.hidden = !message;

        return !message;
    }

    function initializeContactForm(root) {
        var nameInput = root.querySelector('[name="fullName"]');
        var emailInput = root.querySelector('[name="emailAddress"]');
        var messageInput = root.querySelector('[name="message"]');
        var submitButton = root.querySelector("#contact-submit-action") ||
            root.querySelector('button[name="contact-submit"]');

        if (!nameInput || !emailInput || !messageInput || !submitButton) {
            console.error("Contact form requires fullName, emailAddress, message, and submit fields.");
            return;
        }

        var toast = document.createElement("div");
        toast.className = "sb-contact__toast";
        toast.setAttribute("role", "status");
        toast.setAttribute("aria-live", "polite");
        toast.setAttribute("aria-atomic", "true");
        toast.hidden = true;
        root.appendChild(toast);

        var fields = [
            {
                input: nameInput,
                validate: function (value) {
                    if (!value) {
                        return "Enter your full name.";
                    }

                    return value.length < 2 ? "Enter at least 2 characters." : "";
                }
            },
            {
                input: emailInput,
                validate: function (value) {
                    if (!value) {
                        return "Enter your email address.";
                    }

                    return emailPattern.test(value) ? "" : "Enter a valid email address.";
                }
            },
            {
                input: messageInput,
                validate: function (value) {
                    return value ? "" : "Enter a message.";
                }
            }
        ];

        fields.forEach(function (field) {
            field.error = addErrorElement(root, field.input);
        });

        var hasSubmitted = false;
        submitButton.type = "button";

        fields.forEach(function (field) {
            field.input.addEventListener("input", function () {
                if (hasSubmitted) {
                    validateField(field);
                }
            });
        });

        function handleSubmission(event) {
            event.preventDefault();
            event.stopImmediatePropagation();
            hasSubmitted = true;

            var isValid = fields.reduce(function (valid, field) {
                return validateField(field) && valid;
            }, true);

            if (!isValid) {
                var firstInvalidField = fields.find(function (field) {
                    return field.input.getAttribute("aria-invalid") === "true";
                });
                firstInvalidField.input.focus();
                return;
            }

            var record = {
                fullName: nameInput.value.trim(),
                emailAddress: emailInput.value.trim(),
                message: messageInput.value.trim(),
                submittedAt: new Date().toISOString()
            };

            try {
                window.sessionStorage.setItem(storageKey, JSON.stringify(record));
            } catch (error) {
                console.error("Unable to save the contact submission in sessionStorage.", error);
                showToast(
                    toast,
                    "Your message could not be saved in this tab. The form has not been cleared.",
                    "error"
                );
                return;
            }

            fields.forEach(function (field) {
                field.input.value = "";
            });
            fields.forEach(clearFieldError);
            hasSubmitted = false;
            showToast(
                toast,
                "Your message has been sent successfully. We'll get back to you soon"
            );
        }

        submitButton.addEventListener("click", handleSubmission, true);

        [nameInput, emailInput].forEach(function (input) {
            input.addEventListener("keydown", function (event) {
                if (event.key === "Enter") {
                    handleSubmission(event);
                }
            });
        });
    }

    function initializeContactForms() {
        var roots = document.querySelectorAll("#sb-contact #contact-sections #contact-form");

        roots.forEach(function (root) {
            initializeContactForm(root);
        });
    }

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeContactForms, { once: true });
    } else {
        initializeContactForms();
    }
})();
