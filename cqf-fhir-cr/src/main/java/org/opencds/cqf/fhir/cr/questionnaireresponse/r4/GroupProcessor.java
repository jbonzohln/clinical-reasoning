package org.opencds.cqf.fhir.cr.questionnaireresponse.r4;

import org.apache.commons.lang3.SerializationUtils;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ModelResolverService;
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
    ModelResolverService modelResolverService;
    ProcessObservationItem processObservationItem;
    ProcessDefinitionItem processDefinitionItem;
    ProcessorHelper processorHelper;

    void processGroupItem(ProcessParameters processParameters) {
        final ProcessParameters updatedProcessParameters = updateProcessParameters(processParameters);
        final boolean hasDefinition = modelResolverService.hasValue(processParameters.getQuestionnaireResponseItem(), "answer");
        if (hasDefinition) {
            processDefinitionItem.process(updatedProcessParameters);
        } else {
            IBaseBackboneElement
            processParameters.getQuestionnaireResponse().getItem();

            final List<IBaseBackboneElement> childItems = modelResolverService.getDynamicValues(
                updatedProcessParameters.getQuestionnaireResponseItem(),
                "item"
            );
            childItems.forEach(childItem -> processChildItem(updatedProcessParameters, childItem));
        }
    }

    void processChildItem(ProcessParameters processParameters, IBaseBackboneElement childItem) {
        final boolean hasSubjectExtension = processorHelper.hasExtension(childItem, Constants.SDC_QUESTIONNAIRE_RESPONSE_IS_SUBJECT);
        if (!hasSubjectExtension) {
            if (modelResolverService.hasChildItems(childItem, "item")) {
                processGroupItem(processParameters);
            } else {
                processObservationItem.process(processParameters);
            }
        }
    }

    @Nonnull
    ProcessParameters updateProcessParameters(ProcessParameters processParameters) {
        final IBaseReference groupSubject = getGroupSubject(
            processParameters.getQuestionnaireResponseItem(),
            processParameters.getSubject()
        );
        processParameters.setSubject(groupSubject);
        return processParameters;
    }

    @Nonnull
    private IBaseReference getGroupSubject(IBaseBackboneElement item, IBaseReference subject) {
        final List<IBaseBackboneElement> subjectItems = getSubjectItems(item);
        if (!subjectItems.isEmpty()) {
            final IBaseBackboneElement subjectItem = subjectItems.get(0);
            final List<IBaseBackboneElement> answers = modelResolverService.getDynamicValues(subjectItem, "answer");
            return (IBaseReference) modelResolverService.getDynamicValue(answers.get(0), "value");
        }
        return clone(subject);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T clone(T object) {
        return SerializationUtils.deserialize(SerializationUtils.serialize(object));
    }

    @Nonnull
    private List<IBaseBackboneElement> getSubjectItems(IBaseBackboneElement item) {
        final List<IBaseBackboneElement> subjectItems = modelResolverService.getDynamicValues(item, "item");
        return subjectItems.stream()
            .filter(subItem -> processorHelper.hasExtension(subItem, Constants.SDC_QUESTIONNAIRE_ITEM_POPULATION_CONTEXT))
            .collect(Collectors.toList());
    }
}
