import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Placement Eligibility ===");
        
        // Configuration (stretch goal: read from config object)
        RuleInput config = new RuleInput();
        
        // Build rule list - order matters (same as original if/else chain)
        // OCP Benefit: Add new rules just by creating new class and adding to list!
        List<EligibilityRule> rules = List.of(
            new DisciplinaryFlagRule(),
            new CGRRule(config.minCgr),
            new AttendanceRule(config.minAttendance),
            new CreditsRule(config.minCredits)
        );
        
        // Wire up the engine with rules
        EligibilityEngine engine = new EligibilityEngine(new FakeEligibilityStore(), rules);
        
        // Test with original student
        StudentProfile s = new StudentProfile("23BCS1001", "Ayaan", 8.10, 72, 18, LegacyFlags.NONE);
        engine.runAndPrint(s);
        
        System.out.println();
        
        // Demonstrate configurable thresholds - stricter rules without touching rule classes!
        RuleInput stricterConfig = new RuleInput();
        stricterConfig.minCgr = 8.5;
        stricterConfig.minAttendance = 80;
        stricterConfig.minCredits = 25;
        
        List<EligibilityRule> stricterRules = List.of(
            new DisciplinaryFlagRule(),
            new CGRRule(stricterConfig.minCgr),
            new AttendanceRule(stricterConfig.minAttendance),
            new CreditsRule(stricterConfig.minCredits)
        );
        
        EligibilityEngine strictEngine = new EligibilityEngine(new FakeEligibilityStore(), stricterRules);
        
        // Test eligible student with standard rules
        StudentProfile s2 = new StudentProfile("23BCS1002", "Priya", 8.70, 85, 22, LegacyFlags.NONE);
        System.out.println("--- Testing with standard rules ---");
        engine.runAndPrint(s2);
        
        System.out.println();
        System.out.println("--- Testing same student with stricter rules ---");
        strictEngine.runAndPrint(s2);
    }
}
