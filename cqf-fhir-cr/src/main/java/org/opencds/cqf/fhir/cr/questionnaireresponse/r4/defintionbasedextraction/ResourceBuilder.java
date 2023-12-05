package org.opencds.cqf.fhir.cr.questionnaireresponse.r4.defintionbasedextraction;

import ca.uhn.fhir.context.BaseRuntimeElementDefinition;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Property;
import org.hl7.fhir.r4.model.QuestionnaireResponse.QuestionnaireResponseItemComponent;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Type;
import org.opencds.cqf.cql.engine.model.ModelResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

class ResourceBuilder {
    protected static final Logger logger = LoggerFactory.getLogger(ResourceBuilder.class);
    private static final String PROPERTY_SETTING_ERROR_MESSAGE = "Error encountered attempting to set property (%s) on resource type (%s): %s";
    private final PropertyHelper propertyHelper = new PropertyHelper();
    ModelResolver modelResolver;
    private IBaseResource baseResource;
    private String resourceType;
    private IBaseResource questionnaireResponseItem;
    private Date authoredDate;
    private IBaseDatatype id;
    private IBaseMetaType meta;
    private Property subjectProperty;
    private Reference subjectPropertyValue;
    private Property authorProperty;
    private Reference authorPropertyValue;
    private List<Property> dateProperties;

    @Nonnull
    ResourceBuilder baseResource(IBaseResource resource) {
        this.baseResource = resource;
        return this;
    }

    @Nonnull
    ResourceBuilder resourceType(String resourceType) {
        this.resourceType = resourceType;
        return this;
    }

    @Nonnull
    ResourceBuilder questionnaireResponseItem(IBaseResource questionnaireResponseItem) {
        this.questionnaireResponseItem = questionnaireResponseItem;
        return this;
    }

    @Nonnull
    ResourceBuilder authoredDate(Date authoredDate) {
        this.authoredDate = authoredDate;
        return this;
    }

    @Nonnull
    ResourceBuilder id(IdType id) {
        this.id = id;
        return this;
    }

    @Nonnull
    ResourceBuilder meta(Meta meta) {
        this.meta = meta;
        return this;
    }

    @Nonnull
    ResourceBuilder subjectPropertyValue(Reference subject) {
        this.subjectPropertyValue = subject;
        return this;
    }

    @Nonnull
    ResourceBuilder authorPropertyValue(Reference author) {
        this.authorPropertyValue = author;
        return this;
    }

    @Nonnull
    ResourceBuilder subjectProperty(Property subjectProperty) {
        this.subjectProperty = subjectProperty;
        return this;
    }

    @Nonnull
    ResourceBuilder authorProperty(Property authorProperty) {
        this.authorProperty = authorProperty;
        return this;
    }

    @Nonnull
    ResourceBuilder dateProperties(List<Property> dateProperties) {
        this.dateProperties = dateProperties;
        return this;
    }

    @Nonnull
    public Resource build() {
        this.baseResource.setId(this.id);
        this.baseResource.setMeta(this.meta);
        if (this.subjectProperty != null) {
            this.baseResource.setProperty(
                this.subjectProperty.getName(),
                this.subjectPropertyValue
            );
        }
        if (this.authorProperty != null) {
            this.baseResource.setProperty(
                this.authorProperty.getName(),
                this.authorPropertyValue
            );
        }
        this.setDatePropertiesUsingModelResolver();
        this.setAnswerValuePropertiesUsingModelResolver();
        return this.baseResource;
    }

    void setAnswerValuePropertiesUsingModelResolver() {
        questionnaireResponseItem.getItem().forEach(childItem -> {
            if (childItem.hasDefinition()) {
                // First element is always the resource type, so it can be ignored
                final Type answerValue = childItem.getAnswerFirstRep().getValue();
                setPropertyValueWithModelResolver(getAnswerValuePath(childItem), answerValue);
            }
        });
    }

    private String getAnswerValuePath(QuestionnaireResponseItemComponent childItem) {
        final String[] definition = childItem.getDefinition().split("#");
        return definition[1].replace(resourceType + ".", "");
    }

    void setDatePropertiesUsingModelResolver() {
        if (dateProperties != null && !dateProperties.isEmpty() && authoredDate != null) {
            dateProperties.forEach(property -> {
                try {
                    final Object datePropertyValue = getDatePropertyValue(property);
                    setPropertyValueWithModelResolver(property.getName(), datePropertyValue);
                } catch (Exception ex) {
                    logger.error(String.format(
                        PROPERTY_SETTING_ERROR_MESSAGE,
                        property.getName(), baseResource.fhirType(), ex.getMessage())
                    );
                }
            });
        }
    }

    private Object getDatePropertyValue(Property property)
        throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        final BaseRuntimeElementDefinition propertyDef = getPropertyDefinition(property);
        return propertyDef
            .getImplementingClass()
            .getConstructor(Date.class)
            .newInstance(authoredDate);
    }

    private BaseRuntimeElementDefinition getPropertyDefinition(Property property) {
        return repository.fhirContext().getElementDefinition(
            property.getTypeCode().contains("|")
                ? property.getTypeCode().split("\\|")[0]
                : property.getTypeCode());
    }

    void setPropertyValueWithModelResolver(String propertyName, Object propertyValue) {
        if (propertyValue != null) {
            modelResolver.setValue(
                baseResource,
                propertyName,
                propertyValue
            );
        }
    }
}
