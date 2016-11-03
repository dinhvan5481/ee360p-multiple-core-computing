import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by quachv on 11/2/2016.
 */
public abstract class BaseConcurrentQueue implements IConcurrentQueue {
    protected Node head;
    protected Node tail;

    public BaseConcurrentQueue() {
        this.head = new Node();
        this.tail = new Node();
    }

    public abstract boolean enq(int value);
    public abstract Integer deq();

    public String toString() {
        StringBuilder results = new StringBuilder("[");
        Node curr = this.head.getNext();
        while (curr != tail) {
            results.append(String.format("%d : ", curr.getValue()));
            curr = curr.getNext();
        }
        if(results.length() > 1) {
            results.setLength(results.length() - 3);
        }
        results.append("]");
        return results.toString();
    }
}
