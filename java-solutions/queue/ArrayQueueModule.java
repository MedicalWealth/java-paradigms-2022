package queue;

import java.util.Objects;

/*
    Model: a[0]...a[n - 1]
    Invariant: for i=0..(n - 1): a[i] != null

    Let immutable(n): for i=0..(n - 1): a'[i] == a[i]
    Let shiftQueueBackward(): for i=0..(n - 2): a'[i] == a[i + 1]
    Let shiftQueueForward(): for i=1..(n - 1): a'[i] == a[i - 1]
 */
public class ArrayQueueModule {
    private static Object[] elements = new Object[8];
    private static int head;
    private static int size;

    /*
       Pred: element != null
       Post: n' == n + 1 && shiftQueueForward() && a[0] == element
   */
    public static void push(final Object element) {
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
    public static Object peek() {
        assert size != 0;
        return elements[getIndex(size - 1)];
    }

    private static int getIndex(int pos) {
        return (head + pos + elements.length) % elements.length;
    }

    /*
        Pred: n > 0
        Post: R == a[n - 1] && n' == n - 1 && immutable(n - 1)
    */
    public static Object remove() {
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
    public static Object[] toArray() {
        return toArray(size);
    }

    private static Object[] toArray(int size) {
        final Object[] result = new Object[size];
        System.arraycopy(elements, head, result, 0, Math.min(head + size, elements.length) - head);
        System.arraycopy(elements, 0, result, Math.min(head + size, elements.length) - head,
            (head + size > elements.length ? (head + size) % elements.length : 0));
        return result;
    }

    /*
        Pred: element != null
        Post: n' == n + 1 && a[n' - 1] == element && immutable(n)
    */
    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        ensureCapacity();
        elements[getIndex(size)] = element;
        size++;
    }

    private static void ensureCapacity() {
        if (size == elements.length) {
            increaseCapacity();
        }
    }

    private static void increaseCapacity() {
        final Object[] enlargedElements = toArray(2 * size);
        head = 0;
        elements = enlargedElements;
    }

    /*
        Pred: n > 0
        Post: R == a[0] && immutable(n) && n' == n
    */
    public static Object element() {
        assert size != 0;
        return elements[head];
    }

    /*
        Pred: n > 0
        Post: R == a[0] && shiftQueueBackward() && n' == n - 1
    */
    public static Object dequeue() {
        assert size != 0;
        final Object result = elements[head];
        elements[head] = null;
        head = getIndex(1);
        size--;
        return result;
    }

    /*
        Pred: true
        Post: R == n && immutable(n)
    */
    public static int size() {
        return size;
    }

    /*
        Pred: true
        Post: R == (n > 0) && immutable(n)
    */
    public static boolean isEmpty() {
        return size == 0;
    }

    /*
        Pred: true
        Post: n' == 0
     */
    public static void clear() {
        elements = new Object[8];
        head = 0;
        size = 0;
    }
}
