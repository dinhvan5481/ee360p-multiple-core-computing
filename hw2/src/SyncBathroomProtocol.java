// Use synchronized, wait(), notify(), and notifyAll() to implement this
// bathroom protocol

public class SyncBathroomProtocol implements BathroomProtocol {

    Object bath_lock = new Object();

    Object fem_lock = new Object();
    Object mal_lock = new Object();
    Object fem_door = new Object();
    Object mal_door = new Object();

    public enum Turn { MaleIn, MaleOut, FemaleIn, FemaleOut };

    Turn turn = Turn.MaleIn; // Important to start In

    int females = 0;
    int males = 0;

    private void notifyDoor(Object door, boolean all) {
        synchronized (door) {
            if(all) door.notifyAll();
            else door.notify();
        }
    }

    public void enterMale() {
        notifyDoor(fem_door, false);

        while (turn != Turn.MaleIn || females > 0) {
            synchronized (mal_door) {
                try {
                    mal_door.wait();
                } catch (InterruptedException e) {}
            }
        }

        notifyDoor(mal_door, true);
        synchronized (mal_lock) {    
            males++;
        }
    }

    public void leaveMale() {
        synchronized (mal_lock) {
            males--;
            if (males == 0) {
                turn = Turn.FemaleIn; // Once the last one leaves, tell the females.
            } else {
                turn = Turn.MaleOut; // Once the first one leaves, no more males
            }
        }
    }

    public void enterFemale() {
        notifyDoor(mal_door, false);

        while (turn != Turn.FemaleIn || males > 0) {
            synchronized (fem_door) {
                try {
                    fem_door.wait(); // Not yet, wait your turn.
                } catch (InterruptedException ie) {}
            }
        }

        notifyDoor(fem_door, false);
        synchronized (fem_lock) {
            females++;
        }
    }

    public void leaveFemale() {
        synchronized (fem_lock) {
            females--;
            if(females == 0) {
                turn = Turn.MaleIn; // Now the males.
            } else {
                turn = Turn.FemaleOut; // Once the first one leaves, no more females allowed
            }
        }
    }


}
