package com.smartbank.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FaqAccordionModel extends SmartBankModel {
    public String getHeading() { return text("heading", "Frequently asked questions"); }
    public String getDescription() { return text("description", ""); }
    public List<Item> getItems() {
        List<Item> result = new ArrayList<>();
        for (Resource item : children("items")) {
            String question = item.getValueMap().get("question", "");
            String answer = item.getValueMap().get("answer", "");
            if (!question.isEmpty() && !answer.isEmpty()) {
                result.add(new Item(question, answer));
            }
        }
        return Collections.unmodifiableList(result);
    }
    public static final class Item {
        private final String question;
        private final String answer;
        Item(String question, String answer) { this.question = question; this.answer = answer; }
        public String getQuestion() { return question; }
        public String getAnswer() { return answer; }
    }
}
