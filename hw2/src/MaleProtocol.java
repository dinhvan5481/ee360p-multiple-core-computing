/**
 * Created by andy on 9/26/16.
 */
public class MaleProtocol implements Runnable {
/*
    SyncBathroomProtocol protocol;

    public MaleProtocol (SyncBathroomProtocol protocol){
        this.protocol = protocol;
    }
*/
    LockBathroomProtocol protocol;
    public MaleProtocol (LockBathroomProtocol protocol){
        this.protocol = protocol;
    }

    public void run(){
        this.protocol.enterMale();
        System.out.println("Male inside");
        this.protocol.leaveMale();
    }
}
