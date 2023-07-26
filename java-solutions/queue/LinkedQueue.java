package queue;

import java.util.Objects;

public class LinkedQueue extends AbstractQueue {
    private Node head;
    private Node tail;

    public LinkedQueue() {
        clearImpl();
    }

    protected void enqueueImpl(final Object element) {
        tail.element = element;
        Node prevTail = tail;
        tail = new Node(null, null);
        prevTail.next = tail;
    }

    @Override
    protected Object elementImpl() {
        return head.next.element;
    }

    @Override
    protected Object dequeueImpl() {
        final Object result = head.next.element;
        head = head.next;
        head.element = null;
        return result;
    }

    @Override
    protected void clearImpl() {
        tail = new Node(null, null);
        head = new Node(null, tail);
    }

    @Override
    protected boolean containsImpl(final Object element) {
        Node current = head.next;
        while (current != tail) {
            if (Objects.equals(current.element, element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    private static class Node {
        private Object element;
        private Node next;

        private Node(Object element, Node next) {
            this.element = element;
            this.next = next;
        }
    }
}
