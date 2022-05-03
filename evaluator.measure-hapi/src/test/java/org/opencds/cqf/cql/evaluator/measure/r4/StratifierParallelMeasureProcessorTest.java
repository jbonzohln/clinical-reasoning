package org.opencds.cqf.cql.evaluator.measure.r4;

import org.hl7.fhir.r4.model.MeasureReport;
import org.hl7.fhir.r4.model.MeasureReport.MeasureReportGroupComponent;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;


@Test()
public class StratifierParallelMeasureProcessorTest extends BaseMeasureProcessorTest {
    public StratifierParallelMeasureProcessorTest() {
        super("PrimaryCariesPreventionasOfferedbyPCPsincludingDentistsFHIR-bundle.json", true, 8);
    }

    @Test
    public void exm74_subject_list() {
        MeasureReport report = this.measureProcessor.evaluateMeasure("http://ecqi.healthit.gov/ecqms/Measure/PrimaryCariesPreventionasOfferedbyPCPsincludingDentistsFHIR", "2019-01-01", "2020-01-01", "subject-list", null, null, null, endpoint, endpoint, endpoint, null);

        MeasureReportGroupComponent mrgc = report.getGroup().get(0);
        // 16 total patients in the test set.
        // 15 in initial population, 3 excluded from the denominator
        // 6 included in the numerator
        validateGroup(mrgc, "initial-population", 15);
        validateGroup(mrgc, "denominator-exclusion", 3);
        validateGroup(mrgc, "denominator", 12);
        validateGroup(mrgc, "numerator", 6);
        validateGroupScore(mrgc, new BigDecimal("0.5"));

        assertEquals(mrgc.getStratifier().size(), 3);

        // Stratifiers should cover the total population, so we expect 
        // initial-population true (5) + initial-population false (10) = initial-population total (15)
        validateStratifier(mrgc.getStratifierFirstRep(), "true", "initial-population", 5);
        validateStratifier(mrgc.getStratifierFirstRep(), "true", "numerator", 2);
        validateStratumScore(mrgc.getStratifierFirstRep(), "true", new BigDecimal("0.5"));

        validateStratifier(mrgc.getStratifierFirstRep(), "false", "initial-population", 10);
        validateStratifier(mrgc.getStratifierFirstRep(), "false", "numerator", 4);
        validateStratumScore(mrgc.getStratifierFirstRep(), "false", new BigDecimal("0.5"));
    }

}