
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

// Use locks and condition variables to implement this bathroom protocol
public class LockBathroomProtocol implements BathroomProtocol {
    // declare the lock and conditions here
    final Lock bathroom_lock = new ReentrantLock();
    final Condition maleTurn = bathroom_lock.newCondition();
    final Condition femaleTurn = bathroom_lock.newCondition();

    public enum Turn { MaleIn, MaleOut, FemaleIn, FemaleOut };

    int males = 0;
    int females = 0;

    private Turn turn = Turn.FemaleIn;

    public void enterMale() {
        bathroom_lock.lock();
        try {
            while(turn != Turn.MaleIn)
                maleTurn.await();

            males++;
            System.out.printf("Male entered. (M: %d, F: %d)\n", males, females);
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            bathroom_lock.unlock();
        }
    }

    public void leaveMale() {
        bathroom_lock.lock();
        try {
            males--;
            System.out.printf("Male left. (M: %d, F: %d)\n",  males, females);
            if(males == 0) {
                turn = Turn.FemaleIn;
                femaleTurn.signalAll(); // Let the females know
            } else {
                // Once the first one leaves, no more males entering.
                turn = Turn.MaleOut;
                maleTurn.signal(); // There might still be somebody trying to get out.
            }
        } finally {
            bathroom_lock.unlock();
        }
    }

    public void enterFemale() {
        bathroom_lock.lock();
        try {
            while(turn != Turn.FemaleIn)
                femaleTurn.await();
            
            females++;
            System.out.printf("Female entered. (M: %d, F: %d)\n", males, females);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }finally {
            bathroom_lock.unlock();
        }
    }

    public void leaveFemale() {
        bathroom_lock.lock();
        try {
            females--;
            System.out.printf("Female left. (M: %d, F: %d)\n", males, females);
            if(females == 0) {
                turn = Turn.MaleIn;
                maleTurn.signalAll(); // Let the males know
            } else {
                turn = Turn.FemaleOut;
                femaleTurn.signal(); // There might be more females inside.
            }
        } finally {
            bathroom_lock.unlock();
        }
    }
}
