// Use synchronized, wait(), notify(), and notifyAll() to implement this
// bathroom protocol

// 
public class SyncBathroomProtocol implements BathroomProtocol {

    Object bath_lock = new Object();

    Object fem_lock = new Object();
    Object mal_lock = new Object();
    Object fem_door = new Object();
    Object mal_door = new Object();

    // This is the key for starvation freedom, give each other turns.
    public enum Turn { MaleIn, MaleOut, FemaleIn, FemaleOut };

    Turn turn = Turn.MaleIn; // Important to start In

    // Keep track of kinds of people in.
    int females = 0;
    int males = 0;

    // Keep track of people waiting to get in.
    int females_waiting = 0;
    int males_waiting = 0;

    private void notifyDoor(Object door) {
        synchronized (door) {
            door.notify();
        }
    }

    public void enterMale() {
        notifyDoor(fem_door); // Oh? Race?

        // Wait until is male's turn and there are no females in.
        // Ghost read, don't need to acquire the lock only to know that you don't have to wait.
        while (turn != Turn.MaleIn || females > 0) {
            synchronized (mal_door) {
                try {
                    males_waiting++;
                    mal_door.wait();
                } catch (InterruptedException e) {}
                finally { males_waiting--; }
            }
        }

        // Once one's through let the others know.
        notifyDoor(mal_door);
        synchronized (mal_lock) {    
            males++;
        }
    }

    public void leaveMale() {
        synchronized (mal_lock) {
            males--;
            if (females_waiting > 0) {
                turn = Turn.MaleOut;
                if(males == 0) { // I'm the last one
                    turn = Turn.FemaleIn; // tell the females.
                }
            } else if (females_waiting == 0) {
                turn = Turn.MaleIn; // No reason to wait!
            }
        }
    }

    public void enterFemale() {
        notifyDoor(mal_door); // Let's race to the door

        while (turn != Turn.FemaleIn || males > 0) {
            synchronized (fem_door) {
                try {
                    females_waiting++;
                    fem_door.wait(); // Not yet, wait your turn.
                } catch (InterruptedException ie) {}
                finally { females_waiting--; }
            }
        }

        // Once one is in, let others know.
        notifyDoor(fem_door);
        synchronized (fem_lock) {
            females++;
        }
    }

    public void leaveFemale() {
        synchronized (fem_lock) {
            females--;
            if(males_waiting > 0) {
                turn = Turn.FemaleOut;
                if(females == 0) {
                    turn = Turn.MaleIn; 
                }
            } else if(males_waiting == 0){
                turn = Turn.FemaleIn; // No reason to wait.
            }
        }
    }


}
