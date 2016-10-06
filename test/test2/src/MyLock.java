
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 9/30/2016.
 */
public class MyLock {
    private ReentrantLock mutex;
    private Condition roomReady = mutex.newCondition();

    private enum ThreadType {
        Thread1,
        Thread2,
        None
    };

    private ThreadType occupyingThreadType;
    private int threadCounter;


    public MyLock() {
        mutex = new ReentrantLock();
        threadCounter = 0;
        occupyingThreadType = ThreadType.None;
    }

    public synchronized void request1() throws InterruptedException {
        while (!isSafeToGetIn(ThreadType.Thread1)) {
            roomReady.await();
        }
        occupyingThreadType = ThreadType.Thread1;
        threadCounter++;
    }

    public synchronized void request2() throws InterruptedException {
        while (!isSafeToGetIn(ThreadType.Thread2)) {
            roomReady.await();
        }
        occupyingThreadType = ThreadType.Thread2;
        threadCounter++;
    }

    public synchronized void release() {
        threadCounter--;
        if(threadCounter == 0) {
            occupyingThreadType = ThreadType.None;
            roomReady.signalAll();
        }
    }

    private boolean isSafeToGetIn(ThreadType threadType) {
        return !(threadCounter > 0 && threadType != occupyingThreadType);
    }
}
