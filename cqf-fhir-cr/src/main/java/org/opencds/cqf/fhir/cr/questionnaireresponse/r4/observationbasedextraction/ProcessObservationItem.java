package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ModelResolverGetterService;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParameters;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class ProcessObservationItem {
    // Observation-based extraction -
    // http://build.fhir.org/ig/HL7/sdc/extraction.html#observation-based-extraction
    ObservationFactory observationFactory;
    ModelResolverGetterService modelResolverGetterService;

    public void process(ProcessParameters processParameters) {
        validate(processParameters);
        final List<IBaseBackboneElement> answers = modelResolverGetterService.getDynamicValues(processParameters.getQuestionnaireResponseItem(), "answer");
        if (!answers.isEmpty()) {
            answers.forEach(answer -> {
                try {
                    processAnswer(processParameters, answer);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                         IllegalAccessException e) {
                    // TODO: WHAT ERROR TO THROW HERE?
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void processAnswer(ProcessParameters processParameters, IBaseBackboneElement answer)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final List<IBaseResource> answerItems = modelResolverGetterService.getDynamicValues(answer, "item");
        if (!answerItems.isEmpty()) {
            answerItems.forEach(answerItem -> {
                processParameters.setQuestionnaireResponseItem(answerItem);
                process(processParameters);
            });
        } else {
            if (codingsExist(processParameters)) {
                final IBaseResource observation = observationFactory.makeObservation(answer, processParameters);
                processParameters.addToResources(observation);
            }
        }
    }

    private boolean codingsExist(ProcessParameters processParameters) {
        final Map<String, List<IBaseCoding>> codeMap = processParameters.getQuestionnaireCodeMap();
        final String linkId = modelResolverGetterService.getDynamicStringValue(processParameters.getQuestionnaireResponseItem(), "linkId");
        final List<IBaseCoding> applicableCodings = codeMap.get(linkId);
        return applicableCodings != null && !applicableCodings.isEmpty();
    }

    private void validate(ProcessParameters processParameters) {
        requireNonNull(processParameters.getQuestionnaireCodeMap());
        if (processParameters.getQuestionnaireCodeMap().isEmpty()) {
            throw new IllegalArgumentException(
                "Unable to retrieve Questionnaire code map for Observation based extraction");
        }
    }
}
