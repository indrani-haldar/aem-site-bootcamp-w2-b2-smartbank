const initializeHeaderMenu = () => {
    const header = document.querySelector("#sb-header");
    const navigation = header && header.querySelector(".navigation");

    if (!(header instanceof HTMLElement) || !(navigation instanceof HTMLElement)) {
        return;
    }

    const toggle = document.createElement("button");
    toggle.className = "header-menu-toggle";
    toggle.type = "button";
    toggle.setAttribute("aria-label", "Open navigation");
    toggle.setAttribute("aria-expanded", "false");

    if (!navigation.id) {
        navigation.id = "sb-header-navigation";
    }
    toggle.setAttribute("aria-controls", navigation.id);

    const setMenuOpen = (isOpen) => {
        header.classList.toggle("is-menu-open", isOpen);
        toggle.setAttribute("aria-expanded", String(isOpen));
        toggle.setAttribute("aria-label", isOpen ? "Close navigation" : "Open navigation");
    };

    toggle.addEventListener("click", () => {
        setMenuOpen(toggle.getAttribute("aria-expanded") !== "true");
    });

    navigation.addEventListener("click", (event) => {
        if (event.target instanceof Element && event.target.closest("a")) {
            setMenuOpen(false);
        }
    });

    document.addEventListener("keydown", (event) => {
        if (event.key === "Escape" && header.classList.contains("is-menu-open")) {
            setMenuOpen(false);
            toggle.focus();
        }
    });

    document.addEventListener("click", (event) => {
        if (event.target instanceof Node && !header.contains(event.target)) {
            setMenuOpen(false);
        }
    });

    window.matchMedia("(min-width: 768px)").addEventListener("change", (event) => {
        if (event.matches) {
            setMenuOpen(false);
        }
    });

    navigation.parentElement.insertBefore(toggle, navigation);
};

if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", initializeHeaderMenu, { once: true });
} else {
    initializeHeaderMenu();
}
