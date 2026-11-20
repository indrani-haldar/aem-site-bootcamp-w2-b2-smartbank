package com.smartbank.core.models;

import java.time.Year;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SiteFooterModel extends SmartBankModel {
    public String getBrandName() { return text("brandName", "SmartBank"); }
    public String getDescription() { return text("description", "Good banking. Greater possibilities."); }
    public String getDisclaimer() { return text("disclaimer", "Demonstration website. Products, rates and offers are illustrative only. Do not submit personal or financial information."); }
    public String getCopyright() { return "\u00a9 " + Year.now().getValue() + " " + getBrandName() + ". All rights reserved."; }
    public List<SiteHeaderModel.NavItem> getLinks() {
        List<SiteHeaderModel.NavItem> result = new ArrayList<>();
        for (Resource item : children("links")) {
            String label = item.getValueMap().get("label", "");
            String href = link(item.getValueMap().get("link", ""));
            if (!label.isEmpty() && !href.isEmpty()) {
                result.add(new SiteHeaderModel.NavItem(label, href));
            }
        }
        return Collections.unmodifiableList(result);
    }
}
