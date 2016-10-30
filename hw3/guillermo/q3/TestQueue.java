import java.util.Random;

public class TestQueue {

    final static Random r = new Random();

    static class TestQueueProducer extends Thread {

        MyQueue queue;

        public TestQueueProducer(MyQueue queue) {
            this.queue = queue;
        }
        
        public void run() {
            for(;;) {
                try {
                    int time = r.nextInt(1000);
                    System.out.printf("[P] Sleeping for %d\n", time);
                    Thread.sleep(time);
                } catch (InterruptedException e) { }

                int number = r.nextInt(1000);
                queue.enq(number);
                System.out.printf("[P] %d enqueued\n", number);
            }
        }
    }

    static class TestQueueConsumer extends Thread {

        MyQueue queue;

        public TestQueueConsumer(MyQueue queue) {
            this.queue = queue;
        }

        public void run() {

            for(;;) {
                try {
                    int time = r.nextInt(100);
                    System.out.printf("[C] Sleeping for %d\n", time);
                    Thread.sleep(time);
                } catch (InterruptedException e) { }

                Integer number = queue.deq();
                System.out.printf("[C] dequeued %d\n", number);
            }

        }

    }

    public static void main(String[] args) {

        MyQueue lockedQueue = new LockQueue();

        TestQueueConsumer consumer = new TestQueueConsumer(lockedQueue);
        TestQueueProducer producer = new TestQueueProducer(lockedQueue);

        consumer.start();
        producer.start();

        MyQueue lockFreeQueue = new LockFreeQueue();

        TestQueueConsumer lockFreeConsumer = new TestQueueConsumer(lockFreeQueue);
        TestQueueProducer lockFreeProducer = new TestQueueProducer(lockFreeQueue);

        lockFreeConsumer.start();
        lockFreeProducer.start();

    }
}