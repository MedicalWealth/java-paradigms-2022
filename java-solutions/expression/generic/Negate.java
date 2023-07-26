package expression.generic;

public final class Negate<T extends Number> extends AbstractUnaryOperation<T> {
    public Negate(final Operand<T> operand) {
        super(operand);
    }

    @Override
    protected T calc(final T number, final CalculationType<T> type) {
        return type.multiply(type.parseTypeT("-1"), number);
    }
}
