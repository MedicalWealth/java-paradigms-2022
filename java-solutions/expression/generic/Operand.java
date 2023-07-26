package expression.generic;

public interface Operand<T extends Number> {
    T evaluate(T x, T y, T z, CalculationType<T> type);
}
