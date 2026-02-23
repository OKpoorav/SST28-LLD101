/**
 * Interface for eligibility rules.
 * Follows Open/Closed Principle: new rules can be added without modifying existing code.
 */
public interface EligibilityRule {
    /**
     * Check if a student passes this rule.
     * @param student The student profile to evaluate
     * @return RuleResult indicating pass/fail and reason if failed
     */
    RuleResult check(StudentProfile student);
}
