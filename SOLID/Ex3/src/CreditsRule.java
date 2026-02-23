/**
 * Rule that checks earned credits threshold.
 * Configurable threshold following Open/Closed Principle.
 */
public class CreditsRule implements EligibilityRule {
    private final int minCredits;
    
    public CreditsRule(int minCredits) {
        this.minCredits = minCredits;
    }
    
    @Override
    public RuleResult check(StudentProfile student) {
        if (student.earnedCredits < minCredits) {
            return RuleResult.fail("credits below " + minCredits);
        }
        return RuleResult.pass();
    }
}
