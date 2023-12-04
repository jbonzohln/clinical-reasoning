package org.opencds.cqf.fhir.cr.questionnaireresponse.common;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Type;
import org.opencds.cqf.fhir.api.Repository;
import javax.lang.model.type.PrimitiveType;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

public class ResourceBuilder {
    Repository repository;
    ModelResolverSetterService modelResolverSetterService;

    public IBaseDatatype makeCodeableConcept(List<IBaseDatatype> codingValues)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseDatatype codeableConcept = makeBaseResource("CodeableConcept");
        modelResolverSetterService.setProperty(codeableConcept, "coding", codingValues);
        return codeableConcept;
    }

    public IBaseDatatype makeCodeableConcept(IBaseDatatype ...codingValue)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseDatatype codeableConcept = makeBaseResource("CodeableConcept");
        modelResolverSetterService.setProperty(codeableConcept, "coding", List.of(codingValue));
        return codeableConcept;
    }

    public IBaseExtension makeExtension(String url, IBaseDatatype value, IBaseExtension subExtension)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        final IBaseExtension extension = makeBaseResource("Extension");
        extension.setUrl(url);
        extension.setValue(value);
        modelResolverSetterService.setProperty(extension, "extension", List.of(subExtension));
        return extension;
    }

    public IPrimitiveType<Date> makeDateTime(IPrimitiveType<Date> dateValue)
        throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {


        final IBaseDatatype datatype = makeBaseResource("IBaseDatatype")


        return makeBaseResource("IPrimitiveType", dateValue.getValue());
    }

    public IPrimitiveType<Date> makeDateTime(String dateValue)
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

    public <T extends IBase> T makeBaseResource(String elementName, Object constructorArguments)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return (T) repository
            .fhirContext()
            .getElementDefinition(elementName)
            .getImplementingClass()
            .getConstructor()
            .newInstance(constructorArguments);
    }
}
