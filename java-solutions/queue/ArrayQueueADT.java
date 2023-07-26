package queue;

import java.util.Objects;

/*
    Model: a[0]...a[n - 1]
    Invariant: for i=0..(n - 1): a[i] != null

    Let immutable(n): for i=0..(n - 1): a'[i] == a[i]
    Let shiftQueueBackward(): for i=0..(n - 2): a'[i] == a[i + 1]
    Let shiftQueueForward(): for i=1..(n - 1): a'[i] == a[i - 1]
 */
public class ArrayQueueADT {
    private Object[] elements = new Object[8];
    private int head;
    private int size;

    /*
        Pred: element != null && queueADT != null
        Post: n' == n + 1 && shiftQueueForward() && a[0] == element
    */
    public static void push(final ArrayQueueADT queueADT, final Object element) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(queueADT);
        ensureCapacity(queueADT);
        queueADT.head = getIndex(queueADT, -1);
        queueADT.elements[queueADT.head] = element;
        queueADT.size++;
    }

    private static int getIndex(final ArrayQueueADT queueADT, int pos) {
        return (queueADT.head + pos + queueADT.elements.length) % queueADT.elements.length;
    }

    /*
        Pred: n > 0 && queueADT != null
        Post: R == a[n - 1] && immutable(n) && n' == n
    */
    public static Object peek(final ArrayQueueADT queueADT) {
        assert queueADT.size != 0;
        Objects.requireNonNull(queueADT);
        return queueADT.elements[getIndex(queueADT, queueADT.size - 1)];
    }

    /*
        Pred: n > 0 && queueADT != null
        Post: R == a[n - 1] && n' == n - 1 && immutable(n')
    */
    public static Object remove(final ArrayQueueADT queueADT) {
        assert queueADT.size != 0;
        Objects.requireNonNull(queueADT);
        int tail = getIndex(queueADT, queueADT.size - 1);
        final Object result = queueADT.elements[tail];
        queueADT.elements[tail] = null;
        queueADT.size--;
        return result;
    }

    /*
        Pred: true && queueADT != null
        Post: R == a[0]..a[n - 1] && immutable(n) && n' = n
    */
    public static Object[] toArray(final ArrayQueueADT queueADT) {
        Objects.requireNonNull(queueADT);
        return toArray(queueADT, queueADT.size);
    }

    private static Object[] toArray(final ArrayQueueADT q, int size) {
        final Object[] result = new Object[size];
        System.arraycopy(q.elements, q.head, result, 0, Math.min(q.head + size, q.elements.length) - q.head);
        System.arraycopy(q.elements, 0, result, Math.min(q.head + size, q.elements.length) - q.head,
            (q.head + size > q.elements.length ? (q.head + size) % q.elements.length : 0));
        return result;
    }

    /*
        Pred: element != null && queueADT != null
        Post: n' == n + 1 && a[n' - 1] == element && immutable(n)
    */
    public static void enqueue(final ArrayQueueADT queueADT, final Object element) {
        Objects.requireNonNull(element);
        Objects.requireNonNull(queueADT);
        ensureCapacity(queueADT);;
        queueADT.elements[getIndex(queueADT, queueADT.size)] = element;
        queueADT.size++;
    }

    private static void ensureCapacity(final ArrayQueueADT queueADT) {
        Objects.requireNonNull(queueADT);
        if (queueADT.size == queueADT.elements.length) {
            increaseCapacity(queueADT);
        }
    }

    private static void increaseCapacity(final ArrayQueueADT queueADT) {
        Objects.requireNonNull(queueADT);
        final Object[] enlargedElements = toArray(queueADT, queueADT.size * 2);
        queueADT.head = 0;
        queueADT.elements = enlargedElements;
    }

    /*
        Pred: n > 0 && queueADT != null
        Post: R == a[0] && immutable(n) && n' == n
    */
    public static Object element(final ArrayQueueADT queueADT) {
        Objects.requireNonNull(queueADT);
        assert queueADT.size != 0;
        return queueADT.elements[queueADT.head];
    }

    /*
        Pred: n > 0 && queueADT != null
        Post: R == a[0] && shiftQueueBackward() && n' == n - 1
    */
    public static Object dequeue(final ArrayQueueADT queueADT) {
        Objects.requireNonNull(queueADT);
        assert queueADT.size != 0;
        final Object result = queueADT.elements[queueADT.head];
        queueADT.elements[queueADT.head] = null;
        queueADT.head = getIndex(queueADT, 1);
        queueADT.size--;
        return result;
    }

    /*
        Pred: queueADT != null
        Post: R == n && immutable(n)
    */
    public static int size(final ArrayQueueADT queueADT) {
        Objects.requireNonNull(queueADT);
        return queueADT.size;
    }

    /*
        Pred: queueADT != null
        Post: R == (n > 0) && immutable(n)
    */
    public static boolean isEmpty(final ArrayQueueADT queueADT) {
        Objects.requireNonNull(queueADT);
        return queueADT.size == 0;
    }

    /*
        Pred: queueADT != null
        Post: n' == 0
     */
    public static void clear(final ArrayQueueADT queueADT) {
        Objects.requireNonNull(queueADT);
        queueADT.elements = new Object[8];
        queueADT.head = 0;
        queueADT.size = 0;
    }
}
