/**
 * Rule that checks attendance percentage threshold.
 * Configurable threshold following Open/Closed Principle.
 */
public class AttendanceRule implements EligibilityRule {
    private final int minAttendancePct;
    
    public AttendanceRule(int minAttendancePct) {
        this.minAttendancePct = minAttendancePct;
    }
    
    @Override
    public RuleResult check(StudentProfile student) {
        if (student.attendancePct < minAttendancePct) {
            return RuleResult.fail("attendance below " + minAttendancePct);
        }
        return RuleResult.pass();
    }
}
