package expression.generic;

import expression.exceptions.DBZException;

public class UncheckedShortType implements CalculationType<Short>{
    @Override
    public Short add(final Short first, final Short second) {
        return (short) (first + second);
    }

    @Override
    public Short subtract(final Short first, final Short second) {
        return (short) (first - second);
    }

    @Override
    public Short multiply(final Short first, final Short second) {
        return (short) (first * second);
    }

    @Override
    public Short divide(final Short first, final Short second) {
        if (second == 0) {
            throw new DBZException();
        }
        return (short) (first / second);
    }

    @Override
    public Short abs(final Short number) {
        return (short) Math.abs(number);
    }

    @Override
    public Short square(final Short number) {
        return (short) (number * number);
    }

    @Override
    public Short mod(final Short first, final Short second) {
        return (short) (first % second);
    }

    @Override
    public Short parseTypeT(final String x) {
        return Short.parseShort(x);
    }

    @Override
    public Short parseTypeT(final int x) {
        return (short) x;
    }

    @Override
    public boolean isPartOfNumber(final char ch) {
        return Character.isDigit(ch);
    }
}
