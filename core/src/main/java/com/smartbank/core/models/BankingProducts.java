package com.smartbank.core.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.dam.cfm.ContentFragment;

final class BankingProducts {
    static final String ROOT = "/content/dam/smartbank/banking-products";
    static final String DETAILS = "/content/smartbank/us/en/product-details";
    private static final String MODEL = "/conf/smartbank/settings/dam/cfm/models/banking-product";
    private static final Pattern ID = Pattern.compile("[a-z0-9][a-z0-9-]{0,99}");
    private static final Logger LOG = LoggerFactory.getLogger(BankingProducts.class);

    private BankingProducts() { }

    static BankingProduct read(ResourceResolver resolver, String path, String detailsPage) {
        if (path == null || !path.startsWith(ROOT + "/")
                || !ID.matcher(path.substring(ROOT.length() + 1)).matches()) {
            LOG.warn("Banking Product reference must be a named fragment directly below {}", ROOT);
            return null;
        }
        Resource asset = resolver.getResource(path);
        Resource data = asset == null ? null : asset.getChild("jcr:content/data");
        if (data == null || !MODEL.equals(data.getValueMap().get("cq:model", String.class))) {
            LOG.warn("Missing or incompatible Banking Product fragment: {}", path);
            return null;
        }
        ContentFragment fragment = asset.adaptTo(ContentFragment.class);
        if (fragment == null) {
            LOG.warn("Could not adapt Banking Product fragment: {}", path);
            return null;
        }
        BankingProduct product = new BankingProduct(asset.getName(), fragment, detailsPage);
        if (product.getName().isEmpty() || !ID.matcher(product.getId()).matches()
                || !("account".equals(product.getType()) || "card".equals(product.getType())
                || "loan".equals(product.getType()))) {
            LOG.warn("Banking Product requires a name, valid asset name and account/card/loan type: {}", path);
            return null;
        }
        return product;
    }

    static List<BankingProduct> list(ResourceResolver resolver, String category, String detailsPage) {
        Resource folder = resolver.getResource(ROOT);
        if (folder == null) {
            LOG.warn("Banking Product folder is missing or inaccessible: {}", ROOT);
            return Collections.emptyList();
        }
        List<BankingProduct> products = new ArrayList<>();
        for (Resource child : folder.getChildren()) {
            if (!child.isResourceType("dam:Asset") && !"dam:Asset".equals(
                    child.getValueMap().get("jcr:primaryType", String.class))) {
                continue;
            }
            BankingProduct product = read(resolver, child.getPath(), detailsPage);
            if (product != null && ("all".equals(category) || category.equals(product.getType()))) {
                products.add(product);
            }
        }
        products.sort(Comparator.comparing(BankingProduct::getName, String.CASE_INSENSITIVE_ORDER));
        return Collections.unmodifiableList(products);
    }

    static BankingProduct selected(ResourceResolver resolver, String id, String detailsPage) {
        return id != null && ID.matcher(id).matches() ? read(resolver, ROOT + "/" + id, detailsPage) : null;
    }
}
