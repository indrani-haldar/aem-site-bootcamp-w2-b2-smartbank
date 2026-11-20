package com.smartbank.core.models;

import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.api.resource.Resource;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductDetailsModel {
    @Self
    private SlingHttpServletRequest request;
    @SlingObject
    private Resource resource;
    private BankingProduct product;

    @PostConstruct
    protected void init() {
        String id = request.getParameter("product");
        if (id != null) {
            product = BankingProducts.selected(resource.getResourceResolver(), id, BankingProducts.DETAILS);
        } else {
            String path = resource.getValueMap().get("fragmentPath", "");
            if (!path.isEmpty()) {
                product = BankingProducts.read(resource.getResourceResolver(), path, BankingProducts.DETAILS);
            }
        }
    }

    public BankingProduct getProduct() { return product; }
    public String getId() { return "sb-details-" + Integer.toUnsignedString(resource.getPath().hashCode(), 36); }
    public String getBackLink() { return "/content/smartbank/us/en/products.html"; }
}
