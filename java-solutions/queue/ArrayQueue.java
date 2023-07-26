package queue;

import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private Object[] elements = new Object[8];
    private int head;

    /*
       Pred: element != null
       Post: n' == n + 1 && shiftQueueForward() && a[0] == element
   */
    public void push(Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        head = getIndex(-1);
        elements[head] = element;
        size++;
    }

    /*
        Pred: n > 0
        Post: R == a[n - 1] && immutable(n) && n' == n
    */
    public Object peek() {
        assert size != 0;
        return elements[getIndex(size - 1)];
    }

    private int getIndex(int pos) {
        return (head + pos + elements.length) % elements.length;
    }

    /*
        Pred: n > 0
        Post: R == a[n - 1] && n' == n - 1
    */
    public Object remove() {
        assert size != 0;
        int tail = getIndex(size - 1);
        final Object result = elements[tail];
        elements[tail] = null;
        size--;
        return result;
    }

    /*
        Pred: true
        Post: R == a[0]..a[n - 1] && immutable(n) && n' = n
    */
    public Object[] toArray() {
        return toArray(size);
    }

    private Object[] toArray(int size) {
        final Object[] result = new Object[size];
        System.arraycopy(elements, head, result, 0, Math.min(head + size, elements.length) - head);
        System.arraycopy(elements, 0, result, Math.min(head + size, elements.length) - head,
            (head + size > elements.length ? (head + size) % elements.length : 0));
        return result;
    }

    @Override
    protected void enqueueImpl(final Object element) {
        ensureCapacity();
        elements[getIndex(size)] = element;
    }

    private void ensureCapacity() {
        if (size == elements.length) {
            increaseCapacity();
        }
    }

    private void increaseCapacity() {
        final Object[] enlargedElements = toArray(2 * size);
        head = 0;
        elements = enlargedElements;
    }

    @Override
    protected Object elementImpl() {
        return elements[head];
    }

    @Override
    protected Object dequeueImpl() {
        final Object result = elements[head];
        elements[head] = null;
        head = getIndex(1);
        return result;
    }

    @Override
    protected void clearImpl() {
        elements = new Object[8];
        head = 0;
    }

    @Override
    protected boolean containsImpl(final Object element) {
        for (int i = 0; i < size; i++) {
            if (elements[getIndex(i)].equals(element)) {
                return true;
            }
        }
        return false;
    }
}
