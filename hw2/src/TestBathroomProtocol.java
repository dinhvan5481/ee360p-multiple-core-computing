import java.util.Arrays;


public class TestBathroomProtocol {

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
                System.out.printf("Female entered.\n");
                brushTeeth();
                protocol.leaveFemale();
                System.out.printf("Female left.\n");
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
                System.out.printf("Male entered.\n");
                brushTeeth();
                protocol.leaveMale();
                System.out.printf("Male left.\n");
            }
        }
    }

    public static void main(String[] args) {

        LockBathroomProtocol lock_proto = new LockBathroomProtocol();
        SyncBathroomProtocol sync_proto = new SyncBathroomProtocol();
        System.out.println(Arrays.toString(args));
        BathroomProtocol protocol = args.length > 0? lock_proto : sync_proto;

        int numberThreads = 8;

        for(int i = 0; i < numberThreads; i++) {
            Runnable r = i % 2 == 0? new MaleRunnable(protocol) : new FemaleRunnable(protocol);
            new Thread(r).start();
        }
    }

}
