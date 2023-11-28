package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.r4.model.Reference;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.CodingResolver;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.QuestionnaireResponseItemComponentResolver;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.QuestionnaireResponseResolver;
import org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4.ReferenceResolver;
import java.util.List;
import java.util.Map;

public class ProcessParameters {
    private final IBaseBackboneElement questionnaireResponseItem;
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
}
