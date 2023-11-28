package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Questionnaire;

public class CodingResolver {
    private final Coding coding;

    public CodingResolver(Coding coding) {
        this.coding = coding;
    }

    public static CodingResolver of(Coding coding) {
        return new CodingResolver(coding);
    }
}
