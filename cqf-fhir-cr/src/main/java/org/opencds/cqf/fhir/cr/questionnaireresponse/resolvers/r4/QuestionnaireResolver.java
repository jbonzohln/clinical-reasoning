package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.Resource;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionnaireResolver {
    private final Questionnaire questionnaire;

    public QuestionnaireResolver(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    public static QuestionnaireResolver of(Questionnaire questionnaire) {
        return new QuestionnaireResolver(questionnaire);
    }

    public static QuestionnaireResolver of(ResourceResolver resourceResolver) {
        final Resource resource = resourceResolver.getResource();
        return new QuestionnaireResolver((Questionnaire) resource);
    }

    public static <T extends IBaseResource> Class<T> _getClass() {
        return (Class<T>) Questionnaire.class;
    }

    public static QuestionnaireResolver of(Resource questionnaire) {
        return new QuestionnaireResolver((Questionnaire) questionnaire);
    }

    public List<QuestionnaireItemComponentResolver> getItem() {
        return this.questionnaire.getItem().stream()
            .map(QuestionnaireItemComponentResolver::of)
            .collect(Collectors.toList());
    }
}
