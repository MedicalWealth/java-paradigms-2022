package expression.generic;

public final class Variable<T extends Number> extends AbstractValue<T> {
    private final String name;

    public Variable(final String name) {
        this.name = name;
    }

    @Override
    protected T calc(final T x, final T y, final T z, final CalculationType<T> type) {
        if ("x".equals(name)) {
            return x;
        } else if ("y".equals(name)) {
            return y;
        } else {
            return z;
        }
    }
}
