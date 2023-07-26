package expression.generic;

import expression.exceptions.DBZException;

public class UncheckedIntegerType implements CalculationType<Integer> {
    @Override
    public Integer add(final Integer first, final Integer second) {
        return first + second;
    }

    @Override
    public Integer subtract(final Integer first, final Integer second) {
        return first - second;
    }

    @Override
    public Integer multiply(final Integer first, final Integer second) {
        return first * second;
    }

    @Override
    public Integer divide(final Integer first, final Integer second) {
        if (second == 0) {
            throw new DBZException();
        }
        return first / second;
    }

    @Override
    public Integer abs(final Integer number) {
        return Math.abs(number);
    }

    @Override
    public Integer square(final Integer number) {
        return number * number;
    }

    @Override
    public Integer mod(final Integer first, final Integer second) {
        return first % second;
    }

    @Override
    public Integer parseTypeT(final String x) {
        return Integer.parseInt(x);
    }

    @Override
    public Integer parseTypeT(final int x) {
        return x;
    }

    @Override
    public boolean isPartOfNumber(final char ch) {
        return Character.isDigit(ch);
    }
}
