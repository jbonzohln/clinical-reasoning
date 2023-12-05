package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.defintionbasedextraction;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Enumerations.FHIRAllTypes;
import org.hl7.fhir.r4.model.Property;
import org.hl7.fhir.r4.model.Resource;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ModelResolverGetterService;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ProcessorHelper;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParameters;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class ResourceFactory {
    private static final String INVALID_RESOURCE_TYPE_ERROR_MESSAGE = "Unable to determine resource type from item definition: %s";
    private final ProcessorHelper processorHelper = new ProcessorHelper();
    private final PropertyHelper propertyHelper = new PropertyHelper();
    private final ModelResolverGetterService modelResolverGetterService;
    private final org.opencds.cqf.fhir.cr.questionnaireresponse.common.ResourceBuilder resourceBuilder = new org.opencds.cqf.fhir.cr.questionnaireresponse.common.ResourceBuilder();


    ResourceFactory(ModelResolverGetterService modelResolverGetterService) {
        this.modelResolverGetterService = modelResolverGetterService;
    }

    Resource makeResource(ProcessParameters processParameters)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final String resourceType = getResourceType(processParameters);
        final IBaseResource baseResource = resourceBuilder.makeBaseResource(resourceType);
        final IBaseDatatype id = makeId(processParameters);
        final IBaseMetaType meta = makeMeta(processParameters);

        final Property subjectProperty = getSubjectProperty(baseResource);
        final Property authorProperty = getAuthorProperty(baseResource);
        final List<Property> dateProperties = getDateProperties(baseResource);

        return new ResourceBuilder()
            .baseResource(baseResource)
            .resourceType(resourceType)
            .authoredDate(processParameters.getQuestionnaireResponseResolver().getAuthored())
            .questionnaireResponseItem(processParameters.getItemResolver())
            .setId(id)
            .setMeta(meta)
            .setSubjectProperty(subjectProperty)
            .setSubjectPropertyValue(processParameters.getSubjectResolver())
            .setAuthorProperty(authorProperty)
            .setAuthorPropertyValue(processParameters.getQuestionnaireResponseResolver().getAuthor())
            .setDateProperties(dateProperties)
            .build();
    }

    private IBaseDatatype makeId(ProcessParameters processParameters)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseBackboneElement linkId = modelResolverGetterService.getDynamicValue(processParameters.getQuestionnaireResponseItem(), "linkId");
        final String extractId = processorHelper.getExtractId(processParameters.getQuestionnaireResponse());
        final String id = extractId + "-" + linkId;
        final String resourceType = getResourceType(processParameters);
        return resourceBuilder.makeIdType(resourceType, id);
    }

    private String getResourceType(ProcessParameters processParameters) {
        final String definition = getQuestionnaireResponseItemDefinition(processParameters);
        if (!definition.contains("#")) {
            throw new IllegalArgumentException(String.format(INVALID_RESOURCE_TYPE_ERROR_MESSAGE, definition));
        }
        return definition.split("#")[1];
    }

    private IBaseMetaType makeMeta(ProcessParameters processParameters)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final String definition = getQuestionnaireResponseItemDefinition(processParameters);
        return resourceBuilder.makeMeta(definition);
    }

    private String getQuestionnaireResponseItemDefinition(ProcessParameters processParameters) {
        final IBaseResource questionnaireResponseItem = processParameters.getQuestionnaireResponseItem();
        final IPrimitiveType<String> definition = modelResolverGetterService.getDynamicStringType(questionnaireResponseItem, "definition");
        return definition.getValueAsString();
    }

    List<Property> getDateProperties(Resource resource) {
        final List<Property> results = new ArrayList<>();
        results.add(resource.getNamedProperty(NamedProperties.ONSET));
        results.add(resource.getNamedProperty(NamedProperties.ISSUED));
        results.add(resource.getNamedProperty(NamedProperties.EFFECTIVE));
        results.add(resource.getNamedProperty(NamedProperties.RECORD_DATE));
        return results.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    Property getSubjectProperty(Resource resource) {
        final Property subjectProperty = resource.getNamedProperty(NamedProperties.SUBJECT);
        if (subjectProperty == null) {
            return resource.getNamedProperty(NamedProperties.PATIENT);
        }
        return subjectProperty;
    }

    Property getAuthorProperty(Resource resource) {
        final Property property = resource.getNamedProperty(NamedProperties.RECORDER);
        if (property == null && resource.fhirType().equals(FHIRAllTypes.OBSERVATION.toCode())) {
            return resource.getNamedProperty(NamedProperties.PERFORMER);
        }
        return property;
    }

}
