import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Student Onboarding ===");
        
        Set<String> allowedPrograms = Set.of("CSE", "AI", "SWE");
        
        // Create dependencies
        StudentRepository db = new FakeDb();
        InputParser parser = new InputParser();
        StudentValidator validator = new StudentValidator(allowedPrograms);
        ConfirmationPrinter printer = new ConfirmationPrinter();
        
        OnboardingService svc = new OnboardingService(parser, validator, db, printer);

        // Example 1: Valid input
        String raw = "name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE";
        svc.registerFromRawInput(raw);

        System.out.println();
        
        // Example 2: Invalid input (fails validation)
        String raw2 = "name=;email=invalid-email;phone=abc123;program=MATH";
        svc.registerFromRawInput(raw2);

        System.out.println();
        System.out.println("-- DB DUMP --");
        System.out.print(TextTable.render3((FakeDb) db));
    }
}
