package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.BaseDateTimeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent;
import org.hl7.fhir.r4.model.Type;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ModelResolverService;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ProcessorHelper;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParameters;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.CodeableConceptResolver;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.ExtensionResolver;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.ReferenceResolver;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class ObservationFactory {
    Repository repository;
    ProcessorHelper processorHelper;
    ModelResolverService modelResolverService;
    static final ObservationStatus status = Observation.ObservationStatus.FINAL;

    final Observation makeObservation(
        IBaseBackboneElement answer,
        ProcessParameters processParameters
    ) {
        final String id = getId(processParameters);
        final List<IBaseReference> basedOn = getBasedOn(processParameters);
        final List<IBaseReference> partOf = getPartOf(processParameters);
        final List<IBaseElement> category = getCategory();
        final IBaseElement code = getCode(processParameters);
        final IBaseReference encounter = getEncounter(processParameters);

        final Type effective = getEffective(processParameters);
        final InstantType issued = getIssued(processParameters);

        final List<IBaseReference> performer = getPerformer(processParameters);

        final Type value = getValue(answer);

        final List<IBaseReference> derived = getDerivedFrom(processParameters);
        final IBaseElement extension = getLinkExtension(processParameters);
        return new ObservationBuilder()
            .id(id)
            .basedOn(basedOn)
            .partOf(partOf)
            .performer(performer)
            .status(status)
            .category(category)
            .code(code)
            .subject(subject)
            .encounter(encounter)
            .effective(effective)
            .issuedElement(issued)
            .performer(performer)
            .value(value)
            .derived(derived)
            .extension(extension)
            .build();
    }

    final String getId(ProcessParameters processParameters) {
        final IBaseBackboneElement linkId = modelResolverService.getDynamicValue(processParameters.getQuestionnaireResponseItem(), "linkId");
        final String extractId = processorHelper.getExtractId((IBaseResource) processParameters.getQuestionnaireResponse());
        return extractId + "." + linkId;
    }

    final List<IBaseReference> getBasedOn(ProcessParameters processParameters) {
        return modelResolverService.getDynamicReferenceValues(processParameters.getQuestionnaireResponseItem(), "basedOn");
    }

    final List<IBaseReference> getPartOf(ProcessParameters processParameters) {
        return modelResolverService.getDynamicReferenceValues(processParameters.getQuestionnaireResponseItem(), "partOf");
    }

    IBaseDatatype getValue(QuestionnaireResponseItemAnswerComponent answer) {

        // ROSIE TODO: I think the fhirType will always be: "Element"

//        answer.getValue()
//        IBase.fhirType()

        switch (answer.getValue().fhirType()) {
            case "Coding":
                return new CodeableConcept().addCoding(answer.getValueCoding());
            case "date":
                return new DateTimeType(((DateType) answer.getValue()).getValue());
            default:
                return answer.getValue();
        }
    }

    IBaseElement getLinkExtension(ProcessParameters processParameters) {
        // ROSIE TODO
        // resolver insertion
        final String linkId = modelResolverService.getDynamicStringValue(processParameters.getQuestionnaireResponseItem(), "linkId");
        return ExtensionResolver.makeExtension(linkId);
    }

    IBaseElement getCode(ProcessParameters processParameters) {
        final String linkId = modelResolverService.getDynamicStringValue(processParameters.getQuestionnaireResponseItem(), "linkId");
        final Map<String, List<IBaseCoding>> coding = processParameters.getQuestionnaireCodeMap();
        return CodeableConceptResolver.makeCodeableConcept(coding.get(linkId));
    }

    List<IBaseElement> getCategory() {
        final String codeSystem = "http://hl7.org/fhir/observation-category";
        final String codeValue = "survey";
        final IBaseElement codeableConcept = CodeableConceptResolver.makeCodeableConcept(codeSystem, codeValue);
        return Collections.singletonList(codeableConcept);
    }

    IBaseElement makeCodeableConcept()
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (IBaseElement) repository
            .fhirContext()
            .getElementDefinition("CodeableConcept")
            .getImplementingClass()
            .getConstructor()
            .newInstance();
    }

    IBaseReference getEncounter(ProcessParameters processParameters) {
        return modelResolverService.getDynamicReferenceValue(processParameters.getQuestionnaireResponse(), "encounter");
    }
    Type getEffective(QuestionnaireResponse questionnaireResponse) {
        return getAuthoredDate(questionnaireResponse);
    }

    BaseDateTimeType getIssued(QuestionnaireResponse questionnaireResponse) {
        return new InstantType(getAuthoredDate(questionnaireResponse));
    }

    BaseDateTimeType getAuthoredDate(ProcessParameters processParameters) {
        final IBaseReference author = modelResolverService.getDynamicReferenceValue(processParameters.getQuestionnaireResponse(), "authored");
        if (author != null) {
            return new DateTimeType(author)
        }



        return new DateTimeType((questionnaireResponse.hasAuthored()
            ? questionnaireResponse.getAuthored().toInstant()
            : Instant.now())
            .toString());
    }

    List<IBaseReference> getPerformer(ProcessParameters processParameters) {
        final IBaseReference author = modelResolverService.getDynamicReferenceValue(processParameters.getQuestionnaireResponse(), "author");
        return Collections.singletonList(author);
    }

    List<IBaseReference> getDerivedFrom(ProcessParameters processParameters) {
        final IBaseReference reference = ReferenceResolver.makeReference(processParameters.getQuestionnaireResponse());
        return Collections.singletonList(reference);
    }
}
