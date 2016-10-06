package test.test2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 9/30/2016.
 */
public class Latch {
    private ReentrantLock mutex = new ReentrantLock();
    private Condition readyCond = mutex.newCondition();
    private int countDown;

    public Latch(int countDown){
        this.countDown = countDown;
    }

    public void await() {
        mutex.lock();
        try {
            while (countDown > 0) {
                readyCond.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mutex.unlock();
        }
    }

    public void countDown() {
        mutex.lock();
        try {
            countDown--;
            if(countDown == 0) {
                readyCond.signalAll();
            }
        } finally {
            mutex.unlock();
        }
    }
}
