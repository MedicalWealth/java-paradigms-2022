package queue;

/*
    Model: a[0]...a[n - 1]
    Invariant: for i=0..(n - 1): a[i] != null

    Let immutable(n): for i=0..(n - 1): a'[i] == a[i]
    Let shiftQueue(): for i=0..(n - 2): a'[i] == a[i + 1]
 */
public interface Queue {

    /*
        Pred: element != null
        Post: n' == n + 1 && a[n' - 1] == element && immutable(n)
    */
    void enqueue(Object element);

    /*
        Pred: n > 0
        Post: R == a[0] && immutable(n) && n' == n
    */
    Object element();

    /*
        Pred: n > 0 && queueADT != null
        Post: R == a[0] && shiftQueueBackward() && n' == n - 1
    */
    Object dequeue();

    /*
        Pred: true
        Post: R == n && immutable(n)
    */
    int size();

    /*
        Pred: true
        Post: R == (n > 0) && immutable(n)
    */
    boolean isEmpty();

    /*
      Pred: true
      Post: n' == 0
   */
    void clear();

    /*
        Pred: true
        Post: R == (exists i: a[i] == element) && immutable(n) && n' == n
     */
    boolean contains(Object element);

    /*
        Pred: true
        Post: R == (for j=0..(i - 1): a[j] != element && a[i] == element) && (immutable(i - 1) &&
                   for j=i..(a.size - 2) a'[j] = a[j + 1] && n' == n - 1)
     */
    boolean removeFirstOccurrence(Object element);
}
