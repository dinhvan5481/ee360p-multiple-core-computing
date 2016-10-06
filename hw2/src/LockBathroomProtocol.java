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
                    System.out.println("before male wait");
                    noFemales.await();
                    System.out.println("after male wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countMale++;
            System.out.println("male enters");
        } finally {
            monitorLock.unlock();
        }
    }

    public void leaveMale() {
        monitorLock.lock();
        try {
            countMale--;
            System.out.println("male leaves");
            if (countMale == 0) {
                noMales.signalAll();
                System.out.println("after notify when no males in the room");
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
                    System.out.println("before female wait");
                    noMales.await();
                    System.out.println("after female wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countFemale++;
            System.out.println("female enters");
        } finally {
            monitorLock.unlock();
        }
    }

    public void leaveFemale() {
        monitorLock.lock();
        try {
            countFemale--;
            System.out.println("female leaves");
            if (countFemale == 0) {
                noFemales.signalAll();
                System.out.println("after notify when no female is in the room.");
            }
        } finally {
            monitorLock.unlock();
        }
    }
}