package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size;

    public void enqueue(final Object element) {
        Objects.requireNonNull(element);
        enqueueImpl(element);
        size++;
    }

    protected abstract void enqueueImpl(final Object element);

    public Object element() {
        assert size != 0;
        return elementImpl();
    }

    protected abstract Object elementImpl();

    public Object dequeue() {
        assert size != 0;
        size--;
        return dequeueImpl();
    }

    protected abstract Object dequeueImpl();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        clearImpl();
        size = 0;
    }

    protected abstract void clearImpl();

    public boolean removeFirstOccurrence(final Object element) {
        return removeKthOccurrence(element, 1);
    }

    protected abstract boolean containsImpl(final Object element);

    public boolean contains(final Object element) {
        return containsImpl(element);
    }

    private boolean removeKthOccurrence(final Object element, final long k) {
        long count = 0;
        final int size = this.size;
        for (int i = 0; i < size; i++) {
            final Object elem = dequeue();
            final boolean isEqual = Objects.equals(elem, element);
            count = isEqual ? count + 1 : count;
            if (count != k || !isEqual) {
                enqueue(elem);
            }
        }
        return count > 0;
    }
}
