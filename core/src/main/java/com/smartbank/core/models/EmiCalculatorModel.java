package com.smartbank.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmiCalculatorModel extends SmartBankModel {
    public String getHeading() { return text("heading", "Plan today. Borrow with confidence."); }
    public String getDescription() { return text("description", "Get a clear picture of your monthly payments before taking the next step."); }
    public String getDisclaimer() { return text("disclaimer", "Illustrative estimate only. Actual rates, fees and repayment schedules may vary."); }
    public String getCurrency() { return choice("currency", "INR", "INR", "USD", "GBP", "EUR"); }
    public String getCtaLink() { return link(text("ctaLink", "/content/smartbank/us/en/contact")); }
    public String getCtaLabel() { return text("ctaLabel", "Discuss your loan"); }
}
