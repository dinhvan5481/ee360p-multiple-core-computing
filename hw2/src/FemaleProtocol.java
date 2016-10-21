/**
 * Created by andy on 9/26/16.
 */
public class FemaleProtocol implements Runnable {
/*
    SyncBathroomProtocol protocol;

    public FemaleProtocol (SyncBathroomProtocol protocol){
        this.protocol = protocol;
    }
*/
    LockBathroomProtocol protocol;
    public FemaleProtocol (LockBathroomProtocol protocol){
        this.protocol = protocol;
    }
    public void run(){
        this.protocol.enterFemale();
        System.out.println("female inside");
        this.protocol.leaveFemale();

    }
}
