/**
 * Created by andy on 9/25/16.
 */

import java.util.concurrent.Semaphore;

public class CyclicBarrier {
    int parties = 0;
    Semaphore mulex = new Semaphore(1);
    Semaphore permits;
    Semaphore lock = new Semaphore (0);

    public CyclicBarrier(int parties) {
        // TODO: The constructor for this CyclicBarrier
        this.parties = parties;
        permits = new Semaphore (0);
    }

    public int await() throws InterruptedException {
        int arrivalIndex = parties;
        if (parties > 0){
            mulex.acquire(); //for mutual exclusion while changing the index
            parties--;
            arrivalIndex = parties;
            mulex.release();

            if (parties==0) {
                lock.release(); //release lock
            } else {
                lock.acquire(); //wait before the last one has gone through
                lock.release(); //release lock for the next thread
            }
        }
        return arrivalIndex;
    }
}