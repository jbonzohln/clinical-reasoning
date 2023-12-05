package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ModelResolverGetterService;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ProcessorHelper;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ResourceBuilder;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParameters;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ObservationFactory {
    ProcessorHelper processorHelper;
    ModelResolverGetterService modelResolverGetterService;
    ResourceBuilder resourceBuilder;

    final IBaseResource makeObservation(
        IBaseBackboneElement answer,
        ProcessParameters processParameters
    )
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final String id = getId(processParameters);
        final List<IBaseReference> basedOn = getBasedOn(processParameters);
        final List<IBaseReference> partOf = getPartOf(processParameters);
        final List<IBaseDatatype> category = getCategory();
        final IBaseDatatype code = getCode(processParameters);
        final IBaseReference encounter = getEncounter(processParameters);
        final IPrimitiveType<Date> effective = getAuthoredDate(processParameters);
        final IPrimitiveType<Date> issued = getIssued(processParameters);
        final List<IBaseReference> performer = getPerformer(processParameters);
        final IBaseDatatype value = getValue(answer);
        final List<IBaseReference> derived = getDerivedFrom(processParameters);
        final List<IBaseDatatype> extension = getLinkExtension(processParameters);
        return new ObservationBuilder()
            .id(id)
            .basedOn(basedOn)
            .partOf(partOf)
            .performer(performer)
            .status("final")
            .category(category)
            .code(code)
            .subject(processParameters.getSubject())
            .encounter(encounter)
            .effective(effective)
            .issued(issued)
            .performer(performer)
            .value(value)
            .derived(derived)
            .extension(extension)
            .build();
    }

    final String getId(ProcessParameters processParameters) {
        final IBaseBackboneElement linkId = modelResolverGetterService.getDynamicValue(processParameters.getQuestionnaireResponseItem(), "linkId");
        final String extractId = processorHelper.getExtractId(processParameters.getQuestionnaireResponse());
        return extractId + "." + linkId;
    }

    final List<IBaseReference> getBasedOn(ProcessParameters processParameters) {
        return modelResolverGetterService.getDynamicReferenceValues(processParameters.getQuestionnaireResponseItem(), "basedOn");
    }

    final List<IBaseReference> getPartOf(ProcessParameters processParameters) {
        return modelResolverGetterService.getDynamicReferenceValues(processParameters.getQuestionnaireResponseItem(), "partOf");
    }

    final IBaseDatatype getValue(IBaseBackboneElement answer)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseDatatype answerValue = modelResolverGetterService.getDynamicDataType(answer, "value");
        final String fhirType = answerValue.fhirType();
        switch (fhirType) {
            case "Coding":
                return resourceBuilder.makeCodeableConcept(answerValue);
            case "date":
                return resourceBuilder.makeDateTime(answerValue);
            default:
                return answerValue;
        }
    }

    final List<IBaseDatatype> getLinkExtension(ProcessParameters processParameters)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final String linkIdUrl = "http://hl7.org/fhir/uv/sdc/StructureDefinition/derivedFromLinkId";
        final String linkId = modelResolverGetterService.getDynamicStringValue(processParameters.getQuestionnaireResponseItem(), "linkId");
        final IPrimitiveType<String> linkIdAsStringType = resourceBuilder.makeBaseResource("StringType", linkId);
        final IBaseDatatype innerLinkIdExtension = resourceBuilder.makeExtension("text", linkIdAsStringType, null);
        final IBaseDatatype extension = resourceBuilder.makeExtension(linkIdUrl, null, innerLinkIdExtension);
        return Collections.singletonList(extension);
    }

    final IBaseDatatype getCode(ProcessParameters processParameters)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final String linkId = modelResolverGetterService.getDynamicStringValue(processParameters.getQuestionnaireResponseItem(), "linkId");
        final Map<String, List<IBaseCoding>> coding = processParameters.getQuestionnaireCodeMap();
        final List<IBaseDatatype> codingsAsBaseDataType = coding.get(linkId).stream()
            .map(IBaseDatatype.class::cast)
            .collect(Collectors.toList());
        return resourceBuilder.makeCodeableConcept(codingsAsBaseDataType);
    }

    final List<IBaseDatatype> getCategory()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final String codeSystem = "http://hl7.org/fhir/observation-category";
        final String codeValue = "survey";
        final IBaseDatatype codeableConcept = resourceBuilder.makeCodeableConcept(codeSystem, codeValue);
        return Collections.singletonList(codeableConcept);
    }

    final IBaseReference getEncounter(ProcessParameters processParameters) {
        return modelResolverGetterService.getDynamicReferenceValue(processParameters.getQuestionnaireResponse(), "encounter");
    }

    final IPrimitiveType<Date> getIssued(ProcessParameters processParameters)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IPrimitiveType<Date> authoredDate = getAuthoredDate(processParameters);
        return resourceBuilder.makeBaseResource("InstantType", authoredDate);
    }

    final IPrimitiveType<Date> getAuthoredDate(ProcessParameters processParameters)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IPrimitiveType<Date> authored = modelResolverGetterService.getDynamicDateType(processParameters.getQuestionnaireResponse(), "authored");
        if (authored != null) {
            return resourceBuilder.makeDateTime(authored);
        }
        return resourceBuilder.makeDateTime(Instant.now().toString());}

    final List<IBaseReference> getPerformer(ProcessParameters processParameters) {
        final IBaseReference author = modelResolverGetterService.getDynamicReferenceValue(processParameters.getQuestionnaireResponse(), "author");
        return Collections.singletonList(author);
    }

    final List<IBaseReference> getDerivedFrom(ProcessParameters processParameters)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseReference reference = resourceBuilder.makeReference(processParameters.getQuestionnaireResponse());
        return Collections.singletonList(reference);
    }
}
