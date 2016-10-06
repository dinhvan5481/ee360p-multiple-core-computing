/**
 * Created by andy on 9/25/16.
 */

import java.util.concurrent.Semaphore;

public class CyclicBarrier {
    int parties = 0;
    int originalParties = 0;
    int numRelease = 0;
    Semaphore mulex = new Semaphore(1);
    Semaphore mulex2 = new Semaphore(1);
    Semaphore lock = new Semaphore (0);
    Semaphore extra;

    public CyclicBarrier(int parties) {
        this.parties = parties;
        this.numRelease = parties;
        this.originalParties = parties;
        this.extra = new Semaphore (parties);
    }

    public int await() throws InterruptedException {
        extra.acquire();
        int arrivalIndex = parties;
        if (parties > 0){
            mulex.acquire(); //for mutual exclusion while changing the index
            parties--;
            arrivalIndex = parties;
            mulex.release();

            if (parties==0) {
                lock.release(); //release lock
                mulex2.acquire();
                numRelease--;
                mulex2.release();
            } else {
                lock.acquire(); //wait before the last one has gone through
                mulex2.acquire();
                if (numRelease >0) {
                    lock.release(); //release lock for the next thread
                    numRelease--;
                }
                mulex2.release();
            }
            
            if (numRelease == 0) {
                parties = originalParties;
                numRelease = originalParties;
                extra.release(parties);
            }
        }
        return arrivalIndex;
    }
}
