package expression.generic;

public final class Add<T extends Number> extends AbstractBinaryOperation<T> {
    public Add(final Operand<T> firstOperand, final Operand<T> secondOperand) {
        super(firstOperand, secondOperand);
    }

    @Override
    protected T calc(final T first, final T second, final CalculationType<T> type) {
        return type.add(first, second);
    }
}
