public class Main {
    public static void main(String[] args) {
        System.out.println("=== Evaluation Pipeline ===");
        Submission sub = new Submission("23BCS1007", "public class A{}", "A.java");
        RubricProvider rubricProvider = new DefaultRubricProvider();
        PlagiarismCheckService plagiarismChecker = new PlagiarismChecker();
        CodeGradeService codeGrader = selectGrader(args);
        ReportWriteService reportWriter = new ReportWriter();

        EvaluationPipeline pipeline = new EvaluationPipeline(
                rubricProvider,
                plagiarismChecker,
                codeGrader,
                reportWriter
        );
        pipeline.evaluate(sub);
    }

    private static CodeGradeService selectGrader(String[] args) {
        if (args.length > 0 && "strict".equalsIgnoreCase(args[0])) {
            return new StrictCodeGrader();
        }
        return new CodeGrader();
    }
}
