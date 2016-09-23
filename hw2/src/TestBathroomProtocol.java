
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
                brushTeeth();
                protocol.leaveFemale();
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
                brushTeeth();
                protocol.leaveMale();
            }
        }
    }

    public static void main(String[] args) {

        BathroomProtocol protocol = new LockBathroomProtocol();

        int numberThreads = 4;

        for(int i = 0; i < numberThreads; i++) {
            Runnable r = i % 2 == 0? new MaleRunnable(protocol) : new FemaleRunnable(protocol);
            new Thread(r).start();
        }
    }

}
