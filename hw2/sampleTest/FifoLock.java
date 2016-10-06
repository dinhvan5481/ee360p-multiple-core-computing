// Q.5 (20 points) Write a Java class FifoLock that provides 
// the following methods getTicket(), requestCS(int ticketnumber) 
// and releaseCS(). When any thread invokes getTicket(), it gets a 
// ticket number. You should ensure that no two ticket numbers are 
// identical. After getting a ticket number, the thread can invoke 
// requestCS anytime. It must provide the ticket number that it received 
// using getTicket. Assume that threads do not cheat and provide the 
// genuine ticket number. You should guarantee the following property. 
// Any process that invokes requestCS enters the critical section if 
// the critical section is empty and there is no process with lower 
// ticket number waiting in the queue.

public class FifoLock {

    public int getTicket() {
        // TODO
    }

    public void requestCS(int ticketnumber) {
        // TODO
    }

    public void releaseCS() {
        // TODO
    }
}