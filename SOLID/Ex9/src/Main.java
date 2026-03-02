public class Main {
    public static void main(String[] args) {
        System.out.println("=== Evaluation Pipeline ===");
        Submission sub = new Submission("23BCS1007", "public class A{}", "A.java");
        RubricProvider rubricProvider = new DefaultRubricProvider();
        PlagiarismCheckService plagiarismChecker = new PlagiarismChecker();
        CodeGradeService codeGrader = new CodeGrader();
        ReportWriteService reportWriter = new ReportWriter();

        EvaluationPipeline pipeline = new EvaluationPipeline(
                rubricProvider,
                plagiarismChecker,
                codeGrader,
                reportWriter
        );
        pipeline.evaluate(sub);
    }
}
