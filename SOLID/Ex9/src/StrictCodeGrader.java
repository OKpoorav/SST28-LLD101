public class StrictCodeGrader implements CodeGradeService {
    public int grade(Submission s, Rubric r) {
        int base = Math.min(70, 45 + s.code.length() % 35);
        return base + (r.bonus / 2);
    }
}