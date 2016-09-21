import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrier {
  // TODO: Declare variables and the constructor for CyclicBarrier
  // Note that you can use only semaphores but not synchronized blocks and
  // locks 
  private int parties;

  private Semaphore[] barrierLocks;
  private Semaphore mutex;
  private AtomicInteger lockPointer;

  public CyclicBarrier(int parties) {
    // TODO: The constructor for this CyclicBarrier
    this.parties = parties;

    this.barrierLocks = new Semaphore[parties];

    for (int i = 0; i < parties; i++) {
      barrierLocks[i] = new Semaphore(0);
    }
    this.mutex = new Semaphore(1);
    lockPointer = new AtomicInteger(parties);
  }

  public int await() throws InterruptedException {
    // Waits until all parties have invoked await on this barrier.
    // If the current thread is not the last to arrive then it is
    // disabled for thread scheduling purposes and lies dormant until
    // the last thread arrives.
    // Returns: the arrival index of the current thread, where index
    // (parties - 1) indicates the first to arrive and zero indicates
    // the last to arrive.
    mutex.acquire();
    int lockPointerLocal = lockPointer.decrementAndGet();

    if(lockPointerLocal > 0) {
      mutex.release();
      barrierLocks[lockPointerLocal].acquire();
    } else {
      for (int i = this.parties - 1; i > 0 ; i--) {
        barrierLocks[i].release(2);
      }
      mutex.release();
    }
    return lockPointerLocal;
  }
}
