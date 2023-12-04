package org.opencds.cqf.fhir.cr.questionnaireresponse.common;

import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import java.util.List;

public class ModelResolverSetterService {
    private final ModelResolver modelResolver;

    public ModelResolverSetterService(ModelResolver modelResolver) {
        this.modelResolver = modelResolver;
    }

    public void setProperty(IBaseDatatype element, String path, IBaseDatatype property) {
        modelResolver.setValue(element, path, property);
    }

    public void setProperty(IBaseDatatype element, String path, List<IBaseDatatype> properties) {
        modelResolver.setValue(element, path, properties);
    }

}
