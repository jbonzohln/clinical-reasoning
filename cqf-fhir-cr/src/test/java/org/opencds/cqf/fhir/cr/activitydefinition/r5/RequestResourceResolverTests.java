package org.opencds.cqf.fhir.cr.activitydefinition.r5;

import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.Appointment;
import org.hl7.fhir.r5.model.AppointmentResponse;
import org.hl7.fhir.r5.model.CarePlan;
import org.hl7.fhir.r5.model.Claim;
import org.hl7.fhir.r5.model.Communication;
import org.hl7.fhir.r5.model.CommunicationRequest;
import org.hl7.fhir.r5.model.CoverageEligibilityRequest;
import org.hl7.fhir.r5.model.DeviceRequest;
import org.hl7.fhir.r5.model.DiagnosticReport;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.EnrollmentRequest;
import org.hl7.fhir.r5.model.ImmunizationRecommendation;
import org.hl7.fhir.r5.model.MedicationRequest;
import org.hl7.fhir.r5.model.NutritionOrder;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Procedure;
import org.hl7.fhir.r5.model.RequestOrchestration;
import org.hl7.fhir.r5.model.ServiceRequest;
import org.hl7.fhir.r5.model.SupplyRequest;
import org.hl7.fhir.r5.model.Task;
import org.hl7.fhir.r5.model.Transport;
import org.hl7.fhir.r5.model.VisionPrescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.opencds.cqf.fhir.cr.activitydefinition.RequestResourceResolver.Given;
import org.opencds.cqf.fhir.utility.Ids;

@TestInstance(Lifecycle.PER_CLASS)
public class RequestResourceResolverTests {
    private final FhirContext fhirContext = FhirContext.forR5Cached();
    private final IIdType subjectId = Ids.newId(Patient.class, "patient123");
    private final IIdType practitionerId = Ids.newId(Practitioner.class, "practitioner123");
    private final IIdType encounterId = Ids.newId(Encounter.class, "encounter123");
    private final IIdType organizationId = Ids.newId(Organization.class, "org123");

    @SuppressWarnings("unchecked")
    private <R extends IBaseResource> R testResolver(String testId, Class<R> expectedClass) {
        var result = new Given()
                .repositoryFor(fhirContext, "r5")
                .activityDefinition(testId)
                .when()
                .subjectId(subjectId)
                .encounterId(encounterId)
                .practitionerId(practitionerId)
                .organizationId(organizationId)
                .resolve();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedClass, (Class<R>) result.getClass());

        return (R) result;
    }

    @Test
    public void testAppointmentResolver() {
        testResolver("appointment-test", Appointment.class);
    }

    @Test
    public void testAppointmentResponseResolver() {
        testResolver("appointmentresponse-test", AppointmentResponse.class);
    }

    @Test
    public void testCarePlanResolver() {
        testResolver("careplan-test", CarePlan.class);
    }

    @Test
    public void testClaimResolver() {
        testResolver("claim-test", Claim.class);
    }

    @Test
    public void testCommunicationRequestResolver() {
        testResolver("communicationrequest-test", CommunicationRequest.class);
    }

    @Test
    public void testCommunicationResolver() {
        testResolver("communication-test", Communication.class);
    }

    @Test
    public void testCoverageEligibilityRequestResolver() {
        testResolver("coverageeligibilityrequest-test", CoverageEligibilityRequest.class);
    }

    @Test
    public void testDeviceRequestResolver() {
        testResolver("devicerequest-test", DeviceRequest.class);
    }

    @Test
    public void testDiagnosticReportResolver() {
        testResolver("diagnosticreport-test", DiagnosticReport.class);
    }

    @Test
    public void testEnrollmentRequestResolver() {
        testResolver("enrollmentrequest-test", EnrollmentRequest.class);
    }

    @Test
    public void testImmunizationRecommendationResolver() {
        assertThrows(FHIRException.class, () -> {
            testResolver("immunizationrecommendation-test", ImmunizationRecommendation.class);
        });
        testResolver("RecommendImmunizationActivity", ImmunizationRecommendation.class);
    }

    @Test
    public void testMedicationRequestResolver() {
        testResolver("medicationrequest-test", MedicationRequest.class);
    }

    @Test
    public void testNutritionOrderResolver() {
        testResolver("nutritionorder-test", NutritionOrder.class);
    }

    @Test
    public void testProcedureResolver() {
        testResolver("procedure-test", Procedure.class);
    }

    @Test
    public void testRequestOrchestrationResolver() {
        testResolver("requestorchestration-test", RequestOrchestration.class);
    }

    @Test
    public void testServiceRequestResolver() {
        testResolver("servicerequest-test", ServiceRequest.class);
    }

    @Test
    public void testSupplyRequestResolver() {
        testResolver("supplyrequest-test", SupplyRequest.class);
    }

    @Test
    public void testTaskResolver() {
        testResolver("task-test", Task.class);
    }

    @Test
    public void testTransportResolver() {
        testResolver("transport-test", Transport.class);
    }

    @Test
    public void testVisionPrescriptionResolver() {
        testResolver("visionprescription-test", VisionPrescription.class);
    }

    @Test
    public void testUnsupported() {
        assertThrows(FHIRException.class, () -> {
            testResolver("unsupported-test", Task.class);
        });
    }
}
