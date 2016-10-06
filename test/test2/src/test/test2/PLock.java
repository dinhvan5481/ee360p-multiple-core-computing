package test.test2;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 9/29/2016.
 */
public class PLock {
    private ReentrantLock mutex = new ReentrantLock();
    private Condition hasHigherPriorityCond = mutex.newCondition();
    private Condition waitingCond = mutex.newCondition();
    private int highPriorityCounter = 0;
    private ConcurrentHashMap<Long, Boolean> highPriorityMap = new ConcurrentHashMap<>();

    public void requestCS(int priority) {
        mutex.lock();
        try {
            highPriorityCounter++;
            if(priority == 1) {
                highPriorityCounter++;
                highPriorityMap.put(Thread.currentThread().getId(), true);
                if(highPriorityCounter > 1) {
                    waitingCond.await();
                }
            } else if(priority == 0) {
                while(highPriorityCounter > 0) {
                    hasHigherPriorityCond.await();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.unlock();
        }
    }

    public void releaseCS() {
        mutex.lock();
        try {
            if(highPriorityMap.containsKey(Thread.currentThread().getId())) {
                highPriorityMap.remove(Thread.currentThread().getId());
                highPriorityCounter--;
            }
            if(highPriorityCounter > 0) {
                waitingCond.signalAll();
            } else {
                hasHigherPriorityCond.signalAll();
            }
        } finally {
            mutex.unlock();
        }
    }
}
