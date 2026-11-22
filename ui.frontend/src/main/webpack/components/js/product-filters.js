(function () {
    "use strict";

    const normalizeText = (value) => String(value || "").trim().toLowerCase();
    const categoryAliases = new Map([
        ["all", "all"],
        ["account", "accounts"],
        ["accounts", "accounts"],
        ["saving", "accounts"],
        ["savings", "accounts"],
        ["card", "cards"],
        ["cards", "cards"],
        ["credit", "cards"],
        ["debit", "cards"],
        ["loan", "loans"],
        ["loans", "loans"],
        ["mortgage", "loans"]
    ]);

    const getCategory = (value) => categoryAliases.get(normalizeText(value)) || null;

    const getProductCategory = (card) => {
        const explicitCategory = getCategory(card.dataset.category || "");
        if (explicitCategory && explicitCategory !== "all") {
            return explicitCategory;
        }

        const title = card.querySelector(".cmp-product-cards__title");
        const text = normalizeText(title ? title.textContent : "");

        if (/\b(account|accounts|saving|savings|deposit|deposits)\b/.test(text)) {
            return "accounts";
        }
        if (/\b(card|cards|credit|debit)\b/.test(text)) {
            return "cards";
        }
        if (/\b(loan|loans|mortgage)\b/.test(text)) {
            return "loans";
        }

        return null;
    };

    const initializeProductFilters = () => {
        const productsRoot = document.querySelector("#sb-products");
        if (!(productsRoot instanceof HTMLElement)) {
            return;
        }

        const searchContainer = productsRoot.querySelector("#search-container");
        const tabContainer = productsRoot.querySelector("#tab-container");
        const searchInput = searchContainer &&
            searchContainer.querySelector(".cmp-search__input");
        const cardContainer = productsRoot.querySelector(".cmp-product-cards");
        const cards = Array.from(
            cardContainer ? cardContainer.querySelectorAll(".cmp-product-cards__card") : []
        );

        if (!(searchInput instanceof HTMLInputElement) || !tabContainer || !cardContainer) {
            return;
        }

        let emptyMessage = cardContainer.querySelector(".cmp-product-cards__empty");
        if (!emptyMessage) {
            emptyMessage = document.createElement("p");
            emptyMessage.className = "cmp-product-cards__empty";
            emptyMessage.setAttribute("role", "status");
            emptyMessage.textContent = "No products found.";
            emptyMessage.hidden = true;
            cardContainer.appendChild(emptyMessage);
        }

        const filters = Array.from(tabContainer.querySelectorAll(".cmp-button"))
            .map((button) => ({
                button,
                category: getCategory(button.dataset.category || button.textContent || "")
            }))
            .filter((filter) => filter.category);

        let activeCategory = "all";

        const updateVisibleCards = () => {
            const query = normalizeText(searchInput.value);
            let visibleCardCount = 0;

            cards.forEach((card) => {
                const matchesCategory = activeCategory === "all" ||
                    getProductCategory(card) === activeCategory;
                const matchesQuery = normalizeText(card.textContent || "").includes(query);
                const isVisible = matchesCategory && matchesQuery;

                card.hidden = !isVisible;
                visibleCardCount += isVisible ? 1 : 0;
            });

            emptyMessage.hidden = visibleCardCount > 0;
        };

        const setActiveCategory = (category) => {
            activeCategory = category;

            filters.forEach(({ button, category: buttonCategory }) => {
                const isActive = buttonCategory === activeCategory;
                button.setAttribute("aria-pressed", String(isActive));

                const buttonWrapper = button.closest(".button");
                if (buttonWrapper) {
                    buttonWrapper.classList.toggle("outlined", !isActive);
                }
            });

            updateVisibleCards();
        };

        filters.forEach(({ button, category }) => {
            button.addEventListener("click", (event) => {
                event.preventDefault();
                setActiveCategory(category);
            });
        });

        searchInput.addEventListener("input", updateVisibleCards);
        searchInput.addEventListener("search", updateVisibleCards);

        const searchForm = searchInput.closest("form");
        if (searchForm) {
            searchForm.addEventListener("submit", (event) => {
                event.preventDefault();
                updateVisibleCards();
            });
        }

        const clearButton = searchContainer.querySelector(
            '[data-cmp-hook-search="clear"]'
        );
        if (clearButton) {
            clearButton.addEventListener("click", () => {
                window.requestAnimationFrame(updateVisibleCards);
            });
        }

        setActiveCategory("all");
    };

    if (document.readyState === "loading") {
        document.addEventListener("DOMContentLoaded", initializeProductFilters, { once: true });
    } else {
        initializeProductFilters();
    }
})();
