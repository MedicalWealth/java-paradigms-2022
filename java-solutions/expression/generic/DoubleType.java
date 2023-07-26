package expression.generic;

public final class DoubleType implements CalculationType<Double> {
    @Override
    public Double add(final Double first, final Double second) {
        return first + second;
    }

    @Override
    public Double subtract(final Double first, final Double second) {
        return first - second;
    }

    @Override
    public Double multiply(final Double first, final Double second) {
        return first * second;
    }

    @Override
    public Double divide(final Double first, final Double second) {
        return first / second;
    }

    @Override
    public Double abs(Double number) {
        return Math.abs(number);
    }

    @Override
    public Double square(Double number) {
        return number * number;
    }

    @Override
    public Double mod(Double first, Double second) {
        return first % second;
    }

    @Override
    public Double parseTypeT(final String x) {
        return Double.parseDouble(x);
    }

    @Override
    public Double parseTypeT(int x) {
        return (double) x;
    }

    @Override
    public boolean isPartOfNumber(char ch) {
        return Character.isDigit(ch) || ch == '.';
    }
}
