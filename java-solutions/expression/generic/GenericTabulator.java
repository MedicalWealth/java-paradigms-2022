package expression.generic;

public class GenericTabulator implements Tabulator {
    @Override
    public Object[][][] tabulate(String mode, String expression, int x1, int x2, int y1, int y2, int z1, int z2)
        throws Exception {
        return generateTable(getType(mode), expression, x1, x2, y1, y2, z1, z2);
    }

    private <T extends Number> Object[][][] generateTable(CalculationType<T> type, String expression,
                                            int x1, int x2, int y1, int y2, int z1, int z2) throws Exception {
        final Object[][][] result = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        Operand<T> parsedExpression = new ExpressionParser<T>().parse(expression, type);
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                for (int k = z1; k <= z2; k++) {
                    evaluateCell(type, x1, y1, z1, result, parsedExpression, i, j, k);
                }
            }
        }
        return result;
    }

    private static <T extends Number> void evaluateCell(CalculationType<T> type, int x1, int y1, int z1, Object[][][] result, Operand<T> parsedExpression, int i, int j, int k) {
        try {
            result[i - x1][j - y1][k - z1] = parsedExpression.evaluate(type.parseTypeT(i),
                type.parseTypeT(j), type.parseTypeT(k), type);
        } catch (Exception e) {
            result[i - x1][j - y1][k - z1] = null;
        }
    }

    private static CalculationType<? extends Number> getType(String mode) {
        return switch (mode) {
            case "i" -> new CheckedIntegerType();
            case "d" -> new DoubleType();
            case "bi" -> new BigIntegerType();
            case "u" -> new UncheckedIntegerType();
            case "s" -> new UncheckedShortType();
            case "l" -> new UncheckedLongType();
            default -> throw new IllegalArgumentException(mode + "is not supported");
        };
    }
}
