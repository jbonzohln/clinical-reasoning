package org.opencds.cqf.fhir.cr.questionnaireresponse.common;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.BaseRuntimeElementCompositeDefinition;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.ICompositeType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.opencds.cqf.cql.engine.fhir.exception.UnknownType;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ModelResolverGetterService {
    private final ModelResolver modelResolver;

    public ModelResolverGetterService(ModelResolver modelResolver) {
        this.modelResolver = modelResolver;
    }

    public boolean hasValue(IBaseBackboneElement resource, String path) {
        return getDynamicValue(resource, path) != null;
    }

    public boolean hasChildItems(IBaseBackboneElement resource, String path) {
        return !getDynamicValues(resource, path).isEmpty();
    }

    public IBaseDatatype getDataTypeValue(IBaseBackboneElement resource, String path) {
        // ROUTODO: FIX THESE
        return (IBaseDatatype) modelResolver.resolvePath(resource, path);
    }

    public IBaseBackboneElement getDynamicValue(IBaseBackboneElement resource, String path) {
        return (IBaseBackboneElement) modelResolver.resolvePath(resource, path);
    }

    public String getDynamicStringValue(IBaseBackboneElement resource, String path) {
        // ROSIE TODO: check string conversion here

        final IPrimitiveType<String> ibase = getIBase();
        ibase.getValue();

        return modelResolver.resolvePath(resource, path).toString();
    }

    public String getDynamicStringValue(IBaseResource resource, String path) {
        // ROSIE TODO: check string conversion here

        final IPrimitiveType<String> ibase = getIBase();
        ibase.getValue();

        return modelResolver.resolvePath(resource, path).toString();
    }

    public List<IBaseResource> getDynamicValues(IBaseBackboneElement resource, String path) {

        return getDynamicValues((IBaseResource) resource, path).stream().map(r -> (IBaseResource) r).collect(
            Collectors.toList());
    }

    public List<IBaseReference> getDynamicReferenceValues(IBaseResource resource, String path) {
        return getDynamicValues(resource, path).stream()
            .map(IBaseReference.class::cast)
            .collect(Collectors.toList());
    }

    public IBaseReference getDynamicReferenceValue(IBaseResource resource, String path) {
        return (IBaseReference) getDynamicValue(resource, path);
    }

    public IBaseElement getDynamicBaseElement(IBaseBackboneElement resource, String path) {
        return (IBaseElement) getDynamicValue(resource, path);
    }

    public IBaseDatatype getDynamicDataType(IBaseBackboneElement resource, String path) {
        return (IBaseDatatype) getDynamicValue(resource, path);
    }

    public IPrimitiveType<Date> getDynamicDateType(IBaseResource resource, String path) {
        return (IPrimitiveType<Date>) getDynamicValue(resource, path);
    }

    public IPrimitiveType<String> getDynamicStringType(IBaseResource resource, String path) {
        return (IPrimitiveType<String>) getDynamicValue(resource, path);
    }

    public IBaseBackboneElement getDynamicValue(IBaseResource resource, String path) {
        // todo: check if list, or check if single value
        final List<IBaseBackboneElement> dynamicValues = new ArrayList<>();
        final Object pathResult = modelResolver.resolvePath(resource, path);
        var list = (pathResult instanceof List ? (List<?>) pathResult : null);
        if (list != null && !list.isEmpty()) {
            dynamicValues.addAll(
                list.stream().map(o -> (IBaseBackboneElement) o).collect(Collectors.toList()));
        }
        return dynamicValues;
    }

    public List<IBaseResource> getDynamicResourceValues(IBaseResource resource, String path) {
        final List<IBaseBackboneElement> dynamicValues = new ArrayList<>();
        final Object pathResult = modelResolver.resolvePath(resource, path);
        var list = (pathResult instanceof List ? (List<?>) pathResult : null);
        if (list != null && !list.isEmpty()) {
            dynamicValues.addAll(
                list.stream().map(o -> (IBaseBackboneElement) o).collect(Collectors.toList()));
        }
        return dynamicValues;
    }

    public List<IBaseBackboneElement> getDynamicValues(IBaseResource resource, String path) {
        final List<IBaseBackboneElement> dynamicValues = new ArrayList<>();
        final Object pathResult = modelResolver.resolvePath(resource, path);
        var list = (pathResult instanceof List ? (List<?>) pathResult : null);
        if (list != null && !list.isEmpty()) {
            dynamicValues.addAll(
                list.stream().map(o -> (IBaseBackboneElement) o).collect(Collectors.toList()));
        }
        return dynamicValues;
    }

    public boolean hasPropertyName(IBase theResource, String path, FhirContext fhirContext) {
        final BaseRuntimeElementCompositeDefinition<?> definition = this.resolveRuntimeDefinition(theResource, fhirContext);
        final BaseRuntimeChildDefinition child = definition.getChildByName(path);
        return child != null;
    }

    private BaseRuntimeElementCompositeDefinition<?> resolveRuntimeDefinition(IBase base, FhirContext fhirContext) {
        if (base instanceof IAnyResource) {
            return fhirContext.getResourceDefinition((IAnyResource)base);
        } else if (!(base instanceof IBaseBackboneElement) && !(base instanceof IBaseElement)) {
            if (base instanceof ICompositeType) {
                return (BaseRuntimeElementCompositeDefinition) fhirContext.getElementDefinition(base.getClass());
            } else {
                throw new UnknownType(String.format("Unable to resolve the runtime definition for %s", base.getClass().getName()));
            }
        } else {
            return (BaseRuntimeElementCompositeDefinition) fhirContext.getElementDefinition(base.getClass());
        }
    }

}
