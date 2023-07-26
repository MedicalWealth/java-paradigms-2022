package queue;

public class MyArrayQueueTest {
    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        generateQueue(queue1, 25, 1);
        generateQueue(queue2, 7, 2);
        dumpQueue(queue1);
        dumpQueue(queue2);
        System.out.println("------");
        queue1.clear();
        dumpQueue(queue1);
    }

    private static void generateQueue(ArrayQueue queue, int size, int id) {
        for (int i = 0; i < size; i++) {
            queue.enqueue("q" + id + "_elem_" + i);
            queue.push("q" + id + "_elem_" + i);
        }
    }

    private static void dumpQueue(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
    }
}
