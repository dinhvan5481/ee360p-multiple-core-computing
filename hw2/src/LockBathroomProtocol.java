import java.util.concurrent.locks.*;

/**
 * Created by andy on 9/26/16.
 */
// TODO
// Use locks and condition variables to implement this bathroom protocol
public class LockBathroomProtocol implements BathroomProtocol {
    // declare the lock and conditions here
    final ReentrantLock monitorLock = new ReentrantLock ( ) ;
    final Condition noMales= monitorLock.newCondition();
    final Condition noFemales = monitorLock.newCondition();

    int countMale = 0, countFemale = 0;

    public void enterMale() {
        monitorLock.lock();
        try {
            while (countFemale != 0) {
                try {
                    noFemales.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countMale++;
        } finally {
            monitorLock.unlock();
        }
    }

    public void leaveMale() {
        monitorLock.lock();
        try {
            countMale--;
            if (countMale == 0) {
                noMales.signalAll();
            }
        } finally {
            monitorLock.unlock();
        }
    }

    public void enterFemale() {
        monitorLock.lock();
        try {
            while (countMale != 0) {
                try {
                    noMales.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countFemale++;
        } finally {
            monitorLock.unlock();
        }
    }

    public void leaveFemale() {
        monitorLock.lock();
        try {
            countFemale--;
            if (countFemale == 0) {
                noFemales.signalAll();
            }
        } finally {
            monitorLock.unlock();
        }
    }
}
