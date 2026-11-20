package com.smartbank.core.models;

import javax.annotation.PostConstruct;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FinancialProductCardModel extends SmartBankModel {
    private BankingProduct product;

    @PostConstruct
    protected void init() {
        String path = text("fragmentPath", "");
        if (!path.isEmpty()) {
            product = BankingProducts.read(resource.getResourceResolver(), path, text("detailsPage", BankingProducts.DETAILS));
        }
    }

    public BankingProduct getProduct() { return product; }
    public boolean isCompareEnabled() { return resource.getValueMap().get("compareEnabled", false); }
}
