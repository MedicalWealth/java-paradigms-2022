package expression.generic;

public interface CalculationType<T extends Number> {
    T add(T first, T second);
    T subtract(T first, T second);
    T multiply(T first, T second);
    T divide(T first, T second);
    T abs(T number);
    T square(T number);
    T mod(T first, T second);
    T parseTypeT(String x);
    T parseTypeT(int x);
    boolean isPartOfNumber(char ch);
}
