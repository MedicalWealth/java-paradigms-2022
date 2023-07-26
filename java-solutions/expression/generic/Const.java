package expression.generic;

public final class Const<T extends Number> extends AbstractValue<T> {
    private final T constant;

    public Const(final T constant) {
        this.constant = constant;
    }

    @Override
    protected T calc(final T x, final T y, final T z, final CalculationType<T> type) {
        return constant;
    }
}
