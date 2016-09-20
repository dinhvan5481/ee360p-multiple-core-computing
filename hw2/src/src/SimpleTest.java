/**
 * Created by quachv on 9/20/2016.
 */

class Task implements Runnable {

    private CyclicBarrier barrier;
    private String threadName;

    public Task(CyclicBarrier barrier) {
        this.barrier = barrier;
        this.threadName = Thread.currentThread().getName();
    }

    @Override
    public void run() {
        try {
            log(String.format("%s is waiting on barrier", this.threadName));
            barrier.await();
            log(String.format("%s crossed the barrier", this.threadName));
        } catch (InterruptedException e) {
            log(String.format("InterruptedException happens for thread %s", this.threadName));
            log(e.getMessage());
        }
    }

    private void log(String message) {
        System.out.println(message);
    }
}

public class SimpleTest {

    public static void main(String[] args) {
        int numOfParties = 5;
        Thread[] threads = new Thread[numOfParties + 2];
        CyclicBarrier cyclicBarrier = new CyclicBarrier(numOfParties);

        log("Case 1: When waiting threads reach the right number of parties");
        for(int i = 0; i < numOfParties; i++) {
            threads[i] = new Thread(new Task(cyclicBarrier), String.format("Thread %d", i));
            threads[i].start();
        }
        log("End case 1");


    }

    private static void log(String message) {
        System.out.println(message);
    }
}
