
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
    int males_waiting = 0;
    int females_waiting = 0;

    private Turn turn = Turn.FemaleIn;

    public void enterMale() {
        bathroom_lock.lock();
        try {
            while(turn != Turn.MaleIn || females > 0) {
                males_waiting++;
                maleTurn.await();
                males_waiting--;
            }
            males++;
            maleTurn.signal();
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
            if(females_waiting > 0) {
                turn = Turn.MaleOut;
                if (males == 0) {
                    turn = Turn.FemaleIn;
                    femaleTurn.signal();
                }
            } else if (females_waiting == 0) {
                turn = Turn.MaleIn;
            }
        } finally {
            bathroom_lock.unlock();
        }
    }

    public void enterFemale() {
        bathroom_lock.lock();
        
        try {
            while (turn != Turn.FemaleIn || males > 0) {
                females_waiting++;
                femaleTurn.await();
                females_waiting--;
            }

            females++;
            femaleTurn.signal();
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
            if(males_waiting > 0) {
                turn = Turn.FemaleOut;
                if (females == 0) {
                    turn = Turn.MaleIn;
                    maleTurn.signal();
                }
            } else if (males_waiting == 0) {
                turn = Turn.FemaleIn;
            }
        } finally {
            bathroom_lock.unlock();
        }
    }
}
