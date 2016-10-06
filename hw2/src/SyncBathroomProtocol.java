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
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            countMale++;
    }

    public synchronized void leaveMale() {
        countMale--;
        if (countMale == 0) {
            notifyAll();
        }
    }

    public synchronized void enterFemale() {
        while (countMale!=0) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        countFemale++;
    }

    public synchronized void leaveFemale() {
        countFemale--;
        if (countFemale == 0) {
            notifyAll();
        }
    }
}
