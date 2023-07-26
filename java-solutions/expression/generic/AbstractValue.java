package expression.generic;

public abstract class AbstractValue<T extends Number> implements Operand<T> {
    @Override
    public T evaluate(final T x, final T y, final T z, final CalculationType<T> type) {
        return calc(x, y, z, type);
    }

    protected abstract T calc(final T x, final T y, final T z, final CalculationType<T> type);
}
