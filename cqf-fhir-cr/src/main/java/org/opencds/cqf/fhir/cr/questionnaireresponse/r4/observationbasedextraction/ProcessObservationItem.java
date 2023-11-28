package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction;

import org.hl7.fhir.r4.model.Observation;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParameters;

import static java.util.Objects.requireNonNull;

public class ProcessObservationItem {
    // Observation-based extraction -
    // http://build.fhir.org/ig/HL7/sdc/extraction.html#observation-based-extraction
    ObservationFactory observationFactory;
    public void process(ProcessParameters processParameters) {
        requireNonNull(processParameters.getQuestionnaireCodeMap());
        if (processParameters.getQuestionnaireCodeMap().isEmpty()) {
            throw new IllegalArgumentException(
                "Unable to retrieve Questionnaire code map for Observation based extraction");
        }
        if (processParameters.getItemResolver().hasAnswer()) {
            processParameters.getItemResolver().getAnswer().forEach(answer -> {
                if (answer.hasItem()) {
                    answer.getItem().forEach(answerItem -> {
                        processParameters.setItem(answerItem);
                        process(processParameters);
                    });
                } else {
                    if (processParameters.getQuestionnaireCodeMap().get(processParameters.getItemResolver().getLinkId()) != null && !processParameters.getQuestionnaireCodeMap().get(processParameters.getItemResolver().getLinkId()).isEmpty()) {
                        final Observation observation = observationFactory.makeObservation(
                            answer,
                            processParameters.getItemResolver().getLinkId(),
                            processParameters.getQuestionnaireResponseResolver(),
                            processParameters.getSubjectResolver(),
                            processParameters.getQuestionnaireCodeMap()
                        );
                        processParameters.addToResources(observation);
                    }
                }
            });
        }
    }
}
