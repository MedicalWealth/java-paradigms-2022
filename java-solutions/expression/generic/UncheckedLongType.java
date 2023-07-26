package expression.generic;

import expression.exceptions.DBZException;

public class UncheckedLongType implements CalculationType<Long> {
    @Override
    public Long add(final Long first, final Long second) {
        return first + second;
    }

    @Override
    public Long subtract(final Long first, final Long second) {
        return first - second;
    }

    @Override
    public Long multiply(final Long first, final Long second) {
        return first * second;
    }

    @Override
    public Long divide(final Long first, final Long second) {
        if (second == 0) {
            throw new DBZException();
        }
        return first / second;
    }

    @Override
    public Long abs(final Long number) {
        return Math.abs(number);
    }

    @Override
    public Long square(final Long number) {
        return number * number;
    }

    @Override
    public Long mod(final Long first, final Long second) {
        return first % second;
    }

    @Override
    public Long parseTypeT(final String x) {
        return Long.parseLong(x);
    }

    @Override
    public Long parseTypeT(final int x) {
        return (long) x;
    }

    @Override
    public boolean isPartOfNumber(final char ch) {
        return Character.isDigit(ch);
    }
}
