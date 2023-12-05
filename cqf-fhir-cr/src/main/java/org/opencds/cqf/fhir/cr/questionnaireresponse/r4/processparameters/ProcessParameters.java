package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters;

import org.hl7.fhir.QuestionnaireResponse;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IDomainResource;
import java.util.List;
import java.util.Map;

public class ProcessParameters {
    private IBaseResource questionnaireResponseItem;
    private final IBaseResource questionnaireResponse;
    private IBaseReference subject;
    private final List<IBaseResource> resources;
    private final Map<String, List<IBaseCoding>> questionnaireCodeMap;

    public ProcessParameters(
        IBaseResource questionnaireResponseItem,
        IBaseResource questionnaireResponse,
        IBaseReference subject,
        List<IBaseResource> resources,
        Map<String, List<IBaseCoding>> questionnaireCodeMap) {
        this.questionnaireResponseItem = questionnaireResponseItem;
        this.questionnaireResponse = questionnaireResponse;
        this.subject = subject;
        this.resources = resources;
        this.questionnaireCodeMap = questionnaireCodeMap;
    }

    public IBaseResource getQuestionnaireResponseItem() {
        return questionnaireResponseItem;
    }

    public IBaseResource getQuestionnaireResponse() {
        return questionnaireResponse;
    }

    public IBaseReference getSubject() {
        return subject;
    }

    public List<IBaseResource> getResources() {
        return resources;
    }

    public Map<String, List<IBaseCoding>> getQuestionnaireCodeMap() {
        return questionnaireCodeMap;
    }

    public void setSubject(IBaseReference reference) {
        this.subject = reference;
    }
    public void setQuestionnaireResponseItem(IBaseResource item) {
        this.questionnaireResponseItem = item;
    }

    public void addToResources(IBaseResource resource) {
        this.resources.add(resource);
    }
}
