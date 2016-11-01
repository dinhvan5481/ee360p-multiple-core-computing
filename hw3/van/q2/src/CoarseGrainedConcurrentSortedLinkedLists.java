import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 10/24/2016.
 */
public class CoarseGrainedConcurrentSortedLinkedLists extends BaseConcurrentSortedLinkedList {
    private ReentrantLock mutex;

    public CoarseGrainedConcurrentSortedLinkedLists() {
        super();
        mutex = new ReentrantLock();
    }

    public boolean add(int x) {
        mutex.lock();
        try {
            Node newNode = new Node(x);
            Node curr = this.head;

            while (curr.getNext() != tail && curr.getNext().getData() < x) {
                curr = curr.getNext();
            }

            if(curr.getNext() == tail || curr.getNext().getData() != x) {
                newNode.setNext(curr.getNext());
                curr.setNext(newNode);
                return true;
            }

            return false;
        } finally {
            mutex.unlock();
        }
    }

    public boolean remove(int x) {
        mutex.lock();
        try {
            Node curr = this.head;

            while (curr.getNext() != tail && curr.getNext().getData() < x) {
                curr = curr.getNext();
            }
            if(curr.getNext() == tail) {
                return false;
            }
            if(curr.getNext().getData() == x) {
                curr.setNext(curr.getNext().getNext());
                return true;
            }
            return false;
        } finally {
            mutex.unlock();
        }
    }

    public boolean contains(int x) {
        mutex.lock();
        try {
            Node curr = head.getNext();
            while (curr != tail && curr.getData() < x) {
                    curr = curr.getNext();
            }
            if(curr.getData() == x) {
                return true;
            } else {
                return false;
            }

        } finally {
            mutex.unlock();
        }
    }

}
