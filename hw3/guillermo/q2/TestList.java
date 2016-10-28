

public class TestList {

    final static int NODES_COUNT = 1000;

    static class TestListRunner extends Thread {

        SortedLinkedList list;
        int seed;

        public TestListRunner(SortedLinkedList list, int seed) {
            this.list = list;
            this.seed = seed;
        }

        public void run() {
            int limit = (seed + 1) * NODES_COUNT;
            int start = seed * NODES_COUNT;
            
            for(int i = start; i < limit; i++) {
                if(!list.add(i)) {
                    throw new IllegalStateException("Some other thread added " + i);
                }
            }

            for(int i = start; i < limit; i++) {
                if(!list.contains(i)) {
                    throw new IllegalStateException("Not found " + i);
                }
            }

            String listState = list.toString();
            if(!verifyListState(listState)) {
                throw new IllegalStateException("List is not ordered.");
            }
            
            for(int i = start; i < limit; i++) {
                if(!list.remove(i)) {
                    throw new IllegalStateException("Some other thread removed " + i);
                }
            }
        }

        private boolean verifyListState(String state) {
            String trimmed = state.substring(1, state.length() - 1);
            String[] numbers = trimmed.split(",");
            int last = Integer.MIN_VALUE;
            for(String number : numbers) {
                int current = Integer.parseInt(number.trim());
                if(current < last) {
                    return false;
                }
                last = current;
            }
            return true;
        }
    }

    public static void runTests(SortedLinkedList list, int numberOfThreads) {
        TestListRunner[] runners = new TestListRunner[numberOfThreads];

        System.out.printf("Testing list %s with %d threads.\n", list.getClass().getName(), numberOfThreads);
        System.out.printf("Number of nodes %d\n", NODES_COUNT);
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < runners.length; i++) {
            runners[i] = new TestListRunner(list, i);
            runners[i].start();
        }
        
        for (TestListRunner runner : runners) {
            try {
            runner.join(); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        System.out.printf("Tests Completed in %d milliseconds.\n\n", System.currentTimeMillis() - startTime);
    }

    public static void main(String[] args){

        runTests(new FineLinkedList(), 1);
        runTests(new FineLinkedList(), 2);
        runTests(new FineLinkedList(), 4);
        runTests(new FineLinkedList(), 8);

        runTests(new CoarsedLinkedList(), 1);
        runTests(new CoarsedLinkedList(), 2);
        runTests(new CoarsedLinkedList(), 4);
        runTests(new CoarsedLinkedList(), 8);
        
    }
}