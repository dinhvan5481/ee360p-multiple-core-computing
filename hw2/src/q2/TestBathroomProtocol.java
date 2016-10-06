import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;


public class TestBathroomProtocol {

    static class TestStats {
        // Counts females
        public static AtomicInteger females = new AtomicInteger(0);
        // Counts males
        public static AtomicInteger males = new AtomicInteger(0);
        // Counts both.
        public static AtomicInteger people = new AtomicInteger(0); 
    }

    static class FemaleRunnable implements Runnable {
        BathroomProtocol protocol;

        public FemaleRunnable(BathroomProtocol p) {
            protocol = p;
        }

        private void brushTeeth() {
            System.out.println("Female brushing teeth.");
        }

        public void run() {
            for (;;) {
                protocol.enterFemale();
                int fem = TestStats.females.incrementAndGet();
                int peo = TestStats.people.incrementAndGet();
                System.out.printf("Female entered. \t F: %d, P: %d\n", fem, peo);
                assert(fem == peo); // There should be only females at any given time.
                brushTeeth();
                protocol.leaveFemale();
                fem = TestStats.females.decrementAndGet();
                peo = TestStats.people.decrementAndGet();
                System.out.printf("Female left. \t\t F: %d, P: %d\n", fem, peo);
            }
        }
    }

    static class MaleRunnable implements Runnable {
        BathroomProtocol protocol;

        public MaleRunnable(BathroomProtocol p) {
            protocol = p;
        }

        private void brushTeeth() {
            System.out.println("Male brushing teeth.");
        }

        public void run() {
            for (;;) {
                protocol.enterMale();
                // Testing
                int mal = TestStats.males.incrementAndGet();
                int peo = TestStats.people.incrementAndGet();
                System.out.printf("Male entered. \t\t M: %d, P: %d\n", mal, peo);

                // if there are people they should be only males.
                assert  mal == peo;
                brushTeeth();
                protocol.leaveMale();

                // Testing
                mal = TestStats.males.decrementAndGet();
                peo = TestStats.people.decrementAndGet();
                System.out.printf("Male left. \t\t M: %d, P: %d\n", mal, peo);
            }
        }
    }

    public static void main(String[] args) {

        LockBathroomProtocol lock_proto = new LockBathroomProtocol();
        SyncBathroomProtocol sync_proto = new SyncBathroomProtocol();
        System.out.println(Arrays.toString(args));
        // Executes LockBathroomProtocol when executed with extra paramaters
        // i.e.
        // java TestBathroomProtocol Lock
        //
        // and executes SyncBathroomProtocol with NO parameters
        // 
        // java TestBathroomProtocol
        BathroomProtocol protocol = args.length > 0? lock_proto : sync_proto;

        int numberThreads = 8;

        for(int i = 0; i < numberThreads; i++) {
            // 50-50 types of threads.
            Runnable r = i % 2 == 0? new MaleRunnable(protocol) : new FemaleRunnable(protocol);
            new Thread(r).start();
        }
    }

}
