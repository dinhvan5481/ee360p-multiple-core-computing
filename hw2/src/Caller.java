/**
 * Created by andy on 9/25/16.
 */
public class Caller implements Runnable{
    CyclicBarrier cb;

    public Caller (int i){
        cb = new CyclicBarrier (i);
    }

    public void run(){
        try {
            int i = cb.await();
          //  System.out.println(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)  {
        Caller call = new Caller(5);
        Thread t1 = new Thread (call);
        Thread t2 = new Thread (call);
        Thread t3 = new Thread (call);
        Thread t4 = new Thread (call);
        Thread t5 = new Thread (call);
        t1.start();
        t2.start();
        t3.start();
    }
}
