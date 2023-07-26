package expression;

public interface Operand extends TripleExpression {
    int getFirstPriority();

    boolean isSpecialOperation();

    default String getOperation() {
        throw new UnsupportedOperationException("Class without operation");
    }
}
