public class DefaultRubricProvider implements RubricProvider {
    @Override
    public Rubric get() {
        return new Rubric();
    }
}