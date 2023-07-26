package expression.generic;

public class Square<T extends Number> extends AbstractUnaryOperation<T> {
    public Square(final Operand<T> operand) {
        super(operand);
    }

    @Override
    protected T calc(final T operand, final CalculationType<T> type) {
        return type.square(operand);
    }
}
