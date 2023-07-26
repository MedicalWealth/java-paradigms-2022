package expression.generic;

public abstract class AbstractUnaryOperation<T extends Number> implements Operand<T> {
    protected final Operand<T> operand;

    protected AbstractUnaryOperation(final Operand<T> operand) {
        this.operand = operand;
    }

    @Override
    public T evaluate(final T x, final T y, final T z, final CalculationType<T> type) {
        return calc(operand.evaluate(x, y, z, type), type);
    }

    protected abstract T calc(final T operand, final CalculationType<T> type);
}
