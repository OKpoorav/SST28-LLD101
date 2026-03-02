public interface ReportWriteService {
    String write(Submission submission, int plagiarismScore, int codeScore);
}