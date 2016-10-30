import java.util.concurrent.atomic.AtomicInteger;


// Lock-based linked list
public class LockQueue implements MyQueue {

    static class Node {
        int value;
        Node next;

        public Node(int v) {
            value = v;
            next = null;
        }
    }

    final Object enqLock = new Object();
    final Object deqLock = new Object();
    final Node root = new Node(Integer.MIN_VALUE);
    AtomicInteger queueLength = new AtomicInteger(0);

    public LockQueue() {
        root.next = root;
    }

    public int count() {
        return queueLength.get();
    }

    public boolean enq(int x) {
        Node enqNode = new Node(x);
        synchronized (enqLock) {
            Node next = root.next;
            root.next = enqNode;
            enqNode.next = next;
        }

        synchronized (deqLock) {
            deqLock.notify();
        }

        queueLength.incrementAndGet();
        return true;
    }

    // Blocks if queue is empty.
    public Integer deq() {
        int dequeued;
        synchronized (deqLock) {
            while(count() == 0) {
                try {
                    deqLock.wait();
                } catch (InterruptedException e) { }
            }

            Node ptr;
            for (ptr = root; ptr.next.next != root; ptr = ptr.next) {
            }

            Node next = ptr.next.next;
            Node toRemove = ptr.next;
            ptr.next = next;
            toRemove.next = null;
            dequeued = toRemove.value;
        }
        queueLength.decrementAndGet();
        return dequeued;
    }
}