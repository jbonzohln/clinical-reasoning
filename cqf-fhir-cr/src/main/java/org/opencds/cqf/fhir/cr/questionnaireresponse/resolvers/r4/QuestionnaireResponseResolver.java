package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.r4.model.QuestionnaireResponse;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionnaireResponseResolver {
    private final QuestionnaireResponse questionnaireResponse;

    public QuestionnaireResponseResolver(QuestionnaireResponse questionnaireResponse) {
        this.questionnaireResponse = questionnaireResponse;
    }

    public static QuestionnaireResponseResolver of(QuestionnaireResponse questionnaireResponse) {
        return new QuestionnaireResponseResolver(questionnaireResponse);
    }

    public ReferenceResolver getSubject() {
        return ReferenceResolver.of(this.questionnaireResponse.getSubject());
    }

    public List<QuestionnaireResponseItemComponentResolver> getItem() {
        return this.questionnaireResponse.getItem().stream()
            .map(QuestionnaireResponseItemComponentResolver::of)
            .collect(Collectors.toList());
    }

    public String getQuestionnaire() {
        return this.questionnaireResponse.getQuestionnaire();
    }

    public boolean hasExtension(String url) {
        return this.questionnaireResponse.hasExtension(url);
    }
}
