import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrier {
  // TODO: Declare variables and the constructor for CyclicBarrier
  // Note that you can use only semaphores but not synchronized blocks and
  // locks 
  private int parties;
  private Semaphore sem;
  private AtomicInteger ticket;
  private Semaphore barrier;

  public CyclicBarrier(int parties) {
      this.parties = parties;
      sem = new Semaphore(parties);
      ticket = new AtomicInteger(0);
      barrier = new Semaphore(0);
  }

  public int await() throws InterruptedException {
    // Waits until all parties have invoked await on this barrier.
    // If the current thread is not the last to arrive then it is
    // disabled for thread scheduling purposes and lies dormant until
    // the last thread arrives.
    // Returns: the arrival index of the current thread, where index
    // (parties - 1) indicates the first to arrive and zero indicates
    // the last to arrive.
    sem.acquire(); // This let the first `parties` to get it.
    int t = ticket.incrementAndGet(); // Each grabs a ticket.

    if (parties - t == 0) {
        // Last thread.
        barrier.release(t - 1); // Release the rest of the threads.
    } else {
        // If they are not the last they are going to block here.
        barrier.acquire();
    }

    int r = ticket.decrementAndGet();

    if(r == 0) {
        // Last to leave notifies the new batch to enter.
        sem.release(parties);
    }

    return parties - t;
  }
}
