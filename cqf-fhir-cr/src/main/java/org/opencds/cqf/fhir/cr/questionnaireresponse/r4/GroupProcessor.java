package org.opencds.cqf.fhir.cr.questionnaireresponse.r4;

import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.DynamicValueProcessor;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ProcessorHelper;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.defintionbasedextraction.ProcessDefinitionItem;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction.ProcessObservationItem;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParameters;
import org.opencds.cqf.fhir.utility.Constants;
import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public class GroupProcessor {
    DynamicValueProcessor dynamicValueProcessor;
    ProcessObservationItem processObservationItem;
    ProcessDefinitionItem processDefinitionItem;
    ProcessorHelper processorHelper;

    void processGroupItem(ProcessParameters processParameters) {
        final IBaseReference groupSubject = getGroupSubject(
            processParameters.getQuestionnaireResponseItem(),
            processParameters.getSubject()
        );
        processParameters.setSubject(groupSubject);
        final IBaseBackboneElement definition = dynamicValueProcessor.getDynamicValue(
            processParameters.getQuestionnaireResponseItem(),
            "answer"
        );
        if (definition != null) {
            processDefinitionItem.process(processParameters);
        } else {
            final List<IBaseBackboneElement> childItems = dynamicValueProcessor.getDynamicValues(
                processParameters.getQuestionnaireResponseItem(),
                "item"
            );
            childItems.forEach(childItem -> {
                final boolean hasSubjectExtension = processorHelper.hasExtension(childItem, Constants.SDC_QUESTIONNAIRE_RESPONSE_IS_SUBJECT);
                if (!hasSubjectExtension) {
                    if (dynamicValueProcessor.hasChildItems(childItem, "item")) {
                        processGroupItem(processParameters);
                    } else {
                        processObservationItem.process(processParameters);
                    }
                }
            });
        }
    }

    @Nonnull
    private IBaseReference getGroupSubject(IBaseBackboneElement item, IBaseReference subject) {
        final List<IBaseBackboneElement> subjectItems = getSubjectItems(item);
        if (!subjectItems.isEmpty()) {
            final IBaseBackboneElement subjectItem = subjectItems.get(0);
            final List<IBaseBackboneElement> answers = dynamicValueProcessor.getDynamicValues(subjectItem, "answer");
            return (IBaseReference) dynamicValueProcessor.getDynamicValue(answers.get(0), "value");
        }
        return clone(subject);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T object) {
        return SerializationUtils.deserialize(SerializationUtils.serialize(object));
    }

    @Nonnull
    private List<IBaseBackboneElement> getSubjectItems(IBaseBackboneElement item) {
        final List<IBaseBackboneElement> subjectItems = dynamicValueProcessor.getDynamicValues(item, "item");
        return subjectItems.stream()
            .filter(subItem -> processorHelper.hasExtension(subItem, Constants.SDC_QUESTIONNAIRE_ITEM_POPULATION_CONTEXT))
            .collect(Collectors.toList());
    }
}
