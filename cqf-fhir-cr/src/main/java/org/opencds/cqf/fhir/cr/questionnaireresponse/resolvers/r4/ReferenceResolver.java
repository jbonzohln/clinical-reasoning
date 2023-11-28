package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.r4.model.Reference;

public class ReferenceResolver {
    private final Reference reference;

    public ReferenceResolver(Reference reference) {
        this.reference = reference;
    }

    public static ReferenceResolver of(Reference reference) {
        return new ReferenceResolver(reference);
    }

    public ReferenceResolver copy() {
        return ReferenceResolver.of(this.reference.copy());
    }
}
