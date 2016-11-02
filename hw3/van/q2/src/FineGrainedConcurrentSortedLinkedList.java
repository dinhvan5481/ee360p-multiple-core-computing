/**
 * Created by quachv on 11/1/2016.
 */
public class FineGrainedConcurrentSortedLinkedList extends BaseConcurrentSortedLinkedList {

    public FineGrainedConcurrentSortedLinkedList() {
    }
    @Override
    public boolean add(int x) {
        return false;
    }

    @Override
    public boolean remove(int x) {
        return false;
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
