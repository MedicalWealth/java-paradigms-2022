package expression.parser;

public interface CharSource {
    boolean hasNext();
    char next();
    int getPos();
    void setPos(int pos);
    IllegalArgumentException error(String message);
}
