package expression.generic;

import expression.exceptions.DBZException;

import java.math.BigInteger;

public final class BigIntegerType implements CalculationType<BigInteger> {
    @Override
    public BigInteger add(final BigInteger first, final BigInteger second) {
        return first.add(second);
    }

    @Override
    public BigInteger subtract(final BigInteger first, final BigInteger second) {
        return first.subtract(second);
    }

    @Override
    public BigInteger multiply(final BigInteger first, final BigInteger second) {
        return first.multiply(second);
    }

    @Override
    public BigInteger divide(final BigInteger first, final BigInteger second) {
        if (second.equals(BigInteger.ZERO)) {
            throw new DBZException();
        }
        return first.divide(second);
    }

    @Override
    public BigInteger abs(BigInteger number) {
        return number.abs();
    }

    @Override
    public BigInteger square(BigInteger number) {
        return number.multiply(number);
    }

    @Override
    public BigInteger mod(BigInteger first, BigInteger second) {
        return first.mod(second);
    }

    @Override
    public BigInteger parseTypeT(final String x) {
        return new BigInteger(x);
    }

    @Override
    public BigInteger parseTypeT(int x) {
        return BigInteger.valueOf(x);
    }

    @Override
    public boolean isPartOfNumber(char ch) {
        return Character.isDigit(ch);
    }
}
