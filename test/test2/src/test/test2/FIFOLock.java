package test.test2;

/**
 * Created by quachv on 9/30/2016.
 */
public class FIFOLock {
    private int currentMinTicketNumberInWaitingQueue;
    private int currentTicket = 0;

    public synchronized int getTicket() {
        return ++currentTicket;
    }

    public synchronized void requestCS(int ticketNumer) {


    }

    public synchronized void releaseCS() {

    }
}
