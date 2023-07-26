package expression.generic;

public abstract class AbstractBinaryOperation<T extends Number> implements Operand<T> {
    protected final Operand<T> firstOperand;
    protected final Operand<T> secondOperand;

    protected AbstractBinaryOperation(final Operand<T> firstOperand, final Operand<T> secondOperand) {
        this.firstOperand = firstOperand;
        this.secondOperand = secondOperand;
    }

    @Override
    public T evaluate(final T x, final T y, final T z, final CalculationType<T> type) {
        return calc(firstOperand.evaluate(x, y, z, type), secondOperand.evaluate(x, y, z, type), type);
    }

    protected abstract T calc(final T first, final T second, final CalculationType<T> type);
}
