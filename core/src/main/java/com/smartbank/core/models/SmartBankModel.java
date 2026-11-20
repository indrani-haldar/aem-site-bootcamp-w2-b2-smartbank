package com.smartbank.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Shared authoring helpers; content is read with the current visitor's permissions. */
public abstract class SmartBankModel {
    private static final Logger LOG = LoggerFactory.getLogger(SmartBankModel.class);
    private static final Pattern UNSAFE_URL = Pattern.compile("[\\p{Cntrl}\\\\]");

    @SlingObject
    protected Resource resource;

    protected String text(String name, String fallback) {
        return resource.getValueMap().get(name, fallback);
    }

    protected List<Resource> children(String name) {
        Resource parent = resource.getChild(name);
        if (parent == null) {
            return Collections.emptyList();
        }
        List<Resource> result = new ArrayList<>();
        parent.getChildren().forEach(result::add);
        return Collections.unmodifiableList(result);
    }

    protected String choice(String name, String fallback, String... allowed) {
        String value = text(name, fallback);
        for (String option : allowed) {
            if (option.equals(value)) {
                return value;
            }
        }
        LOG.warn("Unsupported {} on {}; using {}", name, resource.getPath(), fallback);
        return fallback;
    }

    public String getId() {
        return "sb-" + Integer.toUnsignedString(resource.getPath().hashCode(), 36);
    }

    public static String link(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        String url = value.trim();
        String lower = url.toLowerCase(Locale.ROOT);
        if (UNSAFE_URL.matcher(url).find() || url.startsWith("//")
                || !(url.startsWith("/") || url.startsWith("#")
                || lower.startsWith("https://") || lower.startsWith("http://")
                || lower.startsWith("mailto:") || lower.startsWith("tel:"))) {
            LOG.warn("Rejected an unsupported SmartBank link");
            return "";
        }
        if (url.startsWith("/content/") && !url.startsWith("/content/dam/")
                && !url.startsWith("/content/experience-fragments/")) {
            int query = url.indexOf('?');
            int hash = url.indexOf('#');
            int end = query < 0 ? url.length() : query;
            if (hash >= 0 && hash < end) {
                end = hash;
            }
            String path = url.substring(0, end);
            if (!path.endsWith("/") && !path.substring(path.lastIndexOf('/') + 1).contains(".")) {
                return path + ".html" + url.substring(end);
            }
        }
        return url;
    }

    public static String image(String value) {
        if (value == null || value.isEmpty()) {
            return "";
        }
        if (value.startsWith("/content/dam/") && !value.contains("..")
                && !UNSAFE_URL.matcher(value).find()) {
            return value;
        }
        LOG.warn("Rejected a SmartBank image reference outside the supported DAM paths");
        return "";
    }
}
