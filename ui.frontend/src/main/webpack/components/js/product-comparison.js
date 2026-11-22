(function () {
    "use strict";

    const detailFields = [
        { key: "interestRate", label: "Interest Rate" },
        { key: "annualFee", label: "Annual Fee" },
        { key: "keyBenefit", label: "Key Benefit" }
    ];

    const getOrCreateDetails = (card) => {
        let details = card.querySelector(".cmp-product-cards__details");
        if (details) {
            return details;
        }

        const content = card.querySelector(".cmp-product-cards__content");
        if (!content) {
            return null;
        }

        details = document.createElement("dl");
        details.className = "cmp-product-cards__details";

        const action = content.querySelector(".cmp-product-cards__action");
        content.insertBefore(details, action);
        return details;
    };

    const getOrCreateDetail = (details, field) => {
        const rows = Array.from(details.children);
        let row = rows.find((element) =>
            element.classList.contains("cmp-product-cards__detail") &&
            element.dataset.productDetail === field.key
        );

        if (!row) {
            row = document.createElement("div");
            row.className = "cmp-product-cards__detail";
            row.dataset.productDetail = field.key;
        }

        let term = row.querySelector("dt");
        if (!term) {
            term = document.createElement("dt");
            row.insertBefore(term, row.firstChild);
        }
        term.textContent = field.label;

        let value = row.querySelector("dd");
        if (!value) {
            value = document.createElement("dd");
            row.appendChild(value);
        }
        if (!value.textContent.trim()) {
            value.textContent = "-";
        }

        details.appendChild(row);
    };

    const initializeProductComparison = () => {
        const comparison = document.querySelector("#sb-product-comparison");
        if (!comparison) {
            return;
        }

        const cards = comparison.querySelectorAll(
            "#compare-cards .cmp-product-cards__card"
        );

        cards.forEach((card) => {
            const details = getOrCreateDetails(card);
            if (!details) {
                return;
            }

            detailFields.forEach((field) => getOrCreateDetail(details, field));
        });
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeProductComparison, {
            once: true
        });
    } else {
        initializeProductComparison();
    }
})();
