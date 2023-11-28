package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Questionnaire.QuestionnaireItemComponent;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionnaireItemComponentResolver {
    private final QuestionnaireItemComponent questionnaireItemComponent;

    public QuestionnaireItemComponentResolver(QuestionnaireItemComponent questionnaireItemComponent) {
        this.questionnaireItemComponent = questionnaireItemComponent;
    }

    public static QuestionnaireItemComponentResolver of(QuestionnaireItemComponent questionnaireItemComponent) {
        return new QuestionnaireItemComponentResolver(questionnaireItemComponent);
    }

    public boolean hasItem() {
        return this.questionnaireItemComponent.hasItem();
    }

    public List<QuestionnaireItemComponentResolver> getItem() {
        return this.questionnaireItemComponent.getItem().stream()
            .map(QuestionnaireItemComponentResolver::of)
            .collect(Collectors.toList());
    }

    public List<CodingResolver> getCode() {
        return this.questionnaireItemComponent.getCode().stream()
            .map(CodingResolver::of)
            .collect(Collectors.toList());
    }

    public String getLinkId() {
        return this.questionnaireItemComponent.getLinkId();
    }
}
