import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 11/2/2016.
 */
public class LockBasedConcurrentQueue extends BaseConcurrentQueue {
    private AtomicInteger count;
    private ReentrantLock headLock;
    private ReentrantLock tailLock;
    private Condition emptyQueueCond;

    public LockBasedConcurrentQueue() {
        this.count = new AtomicInteger(0);

        this.headLock = new ReentrantLock();
        this.tailLock = new ReentrantLock();
        this.emptyQueueCond = headLock.newCondition();
    }

    public boolean enq(int value) {
        headLock.lock();
        try {
            Node newNode = new Node(value);
            newNode.setNext(this.head.getNext());
            this.head.setNext(newNode);
            this.count.incrementAndGet();
            this.emptyQueueCond.signalAll();
        } finally {
            headLock.unlock();
            return true;
        }
    }

    @Override
    public Integer deq() {
        tailLock.lock();
        try {
            while (this.count.get() <= 0) {
                emptyQueueCond.await();
            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            tailLock.unlock();
        }

        return null;
    }
}
