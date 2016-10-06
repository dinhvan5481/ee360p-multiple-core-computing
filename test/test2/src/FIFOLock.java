package test.test2;

import java.util.PriorityQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by quachv on 9/30/2016.
 */
public class FIFOLock {
    private int currentMinTicketNumberInWaitingQueue;
    private ReentrantLock mutex = new ReentrantLock();
    private Condition roomReadyCond = mutex.newCondition();

    private int currentTicket = 0;
    private PriorityQueue<Integer> waitingQueue = new PriorityQueue<>();
    private boolean isRoomOccupied = false;


    public synchronized int getTicket() {
        return ++currentTicket;
    }

    public synchronized void requestCS(int ticketNumer) throws InterruptedException {
        if(!isRoomReadyFor(ticketNumer)) {
            waitingQueue.add(ticketNumer);
            while (!isRoomReadyFor(ticketNumer)) {
                roomReadyCond.await();
            }
        }
        isRoomOccupied = true;


    }

    private boolean isRoomReadyFor(int ticketNumer) {
        return !isRoomOccupied && ticketNumer < currentMinTicketNumberInWaitingQueue;
    }

    public synchronized void releaseCS() {
        // update CS counter
        isRoomOccupied = false;
        // get next ticket number
        // TODO: need to handle when
        currentMinTicketNumberInWaitingQueue = waitingQueue.poll();
        // signal waiting threads
        roomReadyCond.signalAll();
    }
}
