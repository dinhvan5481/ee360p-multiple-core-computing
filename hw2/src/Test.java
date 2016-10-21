/**
 * Created by andy on 9/26/16.
 */
public class Test {

    public static void main (String[] args){

      //  SyncBathroomProtocol protocol = new SyncBathroomProtocol();
        LockBathroomProtocol protocol = new LockBathroomProtocol();
        FemaleProtocol femaleProtocol = new FemaleProtocol(protocol);
        MaleProtocol maleProtocol = new MaleProtocol(protocol);

        Thread maleThread = new Thread (maleProtocol);
        Thread maleThread2 = new Thread (maleProtocol);
        Thread maleThread3 = new Thread (maleProtocol);
        Thread femaleThread = new Thread (femaleProtocol);
        Thread femaleThread2 = new Thread (femaleProtocol);
        Thread femaleThread3 = new Thread (femaleProtocol);

        maleThread.start();
        femaleThread.start();
        maleThread2.start();
        femaleThread2.start();
        maleThread3.start();
        femaleThread3.start();
        /* only one thread can be in thread at once unless it was put on wait, notifyall to wake up all threads but still have to check for the condition,
         use the same object has the monitor with shared variables, but only one thread can enter the method. Same as semaphore, only one copy of the semaphore.
         when no males in the room, notify only triggers one to be ready in the queue, trigger all makes multiple males to be alive to check the condition when no other thread is inside the synchronized,
         queue for wait, and queue for entering the synchronized block
         */
    }
}
