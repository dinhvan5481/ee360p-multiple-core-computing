package test.test2;


import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 9/30/2016.
 */
public class MyLock {
    private ReentrantLock mutex;
    private Condition hasType1Cond = mutex.newCondition();
    private Condition hasType2Cond = mutex.newCondition();

    private int type1Counter;
    private int type2Counter;
    private HashMap<Long, Integer> threadType;

    public MyLock() {
        mutex = new ReentrantLock();
        type1Counter = 0;
        type2Counter = 0;
        threadType = new HashMap<>();
    }

    public synchronized void request1() throws InterruptedException {
        while (type2Counter > 0) {
            hasType2Cond.await();
        }
        type1Counter++;
        threadType.put(Thread.currentThread().getId(), 1);
    }

    public synchronized void request2() throws InterruptedException {
        while (type1Counter > 0) {
            hasType1Cond.await();
        }
        type2Counter++;
        threadType.put(Thread.currentThread().getId(), 2);
    }

    public synchronized void release() {
        int iThreadType;
        if(threadType.containsKey(Thread.currentThread().getId())) {
            iThreadType = threadType.get(Thread.currentThread().getId());
            if(iThreadType == 1) {
                type1Counter--;
                if(type1Counter == 0) {
                    hasType1Cond.signalAll();
                }
            } else if(iThreadType == 2) {
                type2Counter--;
                if(type2Counter == 0) {
                    hasType2Cond.signalAll();
                }
            }
        }
    }
}
