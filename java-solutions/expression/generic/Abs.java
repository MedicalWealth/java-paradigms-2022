package expression.generic;

public final class Abs<T extends Number> extends AbstractUnaryOperation<T> {
    public Abs(Operand<T> operand) {
        super(operand);
    }

    @Override
    protected T calc(T operand, CalculationType<T> type) {
        return type.abs(operand);
    }
}
