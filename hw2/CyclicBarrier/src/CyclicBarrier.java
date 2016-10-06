import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrier {
  // TODO: Declare variables and the constructor for CyclicBarrier
  // Note that you can use only semaphores but not synchronized blocks and
  // locks 
  private int parties;

  private Semaphore barrierLock;
  private Semaphore mutex;
  private AtomicInteger lockPointer;

  public CyclicBarrier(int parties) {
    // TODO: The constructor for this CyclicBarrier
    this.parties = parties;

    this.barrierLock = new Semaphore(-parties);

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
      barrierLock.acquire();
    } else {
      barrierLock.release(2 * this.parties + 1);
      //TODO: elimate busy wait. Is there any better way to implement it? Since we need to wait all threads finish before we reset semeaphore
      while(barrierLock.hasQueuedThreads()){

      }
      barrierLock = null;
      barrierLock = new Semaphore(-this.parties);
      lockPointer.set(this.parties);
      mutex.release();
    }
    return lockPointerLocal;
  }
}
