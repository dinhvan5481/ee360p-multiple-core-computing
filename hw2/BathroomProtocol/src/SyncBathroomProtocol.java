// TODO
// Use synchronized, wait(), notify(), and notifyAll() to implement this
// bathroom protocol

public class SyncBathroomProtocol implements BathroomProtocol {
  private int numberOfPeople;
  private int numberOfFemale;
  private int numberOfMale;

  public SyncBathroomProtocol() {
    numberOfPeople = 0;
    numberOfFemale = 0;
    numberOfMale = 0;
  }

  public synchronized void enterMale() {
    while(numberOfFemale > 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    numberOfMale++;
  }

  public synchronized void leaveMale() {
    numberOfMale--;
    if(numberOfMale == 0) {
      notifyAll();
    }
  }

  public synchronized void enterFemale() {
    while (numberOfMale > 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    numberOfFemale++;
  }

  public synchronized void leaveFemale() {
    numberOfFemale--;
    if(numberOfFemale == 0) {
      notifyAll();
    }
  }


}
