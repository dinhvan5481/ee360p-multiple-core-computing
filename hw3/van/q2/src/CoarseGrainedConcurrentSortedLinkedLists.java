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
            Node curr = this.head.getNext();
            if(curr == tail) {
                //TODO: trying use single linked list, if switch to double linked list, need to modified this
                newNode.setNext(this.tail);
                this.head.setNext(newNode);
                return true;
            }

            if(curr.getData() == x) {
                return false;
            }

            while (curr.getNext() != tail && curr.getNext().getData() < x) {
                curr = curr.getNext();
            }

            if(curr.getNext() == tail || curr.getNext().getData() != x) {
                curr.setNext(newNode);
                newNode.setNext(curr.getNext());
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
            Node curr = this.head.getNext();
            if(curr == tail) {
                return false;
            }

            if(curr.getData() == x) {
                this.head.setNext(curr.getNext());
            }

            while (curr.getNext() != tail && curr.getNext().getData() < x) {
                curr = curr.getNext();
            }

            if(curr.getNext() == tail) {
                return false;
            }

            if(curr.getNext().getData() == x) {
                Node temp = curr.getNext();
                curr.setNext(curr.getNext().getNext());
                temp = null;
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
