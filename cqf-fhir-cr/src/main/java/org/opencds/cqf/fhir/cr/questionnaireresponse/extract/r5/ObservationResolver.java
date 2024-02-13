package org.opencds.cqf.fhir.cr.questionnaireresponse.extract.r5;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.CodeableConcept;
import org.hl7.fhir.r5.model.Coding;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.DateType;
import org.hl7.fhir.r5.model.Enumerations.ObservationStatus;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.InstantType;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.QuestionnaireResponse;
import org.hl7.fhir.r5.model.QuestionnaireResponse.QuestionnaireResponseItemAnswerComponent;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.StringType;
import org.opencds.cqf.fhir.cr.questionnaireresponse.extract.ExtractRequest;

public class ObservationResolver {
    public IBaseResource resolve(
            ExtractRequest request,
            IBaseBackboneElement baseAnswer,
            String linkId,
            IBaseReference subject,
            Map<String, List<IBaseCoding>> questionnaireCodeMap) {
        var questionnaireResponse = (QuestionnaireResponse) request.getQuestionnaireResponse();
        var answer = (QuestionnaireResponseItemAnswerComponent) baseAnswer;
        var obs = new Observation();
        obs.setId(request.getExtractId() + "." + linkId);
        obs.setBasedOn(questionnaireResponse.getBasedOn());
        obs.setPartOf(questionnaireResponse.getPartOf());
        obs.setStatus(ObservationStatus.FINAL);

        var qrCategoryCoding = new Coding();
        qrCategoryCoding.setCode("survey");
        qrCategoryCoding.setSystem("http://hl7.org/fhir/observation-category");
        obs.setCategory(Collections.singletonList(new CodeableConcept().addCoding(qrCategoryCoding)));

        obs.setCode(new CodeableConcept()
                .setCoding(questionnaireCodeMap.get(linkId).stream()
                        .map(c -> (Coding) c)
                        .collect(Collectors.toList())));
        obs.setSubject((Reference) subject);
        // obs.setFocus();
        obs.setEncounter(questionnaireResponse.getEncounter());
        var authoredDate = new DateTimeType((questionnaireResponse.hasAuthored()
                        ? questionnaireResponse.getAuthored().toInstant()
                        : Instant.now())
                .toString());
        obs.setEffective(authoredDate);
        obs.setIssuedElement(new InstantType(authoredDate));
        obs.setPerformer(Collections.singletonList(questionnaireResponse.getAuthor()));

        switch (answer.getValue().fhirType()) {
            case "Coding":
                obs.setValue(new CodeableConcept().addCoding(answer.getValueCoding()));
                break;
            case "date":
                obs.setValue(new DateTimeType(((DateType) answer.getValue()).getValue()));
                break;
            default:
                obs.setValue(answer.getValue());
        }
        obs.setDerivedFrom(Collections.singletonList(new Reference(questionnaireResponse)));

        var linkIdExtension = new Extension();
        linkIdExtension.setUrl("http://hl7.org/fhir/uv/sdc/StructureDefinition/derivedFromLinkId");
        var innerLinkIdExtension = new Extension();
        innerLinkIdExtension.setUrl("text");
        innerLinkIdExtension.setValue(new StringType(linkId));
        linkIdExtension.setExtension(Collections.singletonList(innerLinkIdExtension));
        obs.addExtension(linkIdExtension);
        return obs;
    }
}