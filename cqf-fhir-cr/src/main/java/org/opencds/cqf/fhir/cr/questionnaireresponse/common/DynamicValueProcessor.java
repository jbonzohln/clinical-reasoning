package org.opencds.cqf.fhir.cr.questionnaireresponse.common;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DynamicValueProcessor {
    private final ModelResolver modelResolver;

    public DynamicValueProcessor(ModelResolver modelResolver) {
        this.modelResolver = modelResolver;
    }

    public boolean hasValue(IBaseBackboneElement resource, String path) {
        return getDynamicValue(resource, path) != null;
    }

    public boolean hasChildItems(IBaseBackboneElement resource, String path) {
        return !getDynamicValues(resource, path).isEmpty();
    }

    public IBaseBackboneElement getDynamicValue(IBaseBackboneElement resource, String path) {
        return (IBaseBackboneElement) modelResolver.resolvePath(resource, path);
    }

    public List<IBaseBackboneElement> getDynamicValues(IBaseBackboneElement resource, String path) {
        return getDynamicValues((IBaseResource) resource, path);
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
