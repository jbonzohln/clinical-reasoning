package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;

public class BundleResolver {
    Bundle bundle;

    public BundleResolver(Bundle bundle) {
        this.bundle = bundle;
    }

    public static BundleResolver of(Bundle bundle) {
        return new BundleResolver(bundle);
    }
    public static BundleResolver of(IBaseBundle bundle) {
        return new BundleResolver((Bundle) bundle);
    }
    public static <T extends IBaseResource> Class<T> _getClass() {
        return (Class<T>) Bundle.class;
    }

    public boolean hasEntry() {
        return this.bundle.hasEntry();
    }

    public BundleEntryComponentResolver getEntryFirstRep() {
        return BundleEntryComponentResolver.of(this.bundle.getEntryFirstRep());
    }
}
