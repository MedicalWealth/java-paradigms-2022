package queue;

public class MyArrayQueueModuleTest {
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            ArrayQueueModule.enqueue("q_elem_" + i);
            ArrayQueueModule.push("q_elem_" + i);
        }
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.dequeue());
        }
    }
}
