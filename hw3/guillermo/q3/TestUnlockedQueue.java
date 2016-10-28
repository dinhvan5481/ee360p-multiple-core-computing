import java.util.Random;

public class TestUnlockedQueue {

    final static Random r = new Random();

    static class TestUnlockedQueueProducer extends Thread {

        UnlockedQueue queue;

        public TestUnlockedQueueProducer(UnlockedQueue queue) {
            this.queue = queue;
        }
        
        public void run() {
            for(;;) {
                try {
                    int time = r.nextInt(10);
                    System.out.printf("[P] Sleeping for %d\n", time);
                    Thread.sleep(time);
                } catch (InterruptedException e) { }

                int number = r.nextInt(1000);
                queue.enq(number);
                System.out.printf("[P] %d enqueued\n", number);
            }
        }
    }

    static class TestUnlockedQueueConsumer extends Thread {

        UnlockedQueue queue;

        public TestUnlockedQueueConsumer(UnlockedQueue queue) {
            this.queue = queue;
        }

        public void run() {

            for(;;) {
                try {
                    int time = r.nextInt(100);
                    System.out.printf("[C] Sleeping for %d\n", time);
                    Thread.sleep(time);
                } catch (InterruptedException e) { }

                if(!queue.deq()) {
                    System.out.printf("[C] not possible to deque. Size: %d", queue.count());
                } else {
                    System.out.printf("[C] dequeued\n");
                }
            }

        }

    }

    public static void main(String[] args) {

        UnlockedQueue lockedQueue = new UnlockedQueue();

        TestUnlockedQueueConsumer consumer = new TestUnlockedQueueConsumer(lockedQueue);
        TestUnlockedQueueProducer producer = new TestUnlockedQueueProducer(lockedQueue);

        consumer.start();
        producer.start();

    }
}