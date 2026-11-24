package com.smartbank.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ContactSectionModel extends SmartBankModel {
    public String getHeading() { return text("heading", "Good conversations start here."); }
    public String getDescription() { return text("description", "A question, a plan or a new possibility. We are here to help you take the next step."); }
    public String getEmail() { return text("email", ""); }
    public String getEmailLink() {
        String email = getEmail();
        return email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}") ? link("mailto:" + email) : "";
    }
    public String getPhone() { return text("phone", ""); }
    public String getPhoneLink() {
        String phone = getPhone().replaceAll("[\\s().-]", "");
        return phone.matches("\\+?[0-9]{6,15}") ? link("tel:" + phone) : "";
    }
    public String getHours() { return text("hours", ""); }
    public String getNotice() { return text("notice", "This is a demonstration website. Do not enter real personal, account or financial information."); }
}
