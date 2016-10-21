/**
 * Created by andy on 9/25/16.
 */

import java.util.concurrent.Semaphore;

public class CyclicBarrier {
    int parties = 0;
    int originalParties = 0;
    int numRelease = 0; // Keep track of threads that leave await()
    Semaphore mulex = new Semaphore(1); // Mutex for parties
    Semaphore mulex2 = new Semaphore(1); // Mutex for number of released threads.
    Semaphore lock = new Semaphore (0);  // Barrier 
    Semaphore extra;

    public CyclicBarrier(int parties) {
        this.parties = parties;
        this.numRelease = parties;
        this.originalParties = parties;
        this.extra = new Semaphore (parties); // Required to allow only #parties at once.
    }

    // We solve this by doing a 4 step process.
    // 1. Allow only the number of threads specified in parties.
    // 2. All threads but the last one hold execution.
    // 3. Last one releases the rest of the threads waiting, now we count released threads.
    // 4. Last one to be released, resets to initial state and releases 'parties' number of threads
    //    so the process can continue.
    public int await() throws InterruptedException {
        extra.acquire();
        int arrivalIndex = parties;
        if (parties > 0){
            mulex.acquire(); //for mutual exclusion while changing the index
            parties--;
            arrivalIndex = parties; // Guarantees each thread has its own arrival number.
            mulex.release();

            if (parties==0) {
                // Last to arrive is responsible for telling the others waiting.
                lock.release(); //release lock
                mulex2.acquire();
                numRelease--;
                mulex2.release();
            } else {
                // The first (parties - 1) threads will take this way and
                lock.acquire(); // wait before the last one has gone through
                mulex2.acquire();
                if (numRelease >0) {
                    lock.release(); //release lock for the next thread
                    numRelease--;
                }
                mulex2.release();
            }
            
            if (numRelease == 0) {
                // Last one to leave the await() needs to reset variables
                // so it can be reused.
                parties = originalParties;
                numRelease = originalParties;
                extra.release(parties);
            }
        }
        return arrivalIndex;
    }
}
