import java.util.Random;


public class TestBarrier {

    public static Random r = new Random();

    static class TestRunner implements Runnable {

        CyclicBarrier barrier;

        public TestRunner(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        public void run() {

            try {
            for(;;) {
                System.out.printf("%d wait before entering\n", Thread.currentThread().getId());
                Thread.sleep(r.nextInt(5000));
                int ticket = barrier.await();
                System.out.printf("%d waited at the barrier with ticket: %d\n", Thread.currentThread().getId(), ticket);
            }
            } catch (InterruptedException e) {
                System.err.println(e.toString());
            }
        }
    }

    public static void main(String[] args) {

        int barrierSize = 3;
        int numberOfThreads = 5;
        CyclicBarrier barrier = new CyclicBarrier(barrierSize);


        for(int i = 0; i < numberOfThreads; i++) {
            new Thread(new TestRunner(barrier)).start();
        }

    }
}