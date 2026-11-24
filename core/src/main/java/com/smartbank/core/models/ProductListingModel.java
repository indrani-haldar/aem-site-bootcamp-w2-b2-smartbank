package com.smartbank.core.models;

import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListingModel extends SmartBankModel {
    private List<BankingProduct> products;

    @PostConstruct
    protected void init() {
        products = BankingProducts.list(resource.getResourceResolver(),
                choice("category", "all", "all", "account", "card", "loan"),
                text("detailsPage", BankingProducts.DETAILS));
    }

    public String getHeading() { return text("heading", "Find what moves you forward."); }
    public String getDescription() { return text("description", "Explore accounts, cards and loans designed for your next chapter."); }
    public String getSearchPlaceholder() { return text("searchPlaceholder", "Search products"); }
    public String getNoResultsMessage() { return text("noResultsMessage", "No products match your search. Try another term or category."); }
    public boolean isFiltersEnabled() { return resource.getValueMap().get("filtersEnabled", true); }
    public boolean isCompareEnabled() { return resource.getValueMap().get("compareEnabled", true); }
    public List<BankingProduct> getProducts() { return products; }
}
