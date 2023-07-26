package expression.generic;

import expression.exceptions.DBZException;
import expression.exceptions.OverflowException;

public final class CheckedIntegerType implements CalculationType<Integer> {
    @Override
    public Integer add(final Integer first, final Integer second) {
        final int result = first + second;
        if (first < 0 && second < 0 && result >= 0 || first > 0 && second > 0 && result <= 0) {
            throw new OverflowException("add", first, second);
        }
        return result;
    }

    @Override
    public Integer subtract(final Integer first, final Integer second) {
        final int result = first - second;
        if (first < 0 && second > 0 && result >= 0 || first >= 0 && second < 0 && result <= 0) {
            throw new OverflowException("subtract", first, second);
        }
        return first - second;
    }

    @Override
    public Integer multiply(final Integer first, final Integer second) {
        final int result = first * second;
        if (second != 0 && result / second != first || first != 0 && result / first != second) {
            throw new OverflowException("multiply", first, second);
        }
        return first * second;
    }

    @Override
    public Integer divide(final Integer first, final Integer second) {
        if (second == 0) {
            throw new DBZException();
        } else if (first == Integer.MIN_VALUE && second == -1) {
            throw new OverflowException("divide", first, second);
        }
        return first / second;
    }

    @Override
    public Integer abs(final Integer number) {
        if (number == Integer.MIN_VALUE) {
            throw new OverflowException("abs", number);
        }
        return Math.abs(number);
    }

    @Override
    public Integer square(final Integer number) {
        if ((long) number * number != number * number) {
            throw new OverflowException("square", number);
        }
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
