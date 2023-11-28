package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Questionnaire;

public class QuestionnaireResolver {

    public static <T extends IBaseResource> Class<T> _getClass() {
        return (Class<T>) Questionnaire.class;
    }
}
