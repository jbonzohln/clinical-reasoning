package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Reference;

public class ReferenceResolver {
    public static Reference makeReference(IBaseBackboneElement resource) {
        // ROSIE TODO
        final Reference reference = new Reference();
        reference.setReference((IBaseResource) resource);
        return reference;
    }
}
