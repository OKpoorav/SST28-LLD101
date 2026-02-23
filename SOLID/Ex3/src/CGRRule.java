/**
 * Rule that checks CGR (Cumulative Grade Rating) threshold.
 * Configurable threshold following Open/Closed Principle.
 */
public class CGRRule implements EligibilityRule {
    private final double minCgr;
    
    public CGRRule(double minCgr) {
        this.minCgr = minCgr;
    }
    
    @Override
    public RuleResult check(StudentProfile student) {
        if (student.cgr < minCgr) {
            return RuleResult.fail("CGR below " + String.format("%.1f", minCgr));
        }
        return RuleResult.pass();
    }
}
