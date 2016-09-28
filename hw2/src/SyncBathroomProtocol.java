/**
 * Created by andy on 9/26/16.
 */

// TODO
// Use synchronized, wait(), notify(), and notifyAll() to implement this
// bathroom protocol

public class SyncBathroomProtocol implements BathroomProtocol {

    int countMale = 0, countFemale = 0;

    public synchronized void enterMale() {
            while (countFemale != 0) {
                try {
                    System.out.println("before male wait");
                    this.wait();
                    System.out.println("after male wait");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countMale++;
        System.out.println("male enters");
    }

    public synchronized void leaveMale() {
        countMale--;
        System.out.println("male leaves");
        if (countMale == 0) {
            notifyAll();
            System.out.println("after notify when no males in the room");
        }
    }

    public synchronized void enterFemale() {
        while (countMale!=0) {
            try {
                System.out.println("before female wait");
                this.wait();
                System.out.println("after female wait");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        countFemale++;
        System.out.println("female enters");
    }

    public synchronized void leaveFemale() {
        countFemale--;
        System.out.println("female leaves");
        if (countFemale == 0) {
            notifyAll();
            System.out.println("after notify when no female is in the room.");
        }
    }
}