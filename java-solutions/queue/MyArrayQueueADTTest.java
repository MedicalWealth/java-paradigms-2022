package queue;

public class MyArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT queueADT1 = new ArrayQueueADT();
        ArrayQueueADT queueADT2 = new ArrayQueueADT();
        generateQueue(queueADT1, 25, 1);
        generateQueue(queueADT2, 7, 2);
        dumpQueue(queueADT1);
        dumpQueue(queueADT2);
        System.out.println("------");
        ArrayQueueADT.clear(queueADT1);
        dumpQueue(queueADT1);
    }

    private static void generateQueue(ArrayQueueADT queueADT, int size, int id) {
        for (int i = 0; i < size; i++) {
            ArrayQueueADT.enqueue(queueADT, "q" + id + "_elem_" + i);
            ArrayQueueADT.push(queueADT, "q" + id + "_elem_" + i);
        }
    }

    private static void dumpQueue(ArrayQueueADT queueADT) {
        while (!ArrayQueueADT.isEmpty(queueADT)) {
            System.out.println(ArrayQueueADT.dequeue(queueADT));
        }
    }
}
