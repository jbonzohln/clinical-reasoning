package org.opencds.cqf.fhir.cr.questionnaireresponse.r4;

import org.hl7.fhir.instance.model.api.IBaseBackboneElement;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.opencds.cqf.fhir.api.Repository;
import org.opencds.cqf.fhir.cql.engine.model.FhirModelResolverCache;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.DynamicValueProcessor;
import org.opencds.cqf.fhir.cr.questionnaireresponse.common.ProcessorHelper;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.defintionbasedextraction.ProcessDefinitionItem;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.observationbasedextraction.ProcessObservationItem;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParameters;
import org.opencds.cqf.fhir.cr.questionnaireresponse.r4.processparameters.ProcessParametersHelper;
import org.opencds.cqf.fhir.utility.Constants;
import java.util.ArrayList;
import java.util.List;

public class ProcessorService {
    ProcessObservationItem processObservationItem;
    ProcessDefinitionItem processDefinitionItem;
    DynamicValueProcessor dynamicValueProcessor;
    ProcessParametersHelper processParametersHelper;
    GroupProcessor groupProcessor;
    ProcessorHelper processorHelper;

    ProcessorService(Repository repository) {
        final ModelResolver modelResolver = FhirModelResolverCache.resolverForVersion(
            repository.fhirContext().getVersion().getVersion());
        this.dynamicValueProcessor = new DynamicValueProcessor(modelResolver);
    }

    public List<IBaseResource> processItems(IBaseBackboneElement questionnaireResponse) {
        final ArrayList<IBaseResource> resources = new ArrayList<>();
        final List<IBaseBackboneElement> questionnaireResponseItems = dynamicValueProcessor.getDynamicValues(questionnaireResponse, "item");
        questionnaireResponseItems.forEach(item -> {
            final ProcessParameters processParameters = processParametersHelper.createParameters(questionnaireResponse, item, resources);
            final boolean hasChildItems = dynamicValueProcessor.hasChildItems(item, "item");
            if (processDefinitionBased(questionnaireResponse, item)) {
                processDefinitionItem.process(processParameters);
            } else if (hasChildItems) {
                groupProcessor.processGroupItem(processParameters);
            } else {
                processObservationItem.process(processParameters);
            }
        });
        return resources;
    }

    boolean processDefinitionBased(
        IBaseBackboneElement questionnaireResponse,
        IBaseBackboneElement questionnaireResponseItem
    ) {
        final boolean hasExtension = processorHelper.hasExtension(questionnaireResponse, Constants.SDC_QUESTIONNAIRE_ITEM_POPULATION_CONTEXT);
        final boolean childItemsExist = dynamicValueProcessor.hasChildItems(questionnaireResponseItem, "item");
        final boolean hasDefinitionValue = dynamicValueProcessor.hasValue(questionnaireResponseItem, "definition");
        return hasExtension || (!childItemsExist && hasDefinitionValue);
    }
}
