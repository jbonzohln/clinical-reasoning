package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.DynamicValueProcessor;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.BundleResolver;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.QuestionnaireResolver;
import org.opencds.cqf.fhir.utility.search.Searches;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProcessParametersHelper {
    Repository repository;
    DynamicValueProcessor dynamicValueProcessor;
    QuestionnaireCodeMapHelper questionnaireCodeMapHelper;
    @Nonnull
    public ProcessParameters createParameters(
        IBaseBackboneElement questionnaireResponse,
        IBaseBackboneElement questionnaireResponseItem,
        List<IBaseResource> resources
    ) {
        final IBaseReference subject = (IBaseReference) dynamicValueProcessor.getDynamicValue(questionnaireResponse, "subject");
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
        final IBaseBackboneElement questionnaireCanonical = dynamicValueProcessor.getDynamicValue(questionnaireResponse, "questionnaire");
        if (questionnaireCanonical != null && !questionnaireCanonical.isEmpty()) {
            final IBaseBundle baseBundle = this.repository.search(
                BundleResolver._getClass(),
                QuestionnaireResolver._getClass(),
                Searches.byCanonical(questionnaireCanonical.toString())
            );
            final List<IBaseBackboneElement> bundleEntries = dynamicValueProcessor.getDynamicValues(baseBundle, "entry");
            if (!bundleEntries.isEmpty()) {
                final IBaseBackboneElement resource = dynamicValueProcessor.getDynamicValue(bundleEntries.get(0), "resource");
                return resource != null ? Optional.of(resource) : Optional.empty();
            }
            return Optional.empty();
        }
        return Optional.empty();
    }
}
