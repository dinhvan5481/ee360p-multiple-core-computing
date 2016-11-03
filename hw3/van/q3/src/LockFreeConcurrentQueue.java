/**
 * Created by van on 11/2/2016.
 */
public class LockFreeConcurrentQueue extends BaseConcurrentQueue {

    @Override
    public boolean enq(int value) {
        return false;
    }

    @Override
    public Integer deq() {
        return null;
    }
}
