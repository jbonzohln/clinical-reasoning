package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction;

import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ResourceBuilder;
import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

public class ObservationBuilder {
    // TODO: fix observationstatus field
    private String id;
    private List<IBaseReference> basedOn;
    private List<IBaseReference> partOf;
    private String status;
    private List<IBaseDatatype> category;
    private IBaseDatatype code;
    private IBaseReference encounter;
    private IBaseReference subject;
    private IPrimitiveType<Date> effective;
    private IPrimitiveType<Date> issued;
    private List<IBaseReference> performer;
    private IBaseDatatype value;
    private List<IBaseReference> derivedFrom;
    private List<IBaseDatatype> extension;
    ResourceBuilder resourceBuilder;

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
    ObservationBuilder status(String status) {
        this.status = status;
        return this;
    }

    @Nonnull
    ObservationBuilder category(List<IBaseDatatype> category) {
        this.category = category;
        return this;
    }

    @Nonnull
    ObservationBuilder code(IBaseDatatype code) {
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
    ObservationBuilder effective(IPrimitiveType<Date> effective) {
        this.effective = effective;
        return this;
    }

    @Nonnull
    ObservationBuilder issued(IPrimitiveType<Date> issued) {
        this.issued = issued;
        return this;
    }

    @Nonnull
    ObservationBuilder performer(List<IBaseReference> performer) {
        this.performer = performer;
        return this;
    }

    @Nonnull
    ObservationBuilder value(IBaseDatatype value) {
        this.value = value;
        return this;
    }

    @Nonnull
    ObservationBuilder derived(List<IBaseReference> derivedFrom) {
        this.derivedFrom = derivedFrom;
        return this;
    }

    @Nonnull
    ObservationBuilder extension(List<IBaseDatatype> extension) {
        this.extension = extension;
        return this;
    }

    @Nonnull
    IBaseResource build()
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return resourceBuilder.makeObservation(this);
    }

    public String getId() {
        return id;
    }
    public List<IBaseReference> getBasedOn() {
        return basedOn;
    }

    public List<IBaseReference> getPartOf() {
        return partOf;
    }

    public String getStatus() {
        return status;
    }

    public List<IBaseDatatype> getCategory() {
        return category;
    }

    public IBaseDatatype getCode() {
        return code;
    }

    public IBaseReference getEncounter() {
        return encounter;
    }

    public IBaseReference getSubject() {
        return subject;
    }

    public IPrimitiveType<Date> getEffective() {
        return effective;
    }

    public IPrimitiveType<Date> getIssued() {
        return issued;
    }

    public List<IBaseReference> getPerformer() {
        return performer;
    }

    public IBaseDatatype getValue() {
        return value;
    }

    public List<IBaseReference> getDerivedFrom() {
        return derivedFrom;
    }

    public List<IBaseDatatype> getExtension() {
        return extension;
    }
}
