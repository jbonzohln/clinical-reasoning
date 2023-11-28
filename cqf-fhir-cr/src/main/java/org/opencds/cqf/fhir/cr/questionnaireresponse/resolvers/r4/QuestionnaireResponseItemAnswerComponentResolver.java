package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent;

public class QuestionnaireResponseItemAnswerComponentResolver {
    private final QuestionnaireResponseItemAnswerComponent answer;

    public QuestionnaireResponseItemAnswerComponentResolver(QuestionnaireResponseItemAnswerComponent answer) {
        this.answer = answer;
    }

    public static QuestionnaireResponseItemAnswerComponentResolver of(QuestionnaireResponseItemAnswerComponent answer) {
        return new QuestionnaireResponseItemAnswerComponentResolver(answer);
    }

    public ReferenceResolver getValueReference() {
        return ReferenceResolver.of(this.answer.getValueReference());
    }
}
