/**
 * Result of evaluating a single eligibility rule.
 * Separates data from the engine logic.
 */
public class RuleResult {
    public final boolean passed;
    public final String failureReason;
    
    private RuleResult(boolean passed, String failureReason) {
        this.passed = passed;
        this.failureReason = failureReason;
    }
    
    public static RuleResult pass() {
        return new RuleResult(true, null);
    }
    
    public static RuleResult fail(String reason) {
        return new RuleResult(false, reason);
    }
}
