public class EvaluationPipeline {
    private final RubricProvider rubricProvider;
    private final PlagiarismCheckService plagiarismChecker;
    private final CodeGradeService codeGrader;
    private final ReportWriteService reportWriter;

    public EvaluationPipeline(
            RubricProvider rubricProvider,
            PlagiarismCheckService plagiarismChecker,
            CodeGradeService codeGrader,
            ReportWriteService reportWriter
    ) {
        this.rubricProvider = rubricProvider;
        this.plagiarismChecker = plagiarismChecker;
        this.codeGrader = codeGrader;
        this.reportWriter = reportWriter;
    }

    public void evaluate(Submission sub) {
        Rubric rubric = rubricProvider.get();

        int plag = plagiarismChecker.check(sub);
        System.out.println("PlagiarismScore=" + plag);

        int code = codeGrader.grade(sub, rubric);
        System.out.println("CodeScore=" + code);

        String reportName = reportWriter.write(sub, plag, code);
        System.out.println("Report written: " + reportName);

        int total = plag + code;
        String result = (total >= 90) ? "PASS" : "FAIL";
        System.out.println("FINAL: " + result + " (total=" + total + ")");
    }
}
