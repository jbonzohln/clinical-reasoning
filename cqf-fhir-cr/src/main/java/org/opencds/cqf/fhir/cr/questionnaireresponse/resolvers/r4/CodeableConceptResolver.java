package org.opencds.cqf.fhir.cr.questionnaireresponse.resolvers.r4;

import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import java.util.List;
import java.util.stream.Collectors;

public class CodeableConceptResolver {

    public static CodeableConcept makeCodeableConcept(String codeSystem, String codeValue) {
        final Coding coding = new Coding();
        coding.setSystem(codeSystem);
        coding.setCode(codeValue);
        return makeCodeableConcept(List.of(coding));
    }
    public static CodeableConcept makeCodeableConcept(List<IBaseCoding> iBaseCodings) {
        final List<Coding> codings = iBaseCodings.stream().map(Coding.class::cast).collect(Collectors.toList());
        return new CodeableConcept().setCoding(codings);
    }


}
