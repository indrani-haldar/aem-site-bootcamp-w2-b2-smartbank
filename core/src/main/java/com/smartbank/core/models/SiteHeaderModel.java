package com.smartbank.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SiteHeaderModel extends SmartBankModel {
    public String getBrandName() { return text("brandName", "SmartBank"); }
    public String getHomeLink() { return link(text("homeLink", "/content/smartbank/us/en/home")); }
    public String getLogo() { return image(text("logo", "")); }
    public String getCtaLabel() { return text("ctaLabel", "Let's talk"); }
    public String getCtaLink() { return link(text("ctaLink", "/content/smartbank/us/en/contact")); }
    public List<NavItem> getLinks() {
        List<NavItem> result = new ArrayList<>();
        for (Resource item : children("links")) {
            String label = item.getValueMap().get("label", "");
            String href = link(item.getValueMap().get("link", ""));
            if (!label.isEmpty() && !href.isEmpty()) {
                result.add(new NavItem(label, href));
            }
        }
        return Collections.unmodifiableList(result);
    }
    public static final class NavItem {
        private final String label;
        private final String link;
        NavItem(String label, String link) { this.label = label; this.link = link; }
        public String getLabel() { return label; }
        public String getLink() { return link; }
    }
}
