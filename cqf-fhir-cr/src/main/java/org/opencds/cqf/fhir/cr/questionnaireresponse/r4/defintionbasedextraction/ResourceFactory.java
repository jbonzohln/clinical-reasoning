package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.defintionbasedextraction;

import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Property;
import org.hl7.fhir.r4.model.Resource;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ProcessorHelper;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParameters;

import java.util.List;

class ResourceFactory {
    private static final String INVALID_RESOURCE_TYPE_ERROR_MESSAGE = "Unable to determine resource type from item definition: %s";
    private final ProcessorHelper processorHelper = new ProcessorHelper();
    private final PropertyHelper propertyHelper = new PropertyHelper();

    Resource makeResource(ProcessParameters processParameters) {
        final String resourceType = getResourceType(processParameters.getItemResolver().getDefinition());
        final Resource baseResource = (Resource) processorHelper.newValue(resourceType);
        final IdType id = makeId(processParameters, resourceType);
        final Meta meta = makeMeta(processParameters);
        final Property subjectProperty = propertyHelper.getSubjectProperty(baseResource);
        final Property authorProperty = propertyHelper.getAuthorProperty(baseResource);
        final List<Property> dateProperties = propertyHelper.getDateProperties(baseResource);
        return new ResourceBuilder()
            .setBaseResource(baseResource)
            .setResourceType(resourceType)
            .setAuthoredDate(processParameters.getQuestionnaireResponseResolver().getAuthored())
            .setQuestionnaireResponseItem(processParameters.getItemResolver())
            .setId(id)
            .setMeta(meta)
            .setSubjectProperty(subjectProperty)
            .setSubjectPropertyValue(processParameters.getSubjectResolver())
            .setAuthorProperty(authorProperty)
            .setAuthorPropertyValue(processParameters.getQuestionnaireResponseResolver().getAuthor())
            .setDateProperties(dateProperties)
            .build();
    }

    private IdType makeId(ProcessParameters processParameters, String resourceType) {
        final String id = processorHelper.getExtractId(processParameters.getQuestionnaireResponseResolver()) + "-" + processParameters.getItemResolver().getLinkId();
        return new IdType(resourceType, id);
    }

    private String getResourceType(String definition) {
        if (!definition.contains("#")) {
            throw new IllegalArgumentException(String.format(INVALID_RESOURCE_TYPE_ERROR_MESSAGE, definition));
        }
        return definition.split("#")[1];
    }

    private Meta makeMeta(ProcessParameters processParameters) {
        final String definition = processParameters.getItemResolver().getDefinition();
        final Meta meta = new Meta();
        // Consider setting source and lastUpdated here?
        if (definition != null && !definition.isEmpty()) {
            meta.addProfile(definition.split("#")[0]);
        }
        return meta;
    }
}
