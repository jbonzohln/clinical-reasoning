package org.opencds.cqf.fhir.cr.questionnaireresponse.common;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.defintionbasedextraction.DefinitionBasedResourceBuilder;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction.ObservationBuilder;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ResourceBuilder {
    Repository repository;
    ModelResolverSetterService modelResolverSetterService;

    public IBaseMetaType makeMeta(String definition)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseMetaType meta = makeBaseResource("Meta");
        if (definition != null && !definition.isEmpty()) {
            meta.addProfile(definition.split("#")[0]);
        }
        return meta;
    }

    public IBaseDatatype makeIdType(String resourceType, String id)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return makeBaseResource("IdType", resourceType, id);
    }

    public IBaseResource makeDefinitionBasedResource(
        DefinitionBasedResourceBuilder resourceBuilder) {
        final IBaseResource baseResource = resourceBuilder.getBaseResource();
        modelResolverSetterService.setProperty(baseResource, "id", resourceBuilder.getId());
        modelResolverSetterService.setProperty(baseResource, "meta", resourceBuilder.getMeta());
        final Map<String, IBaseReference> subjectProperty = resourceBuilder.getSubject();
        if (!subjectProperty.isEmpty()) {
            subjectProperty.keySet().forEach(key -> modelResolverSetterService.setProperty(baseResource, key, subjectProperty.get(key)));
        }
        final Map<String, IBaseReference> authorProperty = resourceBuilder.getAuthor();
        if (!authorProperty.isEmpty()) {
            authorProperty.keySet().forEach(key -> modelResolverSetterService.setProperty(baseResource, key, authorProperty.get(key)));
        }
        return baseResource;
    }

    public IBaseResource makeObservationBasedResource(ObservationBuilder observationBuilder)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseResource observation = makeBaseResource("Observation");
        modelResolverSetterService.setProperty(observation, "id", observationBuilder.getId());
        modelResolverSetterService.setProperty(observation, "basedOn", observationBuilder.getBasedOn());
        modelResolverSetterService.setProperty(observation, "partOf", observationBuilder.getPartOf());
        modelResolverSetterService.setProperty(observation, "status", observationBuilder.getStatus());
        modelResolverSetterService.setProperty(observation, "category", observationBuilder.getCategory());
        modelResolverSetterService.setProperty(observation, "code", observationBuilder.getCode());
        modelResolverSetterService.setProperty(observation, "encounter", observationBuilder.getEncounter());
        modelResolverSetterService.setProperty(observation, "subject", observationBuilder.getSubject());
        modelResolverSetterService.setProperty(observation, "effective", observationBuilder.getEffective());
        modelResolverSetterService.setProperty(observation, "issued", observationBuilder.getIssued());
        modelResolverSetterService.setProperty(observation, "performer", observationBuilder.getPerformer());
        modelResolverSetterService.setProperty(observation, "value", observationBuilder.getValue());
        modelResolverSetterService.setProperty(observation, "derivedFrom", observationBuilder.getDerivedFrom());
        modelResolverSetterService.setProperty(observation, "extension", observationBuilder.getExtension());
        return observation;
    }

    public IBaseDatatype makeCodeableConcept(List<IBaseDatatype> codingValues)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseDatatype codeableConcept = makeBaseResource("CodeableConcept");
        modelResolverSetterService.setProperty(codeableConcept, "coding", codingValues);
        return codeableConcept;
    }

    public IBaseDatatype makeCodeableConcept(String codeSystem, String codeValue)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseDatatype codeableConcept = makeBaseResource("CodeableConcept");
        modelResolverSetterService.setProperty(codeableConcept, "system", codeSystem);
        modelResolverSetterService.setProperty(codeableConcept, "value", codeValue);
        return codeableConcept;
    }

    public IBaseDatatype makeCodeableConcept(IBaseDatatype ...codingValue)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseDatatype codeableConcept = makeBaseResource("CodeableConcept");
        modelResolverSetterService.setProperty(codeableConcept, "coding", List.of(codingValue));
        return codeableConcept;
    }

    public IBaseDatatype makeExtension(String url, IBaseDatatype value, IBaseDatatype subExtension)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseDatatype extension = makeBaseResource("Extension");
        modelResolverSetterService.setProperty(extension, "url", url);
        modelResolverSetterService.setProperty(extension, "value", value);
        if (subExtension != null) {
            modelResolverSetterService.setProperty(extension, "extension", List.of(subExtension));
        }
        return extension;
    }

    public IBaseReference makeReference(IBaseResource resourceReference)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
         return makeBaseResource("Reference", resourceReference);
    }

    public IPrimitiveType<Date> makeDateTime(String dateValue)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return makeBaseResource("IPrimitiveType", dateValue);
    }

    public IPrimitiveType<Date> makeDateTime(IBaseDatatype dateValue)
        // TODO: how to get string from IBaseDatatype??
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return makeBaseResource("IPrimitiveType", dateValue);
    }

    public <T extends IBase> T makeBaseResource(String elementName)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) repository
            .fhirContext()
            .getElementDefinition(elementName)
            .getImplementingClass()
            .getConstructor()
            .newInstance();
    }

    public <T extends IBase> T makeBaseResource(String elementName, Object ...constructorArguments)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) repository
            .fhirContext()
            .getElementDefinition(elementName)
            .getImplementingClass()
            .getConstructor()
            .newInstance(constructorArguments);
    }
}
