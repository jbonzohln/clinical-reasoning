package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;

public class BundleEntryComponentResolver {
    private final BundleEntryComponent bundleEntryComponent;

    public BundleEntryComponentResolver(BundleEntryComponent bundleEntryComponent) {
        this.bundleEntryComponent = bundleEntryComponent;
    }

    public static BundleEntryComponentResolver of(BundleEntryComponent bundleEntryComponent) {
        return new BundleEntryComponentResolver(bundleEntryComponent);
    }

    public ResourceResolver getResource() {
        return ResourceResolver.of(this.bundleEntryComponent.getResource());
    }
}
