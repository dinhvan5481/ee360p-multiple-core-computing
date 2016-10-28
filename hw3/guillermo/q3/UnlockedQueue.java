import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;


// Lock-free Linked list
public class UnlockedQueue {

    static class Node {
        int value;
        AtomicReference<Node> next;

        public Node(int v) {
            value = v;
            next = new AtomicReference<Node>();
        }
    }

    AtomicReference<Node> head;
    AtomicReference<Node> tail;
    AtomicInteger count;

    public UnlockedQueue() {
        Node node = new Node(Integer.MIN_VALUE);
        head = new AtomicReference<Node>(node);
        tail = new AtomicReference<Node>(node);
        count = new AtomicInteger(0);
    }

    public int count() {
        return count.get();
    }

    public void enq(int x) {
        Node node = new Node(x);
        Node qtail;
        for (;;) { // Try until is done.
            qtail = tail.get();
            Node next = qtail.next.get();
            if (qtail == tail.get()) {
                if(next == null) {
                    if(qtail.next.compareAndSet(next, node)) {
                        break;
                    }
                } else {
                    tail.compareAndSet(qtail, next);
                }
            }
        }
        tail.compareAndSet(qtail, node);
        count.incrementAndGet();
    }

    // False if queue is empty.
    public boolean deq() {
        for (;;) {
            Node headPointer = head.get();
            Node tailPointer = tail.get();
            Node next = headPointer.next.get();

            if(headPointer == head.get()) {
                if(headPointer == tailPointer) {
                    if(next == null) {
                        return false;
                    }
                    tail.compareAndSet(tailPointer, next);
                } else {
                    int value = next.value;
                    if(head.compareAndSet(headPointer, next)){
                        break;
                    }
                }
            }
        }
        count.decrementAndGet();
        return true;
    }
}