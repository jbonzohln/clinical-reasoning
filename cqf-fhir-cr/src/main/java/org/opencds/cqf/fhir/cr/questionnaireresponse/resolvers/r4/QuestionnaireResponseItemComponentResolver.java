package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionnaireResponseItemComponentResolver {
    private final QuestionnaireResponseItemComponent questionnaireResponseItemComponent;

    public QuestionnaireResponseItemComponentResolver(QuestionnaireResponseItemComponent questionnaireResponseItemComponent) {
        this.questionnaireResponseItemComponent = questionnaireResponseItemComponent;
    }

    public static QuestionnaireResponseItemComponentResolver of(QuestionnaireResponseItemComponent questionnaireResponseItemComponent) {
        return new QuestionnaireResponseItemComponentResolver(questionnaireResponseItemComponent);
    }

    public boolean hasItem() {
        return this.questionnaireResponseItemComponent.hasItem();
    }

    public boolean hasDefinition() {
        return this.questionnaireResponseItemComponent.hasDefinition();
    }

    public boolean hasExtension(String url) {
        return this.questionnaireResponseItemComponent.hasExtension(url);
    }

    public List<QuestionnaireResponseItemComponentResolver> getItem() {
        return this.questionnaireResponseItemComponent.getItem().stream()
            .map(QuestionnaireResponseItemComponentResolver::of)
            .collect(Collectors.toList());
    }

    public QuestionnaireResponseItemComponent getResource() {
        return this.questionnaireResponseItemComponent;
    }

    public List<QuestionnaireResponseItemAnswerComponentResolver> getAnswer() {
        return this.questionnaireResponseItemComponent.getAnswer().stream()
            .map(QuestionnaireResponseItemAnswerComponentResolver::of)
            .collect(Collectors.toList());
    }
}
