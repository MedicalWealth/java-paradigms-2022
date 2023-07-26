package expression.generic;

import expression.exceptions.*;
import expression.parser.BaseParser;
import expression.parser.CharSource;
import expression.parser.StringCharSource;

import java.util.List;

public final class ExpressionParser<T extends Number>  {
    public ExpressionParser() {
    }

    public Operand<T> parse(final String expression, final CalculationType<T> type) throws ParseException {
        return new Parser<>(new StringCharSource(expression), type).parseExpression();
    }

    private static final class Parser<T extends Number> extends BaseParser {
        private final CalculationType<T> type;
        public Parser(final CharSource source, final CalculationType<T> type) {
            super(source);
            this.type = type;
        }

        private Operand<T> parseExpression() throws ParseException {
            final Operand<T> result = parseOperation(0);
            if (eof()) {
                return result;
            }
            throw new EOFException(errorChar(), source.getPos());
        }

        private final List<List<String>> operations = List.of(
            List.of("mod"),
            List.of("+", "-"),
            List.of("*", "/")
        );

        private Operand<T> parseOperation(final int priority) throws ParseException {
            Operand<T> leftOperand = parseNext(priority + 1);
            while (true) {
                skipWhitespace();
                final List<String> currentOperations = operations.get(priority);
                boolean isValidOperation = false;
                for (String operation: currentOperations) {
                    if (take(operation)) {
                        leftOperand = parseOperation(priority, leftOperand, operation);
                        isValidOperation = true;
                    }
                }
                if (!isValidOperation) {
                    return leftOperand;
                }
            }
        }

        private Operand<T> parseOperation(final int priority, Operand<T> leftOperand, final String operation) throws ParseException {
            switch (operation) {
                case "+" -> leftOperand = new Add<>(leftOperand, parseNext(priority + 1));
                case "-" -> leftOperand = new Subtract<>(leftOperand, parseNext(priority + 1));
                case "*" -> leftOperand = new Multiply<>(leftOperand, parseNext(priority + 1));
                case "/" -> leftOperand = new Divide<>(leftOperand, parseNext(priority + 1));
                case "mod" -> leftOperand = new Mod<>(leftOperand, parseNext(priority + 1));
            }
            return leftOperand;
        }

        private Operand<T> parseNext(final int priority) throws ParseException {
            if (priority == operations.size()) {
                return parsePrim();
            } else {
                return parseOperation(priority);
            }
        }

        private Operand<T> parsePrim() throws ParseException {
            skipWhitespace();
            if (take('(')) {
                final Operand<T> operand = parseOperation(0);
                if (!take(')')) {
                    throw new NoBracketException(')', errorChar(), source.getPos());
                }
                return operand;
            }  else if (take('-')) {
                if (Character.isDigit(ch)) {
                    return new Const<>(parseConstant(true));
                } else {
                    return new Negate<>(parsePrim());
                }
            } else if (Character.isDigit(ch)) {
                return new Const<>(parseConstant(false));
            } else {
                final String identifier = parseIdentifier();
                return switch (identifier) {
                    case "abs" -> new Abs<>(parsePrim());
                    case "square" -> new Square<>(parsePrim());
                    case "x" -> new Variable<>("x");
                    case "y" -> new Variable<>("y");
                    case "z" -> new Variable<>("z");
                    default ->
                        throw new PrimExpectedException(identifier.isEmpty() ? errorChar() : identifier, source.getPos());
                };
            }
        }

        private T parseConstant(final boolean isNegative) {
            final StringBuilder sb = new StringBuilder(isNegative ? "-" : "");
            while (type.isPartOfNumber(ch)) {
                sb.append(take());
            }
            return type.parseTypeT(sb.toString());
        }

        private void skipWhitespace() {
            while (Character.isWhitespace(ch) && !eof()) {
                take();
            }
        }

        private String parseIdentifier() {
            skipWhitespace();
            final StringBuilder result = new StringBuilder();
            while (Character.isJavaIdentifierPart(ch) && !eof()) {
                result.append(take());
            }
            return result.toString();
        }
    }
}