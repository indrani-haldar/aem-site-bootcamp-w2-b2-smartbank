package com.smartbank.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class OfferBannerModel extends SmartBankModel {
    public String getHeading() { return text("heading", "Something for every stage of life."); }
    public List<Variant> getVariants() {
        List<Variant> result = new ArrayList<>();
        for (Resource item : children("variants")) {
            Variant variant = new Variant(item);
            if (!variant.getTitle().isEmpty()) {
                result.add(variant);
            }
        }
        return Collections.unmodifiableList(result);
    }

    public static final class Variant {
        private final Resource item;
        Variant(Resource item) { this.item = item; }
        public String getAudience() { return item.getValueMap().get("audience", ""); }
        public String getTitle() { return item.getValueMap().get("title", ""); }
        public String getDescription() { return item.getValueMap().get("description", ""); }
        public String getImage() { return image(item.getValueMap().get("fileReference", "")); }
        public String getAltText() { return item.getValueMap().get("altText", ""); }
        public String getCtaLabel() { return item.getValueMap().get("ctaLabel", "Discover more"); }
        public String getCtaLink() { return link(item.getValueMap().get("ctaLink", "")); }
    }
}
