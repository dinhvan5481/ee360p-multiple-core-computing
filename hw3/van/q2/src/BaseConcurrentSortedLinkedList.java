/**
 * Created by dinhvan5481 on 10/28/16.
 */
public abstract class BaseConcurrentSortedLinkedList implements SortedLinkedList {
    protected Node head;
    protected Node tail;

    public BaseConcurrentSortedLinkedList(){
        head = new Node();
        tail = new Node();
        head.setNext(tail);
    }

    public abstract boolean add(int x);
    public abstract boolean remove(int x);
    public abstract boolean contains(int x);

    public synchronized String toString() {
        StringBuilder results = new StringBuilder("[");
        Node curr = this.head.getNext();
        while (curr != tail) {
            results.append(String.format("%d : ", curr.getData()));
            curr = curr.getNext();
        }
        if(results.length() > 1) {
            results.setLength(results.length() - 3);
        }
        results.append("]");
        return results.toString();
    }

    public synchronized boolean isInASCOrder() {
        Node curr = this.head.getNext();
        if(curr == tail) {
            return true;
        }
        while (curr.getNext() != tail) {
            if(curr.getData() >= curr.getNext().getData()) {
                return false;
            }
            curr = curr.getNext();
        }
        return true;
    }
}
