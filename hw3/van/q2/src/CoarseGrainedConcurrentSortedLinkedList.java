import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 10/24/2016.
 */
public class CoarseGrainedConcurrentSortedLinkedList implements IConcurrentSortedLinkedList {
    private Node head;
    private Node tail;
    private ReentrantLock mutex;

    public CoarseGrainedConcurrentSortedLinkedList(){
        mutex = new ReentrantLock();
        head = new Node();
        tail = new Node();
        head.setNext(tail);
        tail.setPrev(head);
    }

    public boolean add(int x) {
        boolean result = false;
        if(!this.contains(x)) {
            return result;
        }
        mutex.lock();
        try {

        } finally {
            mutex.unlock();
            return false;
        }
    }

    public boolean remove(int x) {
        return false;
    }

    public boolean contains(int x) {
        boolean result = false;
        mutex.lock();
        try {
            Node curr = head.getNext();
            while (curr != tail && curr.getData() <= x) {
                if(curr.getData() == x) {
                    result = true;
                } else {
                    curr = curr.getNext();
                }
            }
        } finally {
            mutex.unlock();
            return result;
        }
    }

}
