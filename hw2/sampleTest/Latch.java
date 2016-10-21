// Q.2 (15 points) Give Java class for Latch. A Latch is initialized with a given count. 
// It supports two methods, await() and countDown(). The await methods block until the 
// current count reaches zero due to invocations of the countDown() method, after which 
// all waiting threads are released and any subsequent invocations of await 
// return immediately. This is a one-shot phenomenon – the count cannot be reset.

public class Latch {

    public Latch(int count) {
        // TODO
    }

    public void await() {
        // TODO
    }

    public void countDown() {
        // TODO
    }
}