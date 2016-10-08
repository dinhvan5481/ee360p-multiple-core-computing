package test.test2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 9/30/2016.
 */
public class Bridge {
    private int maxCar;
    private int numCarOnBridge;
    private int currentDirection;
    private ReentrantLock mutex = new ReentrantLock();
    private Condition waitForRoadReady = mutex.newCondition();
    private Condition waitForDirectionClearCond = mutex.newCondition();

    public Bridge(int maxCar) {
        this.maxCar = maxCar;
        numCarOnBridge = 0;
        currentDirection = -1;
    }

    public synchronized void arriveBridge(int direction) throws InterruptedException {
        while (isSafeToCross(direction)) {
            waitForDirectionClearCond.await();
        }
        currentDirection = direction;
        while (numCarOnBridge >= maxCar) {
            waitForRoadReady.await();
        }
        numCarOnBridge++;
    }

    public synchronized void exitBridge() {
        numCarOnBridge--;
        waitForRoadReady.signalAll();
        if(numCarOnBridge == 0) {
            currentDirection = -1;
            waitForDirectionClearCond.signalAll();
        }

    }

    private boolean isSafeToCross(int direction) {
        if((numCarOnBridge < maxCar && currentDirection == direction) || numCarOnBridge == 0) {
            return true;
        } else {
            return false;
        }
    }

}
