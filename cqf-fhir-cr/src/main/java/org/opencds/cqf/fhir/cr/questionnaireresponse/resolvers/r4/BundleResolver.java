package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;

public class BundleResolver {
    public static <T extends IBaseResource> Class<T> _getClass() {
        return (Class<T>) Bundle.class;
    }
}
