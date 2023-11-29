package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters;

import org.hl7.fhir.QuestionnaireResponse;
import org.hl7.fhir.QuestionnaireResponseItem;
import org.hl7.fhir.Reference;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseResource;
import java.util.List;
import java.util.Map;

public class ProcessParameters {
    private QuestionnaireResponseItem questionnaireResponseItem;
    private final QuestionnaireResponse questionnaireResponse;
    private Reference subject;
    private final List<IBaseResource> resources;
    private final Map<String, List<IBaseCoding>> questionnaireCodeMap;

    public ProcessParameters(
        QuestionnaireResponseItem questionnaireResponseItem,
        QuestionnaireResponse questionnaireResponse,
        Reference subject,
        List<IBaseResource> resources,
        Map<String, List<IBaseCoding>> questionnaireCodeMap) {
        this.questionnaireResponseItem = questionnaireResponseItem;
        this.questionnaireResponse = questionnaireResponse;
        this.subject = subject;
        this.resources = resources;
        this.questionnaireCodeMap = questionnaireCodeMap;
    }

    public QuestionnaireResponseItem getQuestionnaireResponseItem() {
        return questionnaireResponseItem;
    }

    public QuestionnaireResponse getQuestionnaireResponse() {
        return questionnaireResponse;
    }

    public Reference getSubject() {
        return subject;
    }

    public List<IBaseResource> getResources() {
        return resources;
    }

    public Map<String, List<IBaseCoding>> getQuestionnaireCodeMap() {
        return questionnaireCodeMap;
    }

    public void setSubject(Reference reference) {
        this.subject = reference;
    }
    public void setQuestionnaireResponseItem(QuestionnaireResponseItem item) {
        this.questionnaireResponseItem = item;
    }

    public void addToResources(IBaseResource resource) {
        this.resources.add(resource);
    }
}
