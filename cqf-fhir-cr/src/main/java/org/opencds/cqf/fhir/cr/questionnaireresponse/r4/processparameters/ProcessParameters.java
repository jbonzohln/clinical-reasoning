package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import java.util.List;
import java.util.Map;

public class ProcessParameters {
    private IBaseBackboneElement questionnaireResponseItem;
    private final IBaseBackboneElement questionnaireResponse;
    private IBaseReference subject;
    private final List<IBaseResource> resources;
    private final Map<String, List<IBaseCoding>> questionnaireCodeMap;

    public ProcessParameters(
        IBaseBackboneElement questionnaireResponseItem,
        IBaseBackboneElement questionnaireResponse,
        IBaseReference subject,
        List<IBaseResource> resources,
        Map<String, List<IBaseCoding>> questionnaireCodeMap) {
        this.questionnaireResponseItem = questionnaireResponseItem;
        this.questionnaireResponse = questionnaireResponse;
        this.subject = subject;
        this.resources = resources;
        this.questionnaireCodeMap = questionnaireCodeMap;
    }

    public IBaseBackboneElement getQuestionnaireResponseItem() {
        return questionnaireResponseItem;
    }

    public IBaseBackboneElement getQuestionnaireResponse() {
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
    public void setQuestionnaireResponseItem(IBaseBackboneElement item) {
        this.questionnaireResponseItem = item;
    }

    public void addToResources(IBaseResource resource) {
        this.resources.add(resource);
    }
}
