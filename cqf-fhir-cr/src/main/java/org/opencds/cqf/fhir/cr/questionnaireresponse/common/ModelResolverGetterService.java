package org.opencds.cqf.fhir.cr.questionnaireresponse.common;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseElement;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
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

    public List<IBaseBackboneElement> getDynamicValues(IBaseBackboneElement resource, String path) {
        return getDynamicValues((IBaseResource) resource, path);
    }

    public List<IBaseReference> getDynamicReferenceValues(IBaseBackboneElement resource, String path) {
        return getDynamicValues((IBaseResource) resource, path).stream()
            .map(IBaseReference.class::cast)
            .collect(Collectors.toList());
    }

    public IBaseReference getDynamicReferenceValue(IBaseBackboneElement resource, String path) {
        return (IBaseReference) getDynamicValue(resource, path);
    }

    public IBaseElement getDynamicBaseElement(IBaseBackboneElement resource, String path) {
        return (IBaseElement) getDynamicValue(resource, path);
    }

    public IBaseDatatype getDynamicDataType(IBaseBackboneElement resource, String path) {
        return (IBaseDatatype) getDynamicValue(resource, path);
    }

    public IPrimitiveType<Date> getDynamicDateType(IBaseBackboneElement resource, String path) {
        return (IPrimitiveType<Date>) getDynamicValue(resource, path);
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

}
