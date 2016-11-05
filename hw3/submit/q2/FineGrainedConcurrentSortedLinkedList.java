/**
 * Created by quachv on 11/1/2016.
 */
public class FineGrainedConcurrentSortedLinkedList extends BaseConcurrentSortedLinkedList {

    public FineGrainedConcurrentSortedLinkedList() {
    }
    @Override
    public boolean add(int x) {
        Node newNode = new Node(x);
        Node prev = this.head;
        while (true) {
            synchronized (prev) {
                while (prev.getNext() != tail && prev.getNext().getData() < x) {
                    prev = prev.getNext();
                }
                if (prev.getNext() == tail || prev.getNext().getData() != x) {
                    Node succ = prev.getNext();
                    synchronized (succ) {
                        if (succ.getIsDeleted() || prev.getIsDeleted() || prev.getNext() != succ) {
                            prev = this.head;
                            continue;
                        }
                        newNode.setNext(succ);
                        prev.setNext(newNode);
                        return true;
                    }
                }
                return false;
            }
        }
    }

    @Override
    public boolean remove(int x) {
        Node prev = this.head;
        while (true) {
            synchronized (prev) {
                while (prev.getNext() != tail && prev.getNext().getData() < x) {
                    prev = prev.getNext();
                }
                if(prev.getNext() == tail) {
                    return false;
                }
                if(prev.getNext().getData() == x) {
                    Node curr = prev.getNext();
                    synchronized (curr) {
                        if(prev.getIsDeleted() || curr.getIsDeleted() || prev.getNext() != curr) {
                            prev = this.head;
                            continue;
                        }
                        curr.setDeleted(true);
                        prev.setNext(curr.getNext());
                        return true;
                    }
                }
                return false;
            }
        }
    }

    @Override
    public boolean contains(int x) {
        Node curr = this.head;
        synchronized (curr) {
            while (curr != tail && curr.getData() < x) {
                curr = curr.getNext();
            }
            if (curr == this.tail) {
                return false;
            }
            if (curr.getData() == x && !curr.getIsDeleted()) {
                return true;
            }
        }
        return false;
    }
}
