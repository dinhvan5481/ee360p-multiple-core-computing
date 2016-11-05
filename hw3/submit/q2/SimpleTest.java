import java.util.Random;

/**
 * Created by dinhvan5481 on 10/28/16.
 */
public class SimpleTest {
    public static void main(String[] args) throws InterruptedException {
        final int numThreads = 4;
        final int numThreadRemove = 2;
        final int max = 500;
        final int min = 1;
        final int numValues = 100;
        final int numRun = 1000;
        BagValues bagValues = new BagValues(min, max, numValues);
        final Thread[] threads = new Thread[numThreads];

        log("Case 1: happy path");
        SortedLinkedList listUT = new FineGrainedConcurrentSortedLinkedList();
        for (int i = numThreadRemove; i < numThreads; i++) {
            threads[i] = new Thread(new TestRunner(listUT, bagValues, TestRunner.OperationType.Add, numRun));
            threads[i].start();
        }
        for (int i = 0; i < numThreadRemove; i++) {
            threads[i] = new Thread(new TestRunner(listUT, bagValues, TestRunner.OperationType.Remove, numRun / 100));
            threads[i].start();
        }
        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }


        if(!((BaseConcurrentSortedLinkedList)listUT).isInASCOrder()) {
            log("Test failed");
        }
        log(listUT.toString());
        log("End case 1");


    }

    public static void log(String msg) {
        System.out.println(msg);
    }
}

class TestRunner implements Runnable {
    public static enum OperationType {
        Add,
        Remove
    }
    private SortedLinkedList sut;
    private OperationType op;
    private BagValues bagValues;
    private int numRun;

    public TestRunner(SortedLinkedList linkedList, BagValues bagValues, OperationType op, int numRun) {
        this.sut = linkedList;
        this.op = op;
        this.bagValues = bagValues;
        this.numRun = numRun;
    }

    @Override
    public void run() {
        if(this.op == OperationType.Add) {
            log("Adding thread");
            int data = 0;
            for (int i = 0; i < this.numRun; i++) {
                data = bagValues.getValueAndMarkAdded();
                if(this.sut.add(data)) {
                    if(!(this.sut.contains(data) && bagValues.isAdded(data))) {
                        log(String.format("Maybe failed. Added %d, but not exist in the list. Posibly another thread remove it", data));
                        log(sut.toString());
                    }
                }
            }
        } else if (this.op == OperationType.Remove) {
            log("Removing thread");
            int data = 0;
            for (int i = 0; i < this.numRun; i++) {
                data = bagValues.getValue();
                if(this.sut.contains(data)) {
                    if(!this.sut.remove(data)) {
                        log(String.format("Failed. Remove exist not success %d", data));
                        log(sut.toString());
                    }
                }
            }

        }

    }

    private void log(String msg) {
        System.out.println(msg);
    }
}
