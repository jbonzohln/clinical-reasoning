package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.r4.model.Resource;

public class ResourceResolver {
    private final Resource resource;

    public ResourceResolver(Resource resource) {
        this.resource = resource;
    }

    public static ResourceResolver of(Resource resource) {
        return new ResourceResolver(resource);
    }

    public Resource getResource() {
        return this.resource;
    }
}
