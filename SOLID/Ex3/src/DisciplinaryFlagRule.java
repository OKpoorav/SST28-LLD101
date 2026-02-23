/**
 * Rule that checks for disciplinary flags.
 * Single responsibility: validate disciplinary status only.
 */
public class DisciplinaryFlagRule implements EligibilityRule {
    
    @Override
    public RuleResult check(StudentProfile student) {
        if (student.disciplinaryFlag != LegacyFlags.NONE) {
            return RuleResult.fail("disciplinary flag present");
        }
        return RuleResult.pass();
    }
}
