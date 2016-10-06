// Q.1 (20 points) Write a Java class PLock that provides priority based locks. 
// The class provides the following methods requestCS(int priority) and releaseCS(). 
// The priority is either 0 denoting low priority or 1 denoting high priority. 
// Whenever the critical section becomes available, a thread that is waiting with 
// the highest priority is given the access. If there are multiple threads with the
// same priority level waiting for the critical section, then any one of them may 
// enter the critical section. You should guarantee that a low priority thread does 
// not enter the critical section if a thread with a higher priority is waiting.

public class PLock {

    public void requestCS(int priority) {
        // TODO
    }

    public void releaseCS() {
        // TODO
    }
    
}