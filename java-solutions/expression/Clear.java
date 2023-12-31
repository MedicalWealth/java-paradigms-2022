package expression;

public class Clear extends AbstractBinaryOperation {
    public Clear(Operand first, Operand second) {
        super(first, second);
    }

    @Override
    public int getFirstPriority() {
        return 2;
    }

    @Override
    public boolean isSpecialOperation() {
        return true;
    }

    @Override
    public int calc(final int first, final int second) {
        return first & ~(1 << second);
    }

    @Override
    public String getOperation() {
        return "clear";
    }
}
