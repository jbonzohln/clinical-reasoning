package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ModelResolverGetterService;
import org.opencds.cqf.fhir.utility.Canonicals;
import org.opencds.cqf.fhir.utility.search.Searches;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProcessParametersHelper {
    Repository repository;
    ModelResolverGetterService modelResolverGetterService;
    QuestionnaireCodeMapHelper questionnaireCodeMapHelper;
    @Nonnull
    public ProcessParameters createParameters(
        IBaseBackboneElement questionnaireResponse,
        IBaseBackboneElement questionnaireResponseItem,
        List<IBaseResource> resources
    ) {
        final IBaseReference subject = (IBaseReference) modelResolverGetterService.getDynamicValue(questionnaireResponse, "subject");
        final Optional<IBaseBackboneElement> questionnaire = getQuestionnaire(questionnaireResponse);
        final Map<String, List<IBaseCoding>> questionnaireCodeMap = questionnaire
            .map(q -> questionnaireCodeMapHelper.createCodeMap(q)).orElse(null);
        return new ProcessParameters(
            questionnaireResponseItem,
            questionnaireResponse,
            subject,
            resources,
            questionnaireCodeMap
        );
    }

    @Nonnull
    private Optional<IBaseBackboneElement> getQuestionnaire(IBaseBackboneElement questionnaireResponse) {
        final String questionnaireCanonical = modelResolverGetterService.getDynamicStringValue(questionnaireResponse, "questionnaire");
        if (questionnaireCanonical != null && !questionnaireCanonical.isEmpty()) {
            final IBaseBundle baseBundle = this.repository.search(
                getBundleClass(),
                getQuestionnaireClass(questionnaireCanonical),
                Searches.byCanonical(questionnaireCanonical)
            );
            final List<IBaseBackboneElement> bundleEntries = modelResolverGetterService.getDynamicValues(baseBundle, "entry");
            if (!bundleEntries.isEmpty()) {
                final IBaseBackboneElement resource = modelResolverGetterService.getDynamicValue(bundleEntries.get(0), "resource");
                return resource != null ? Optional.of(resource) : Optional.empty();
            }
            return Optional.empty();
        }
        return Optional.empty();
    }

    Class<? extends IBaseResource> getQuestionnaireClass(String questionnaireCanonical) {
        return repository
            .fhirContext()
            .getResourceDefinition(Canonicals.getResourceType(questionnaireCanonical))
            .getImplementingClass();
    }

    Class<? extends IBaseBundle> getBundleClass() {
        return repository
            .fhirContext()
            .getResourceDefinition("Bundle")
            .getImplementingClass(IBaseBundle.class);
    }
}
