package com.smartbank.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.smartbank.core.testcontext.AppAemContext;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class FaqAccordionModelTest {

    private final AemContext context = AppAemContext.newAemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(FaqAccordionModel.class);
    }

    @Test
    void readsAuthoredItemsAndSkipsIncompleteEntries() {
        Resource accordion = context.create().resource("/content/faq",
            "heading", "Frequently asked questions",
            "description", "Answers to common questions.");
        context.create().resource("/content/faq/items/item0",
            "question", "Question one",
            "answer", "Answer one");
        context.create().resource("/content/faq/items/item1",
            "question", "Question without an answer");

        FaqAccordionModel model = accordion.adaptTo(FaqAccordionModel.class);

        assertNotNull(model);
        assertEquals("Frequently asked questions", model.getHeading());
        assertEquals("Answers to common questions.", model.getDescription());
        List<FaqAccordionModel.Item> items = model.getItems();
        assertEquals(1, items.size());
        assertEquals("Question one", items.get(0).getQuestion());
        assertEquals("Answer one", items.get(0).getAnswer());
        assertTrue(items.stream().allMatch(item -> !item.getQuestion().isEmpty()
            && !item.getAnswer().isEmpty()));
    }
}
