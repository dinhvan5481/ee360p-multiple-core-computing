/**
 * Created by quachv on 9/20/2016.
 */

class Task implements Runnable {

    private CyclicBarrier barrier;

    public Task(CyclicBarrier barrier) {
        this.barrier = barrier;

    }

    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        try {
            log(String.format("%s is waiting on barrier", threadName));
            barrier.await();
            log(String.format("%s crossed the barrier", threadName));
        } catch (InterruptedException e) {
            log(String.format("InterruptedException happens for thread %s", threadName));
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

        try {
            for(int i = 0; i < numOfParties; i++) {
                threads[i].join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log("End case 1");


    }

    private static void log(String message) {
        System.out.println(message);
    }
}
