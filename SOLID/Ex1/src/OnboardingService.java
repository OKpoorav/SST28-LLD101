import java.util.*;

public class OnboardingService {
    private final InputParser parser;
    private final StudentValidator validator;
    private final StudentRepository repository;
    private final ConfirmationPrinter printer;

    public OnboardingService(InputParser parser, StudentValidator validator, 
                            StudentRepository repository, ConfirmationPrinter printer) {
        this.parser = parser;
        this.validator = validator;
        this.repository = repository;
        this.printer = printer;
    }

    public void registerFromRawInput(String raw) {
        printer.printInput(raw);

        Map<String, String> kv = parser.parse(raw);

        String name = kv.getOrDefault("name", "");
        String email = kv.getOrDefault("email", "");
        String phone = kv.getOrDefault("phone", "");
        String program = kv.getOrDefault("program", "");

        List<String> errors = validator.validate(name, email, phone, program);

        if (!errors.isEmpty()) {
            printer.printErrors(errors);
            return;
        }

        String id = IdUtil.nextStudentId(repository.count());
        StudentRecord rec = new StudentRecord(id, name, email, phone, program);

        repository.save(rec);

        printer.printSuccess(id, repository.count(), rec);
    }
}
