package com.smartbank.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductComparisonModel extends SmartBankModel {
    public String getHeading() { return text("heading", "A little clarity. A better choice."); }
    public String getDescription() { return text("description", "Choose up to three products from the listing above to compare them side by side."); }
    public String getEmptyMessage() { return text("emptyMessage", "Your shortlist starts here. Select Compare on a product to get started."); }
}
