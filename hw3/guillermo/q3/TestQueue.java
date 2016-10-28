import java.util.Random;

public class TestQueue {

    final static Random r = new Random();

    static class TestQueueProducer extends Thread {

        LockedQueue queue;

        public TestQueueProducer(LockedQueue queue) {
            this.queue = queue;
        }
        
        public void run() {
            for(;;) {
                try {
                    int time = r.nextInt(1000);
                    System.out.printf("[P] Sleeping for %d\n", time);
                    Thread.sleep(r.nextInt(1000));
                } catch (InterruptedException e) { }

                int number = r.nextInt(1000);
                queue.enq(number);
                System.out.printf("[P] %d enqueued\n", number);
            }
        }
    }

    static class TestQueueConsumer extends Thread {

        LockedQueue queue;

        public TestQueueConsumer(LockedQueue queue) {
            this.queue = queue;
        }

        public void run() {

            for(;;) {
                try {
                    int time = r.nextInt(100);
                    System.out.printf("[C] Sleeping for %d\n", time);
                    Thread.sleep(time);
                } catch (InterruptedException e) { }

                int number = queue.deq();
                System.out.printf("[C] dequeued %d\n", number);
            }

        }

    }

    public static void main(String[] args) {

        LockedQueue lockedQueue = new LockedQueue();

        TestQueueConsumer consumer = new TestQueueConsumer(lockedQueue);
        TestQueueProducer producer = new TestQueueProducer(lockedQueue);

        consumer.start();
        producer.start();

    }
}