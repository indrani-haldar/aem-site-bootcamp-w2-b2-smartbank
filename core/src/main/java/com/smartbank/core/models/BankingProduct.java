package com.smartbank.core.models;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;

/** Read-only view shared by cards, listing, details and comparison. */
public final class BankingProduct {
    private final String id;
    private final ContentFragment fragment;
    private final String detailsPage;

    BankingProduct(String id, ContentFragment fragment, String detailsPage) {
        this.id = id;
        this.fragment = fragment;
        this.detailsPage = detailsPage;
    }

    private String value(String name) {
        ContentElement element = fragment.getElement(name);
        return element == null || element.getContent() == null ? "" : element.getContent().trim();
    }

    private List<String> values(String name) {
        ContentElement element = fragment.getElement(name);
        if (element == null) {
            return Collections.emptyList();
        }
        Object raw = element.getValue() == null ? null : element.getValue().getValue();
        List<String> result = new ArrayList<>();
        if (raw instanceof String[]) {
            Arrays.stream((String[]) raw).map(String::trim).filter(s -> !s.isEmpty()).forEach(result::add);
        } else {
            Arrays.stream(value(name).split("\\r?\\n")).map(String::trim)
                    .filter(s -> !s.isEmpty()).forEach(result::add);
        }
        return Collections.unmodifiableList(result);
    }

    public String getId() { return id; }
    public String getName() { return value("name"); }
    public String getType() { return value("type"); }
    public String getTypeLabel() {
        switch (getType()) {
            case "account": return "Account";
            case "card": return "Credit card";
            case "loan": return "Loan";
            default: return getType();
        }
    }
    public String getDescription() { return value("description"); }
    public String getInterestRate() { return value("interestRate"); }
    public String getAnnualFee() { return value("annualFee"); }
    public String getCurrency() { return value("currency").isEmpty() ? "INR" : value("currency"); }
    public String getRateLabel() {
        return "account".equals(getType()) ? "Interest earned p.a." : "Interest rate p.a.";
    }
    public List<String> getBenefits() { return values("benefits"); }
    public String getBenefitsText() { return String.join(" \u00b7 ", getBenefits()); }
    public String getEligibility() { return value("eligibility"); }
    public String getImage() { return SmartBankModel.image(value("image")); }
    public String getImageAlt() { return value("imageAlt"); }
    public String getCtaLabel() { return value("ctaLabel").isEmpty() ? "Enquire now" : value("ctaLabel"); }
    public String getCtaLink() {
        String authored = SmartBankModel.link(value("ctaLink"));
        return authored.isEmpty() ? "/content/smartbank/us/en/contact.html" : authored;
    }
    public String getDetailsLink() {
        String page = SmartBankModel.link(detailsPage);
        if (page.isEmpty()) {
            return "";
        }
        int hash = page.indexOf('#');
        String suffix = hash < 0 ? "" : page.substring(hash);
        String base = hash < 0 ? page : page.substring(0, hash);
        return base + (base.contains("?") ? "&" : "?") + "product="
                + URLEncoder.encode(id, StandardCharsets.UTF_8) + suffix;
    }
}
