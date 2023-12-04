package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction;

import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseIBaseReference;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.IBaseReference;
import org.hl7.fhir.r4.model.Type;
import javax.annotation.Nonnull;
import java.util.Date;
import java.util.List;

public class ObservationBuilder {

//    final List<IBaseIBaseReference> basedOn = getBasedOn(processParameters);
//    final List<IBaseIBaseReference> partOf = getPartOf(processParameters);
//    final List<IBaseElement> category = getCategory();
//    final IBaseDatatype code = getCode(processParameters);
//    final IBaseIBaseReference encounter = getEncounter(processParameters);
//    final PrimitiveType<Date> effective = getAuthoredDate(processParameters);
//    final PrimitiveType<Date> issued = getIssued(processParameters);
//    final List<IBaseIBaseReference> performer = getPerformer(processParameters);
//    final IBaseDatatype value = getValue(answer);
//    final List<IBaseIBaseReference> derived = getDerivedFrom(processParameters);
//    final IBaseExtension extension = getLinkExtension(processParameters);
    private String id;
    private List<IBaseReference> basedOn;
    private List<IBaseReference> partOf;
    private ObservationStatus status;
    private List<IBaseDatatype> category;
    private IBaseDatatype code;
    private IBaseReference encounter;
    private IBaseReference subject;
    private Type effective;
    private InstantType issuedElement;
    private List<IBaseReference> performer;
    private Type value;
    private List<IBaseReference> derivedFrom;
    private Extension extension;

    @Nonnull
    ObservationBuilder id(String id) {
        this.id = id;
        return this;
    }

    @Nonnull
    ObservationBuilder basedOn(List<IBaseReference> basedOn) {
        this.basedOn = basedOn;
        return this;
    }

    @Nonnull
    ObservationBuilder partOf(List<IBaseReference> partOf) {
        this.partOf = partOf;
        return this;
    }

    @Nonnull
    ObservationBuilder status(ObservationStatus status) {
        this.status = status;
        return this;
    }

    @Nonnull
    ObservationBuilder category(List<CodeableConcept> category) {
        this.category = category;
        return this;
    }

    @Nonnull
    ObservationBuilder code(CodeableConcept code) {
        this.code = code;
        return this;
    }

    @Nonnull
    ObservationBuilder subject(IBaseReference subject) {
        this.subject = subject;
        return this;
    }

    @Nonnull
    ObservationBuilder encounter(IBaseReference encounter) {
        this.encounter = encounter;
        return this;
    }

    @Nonnull
    ObservationBuilder effective(Type effective) {
        this.effective = effective;
        return this;
    }

    @Nonnull
    ObservationBuilder issuedElement(InstantType issued) {
        this.issuedElement = issued;
        return this;
    }

    @Nonnull
    ObservationBuilder performer(List<IBaseReference> performer) {
        this.performer = performer;
        return this;
    }

    @Nonnull
    ObservationBuilder value(Type value) {
        this.value = value;
        return this;
    }

    @Nonnull
    ObservationBuilder derived(List<IBaseReference> derivedFrom) {
        this.derivedFrom = derivedFrom;
        return this;
    }

    @Nonnull
    ObservationBuilder extension(Extension extension) {
        this.extension = extension;
        return this;
    }

    @Nonnull
    Observation build() {
        final Observation observation = new Observation();
        observation.setId(this.id);
        observation.setBasedOn(this.basedOn);
        observation.setPartOf(this.partOf);
        observation.setStatus(this.status);
        observation.setCategory(this.category);
        observation.setCode(this.code);
        observation.setEncounter(this.encounter);
        observation.setSubject(this.subject);
        observation.setEffective(this.effective);
        observation.setIssuedElement(this.issuedElement);
        observation.setPerformer(this.performer);
        observation.setValue(this.value);
        observation.setDerivedFrom(this.derivedFrom);
        observation.setExtension(List.of(this.extension));
        return observation;
    }
}
