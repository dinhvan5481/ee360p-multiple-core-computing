import java.util.Random;

/**
 * Created by dinhvan5481 on 10/28/16.
 */
public class SimpleTest {
    public static void main(String[] args) throws InterruptedException {
        final int numThreads = 1;
        final int numNodes = 10;
        final int max = 100;
        final int min = 0;
        final Thread[] threads = new Thread[numThreads];

        log("Case 1: happy path");
        SortedLinkedList listUT = new CoarseGrainedConcurrentSortedLinkedLists();
        TestRunner test = new TestRunner(listUT, numNodes, TestRunner.OperationType.Add, min, max);
        test.run();


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
    private int numNodes;
    private OperationType op;
    private int min;
    private int max;
    private Random numGen;

    public TestRunner(SortedLinkedList linkedList, int numNodes, OperationType op, int min, int max) {
        this.sut = linkedList;
        this.numNodes = numNodes;
        this.op = op;
        this.min = min;
        this.max = max;
        this.numGen = new Random();
    }

    @Override
    public void run() {
        if(this.op == OperationType.Add) {
            for (int i = 0; i < this.numNodes; i++) {
                int data = this.numGen.nextInt(max - min) + min;
                log("Prepare to add");
                if(this.sut.add(data)) {
                    log("Prepare to check contains");
                   if(!this.sut.contains(data)) {
                       log(String.format("Error while adding %d: the list not contain the value after added", data));
                   }
                } else {
                    if(!this.sut.contains(data)) {
                        log(String.format("Error while adding %d: the list not contain the value even it said it has", data));
                    }

                }
            }
        }

    }

    private void log(String msg) {
        System.out.println(msg);
    }
}
