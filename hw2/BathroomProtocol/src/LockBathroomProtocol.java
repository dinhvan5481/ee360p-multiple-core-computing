import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

// TODO
// Use locks and condition variables to implement this bathroom protocol
public class LockBathroomProtocol implements BathroomProtocol {
  // declare the lock and conoditions here
  private ReentrantLock mutex;
  private Condition occupiedByFemaleCond;
  private Condition occupiedByMaleCond;
  private AtomicInteger femaleCounter;
  private AtomicInteger maleCounter;

  public LockBathroomProtocol() {
    mutex = new ReentrantLock(true);
    occupiedByFemaleCond = mutex.newCondition();
    occupiedByMaleCond = mutex.newCondition();
    femaleCounter = new AtomicInteger(0);
    maleCounter = new AtomicInteger(0);
  }

  public void enterMale()  {
    mutex.lock();
    try {
      while (femaleCounter.get() > 0) {
        occupiedByFemaleCond.await();
      }
      maleCounter.incrementAndGet();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      mutex.unlock();
    }
  }

  public void leaveMale() {
    mutex.lock();
    try {
      int numberOfMale = maleCounter.decrementAndGet();
      if(numberOfMale == 0) {
        occupiedByMaleCond.signalAll();
      }
    }finally {
      mutex.unlock();
    }
  }

  public void enterFemale() {
    mutex.lock();
    try {
      while (maleCounter.get() > 0) {
        occupiedByMaleCond.await();
      }
      femaleCounter.incrementAndGet();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      mutex.unlock();
    }
  }

  public void leaveFemale() {
    mutex.lock();
    try {
      int numberOfFemale = femaleCounter.decrementAndGet();
      if(numberOfFemale == 0) {
        occupiedByFemaleCond.signalAll();
      }
    }finally {
      mutex.unlock();
    }
  }

  protected void log(){}
}
