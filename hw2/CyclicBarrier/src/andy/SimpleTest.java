package andy;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by quachv on 9/20/2016.
 */

class Task implements Runnable {

    private CyclicBarrier barrier;
    private AtomicInteger threadCounter;
    private int threadId;
    private int numOfParties;
    private boolean[] inCSMark;

    public Task(CyclicBarrier barrier, AtomicInteger threadCounter, boolean[] inCSMark, int numOfParties) {
        this.barrier = barrier;
        this.threadCounter = threadCounter;
        this.inCSMark = inCSMark;
        this.numOfParties = numOfParties;

    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {

            this.threadId = this.threadCounter.getAndIncrement();
            //log(String.format("Thread %d is waiting on barrier", threadId));
            barrier.await();
            //log(String.format("Thread %d crossed the barrier", threadId));
            if(this.threadId < this.numOfParties) {
                this.inCSMark[this.threadId] = true;
                //log(String.format("Thread %d crossed the barrier", threadId));
            } else {
                int threadId = this.checkIfThisThreadGetInCSBeforeNFirstThread();
                if(threadId > 0) {
                    log(String.format("Failed: thread %d get in before thread %d", this.threadId, threadId));
                }
            }
        } catch (InterruptedException e) {
            log(String.format("InterruptedException happens for thread %s", threadName));
            log(e.getMessage());
        }
    }

    private int checkIfThisThreadGetInCSBeforeNFirstThread() {
        for (int i = 0; i < this.numOfParties; i++) {
            if(!this.inCSMark[i]) {
                return i;
            }
        }
        return -1;
    }
    
    private void log(String message) {
        System.out.println(message);
    }
}

public class SimpleTest {

    public static void main(String[] args) {
        int numOfParties = 50;
        int numOfThreads = 2 * numOfParties + 2;
        Thread[] threads = new Thread[numOfThreads];
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numOfParties);
        boolean[] inCSMark = new boolean[numOfParties];
        int WAITING_TIME_OUT_ms = 100;
        AtomicInteger threadCounter = new AtomicInteger(0);

        if(true) {
            log("Case 1: When waiting threads = number of parties");
            for (int i = 0; i < numOfParties; i++) {
                threads[i] = new Thread(new Task(cyclicBarrier, threadCounter, inCSMark, numOfParties), String.format("Thread %d", i));
                threads[i].start();
            }

            try {
                for (int i = 0; i < numOfParties; i++) {
                    threads[i].join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log("End case 1");
        }

        if(true) {
            log("Case 2: When waiting threads more than number of parties");
            for (int runCounter = 0; runCounter < 1000; runCounter++) {
                log(String.format("Start Run %d times", runCounter));
                for(int i = 0; i < numOfThreads; i++) {
                    threads[i] = new Thread(new Task(cyclicBarrier, threadCounter, inCSMark, numOfParties), String.format("Thread %d", i));
                    threads[i].start();
                }

                try {
                    for(int i = 0; i < numOfThreads; i++) {
                        threads[i].join(WAITING_TIME_OUT_ms);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < numOfParties; i++) {
                    if(!inCSMark[i]) {
                        log(String.format("Failed: thread %d is still waiting", i));
                    }
                }
                for (int i = 0; i < numOfThreads; i++) {
                    if(threads[i].isAlive()) {
                        threads[i].stop();
                    }
                }
                log(String.format("End Run %d times", runCounter));
            }
            log("End case 2");
        }


/*        if(true) {
            log("Case 3: Race condition when release barrier, check if first n thread can get in");

            log("End case 3")
        }*/

    }

    private static void log(String message) {
        System.out.println(message);
    }
}
