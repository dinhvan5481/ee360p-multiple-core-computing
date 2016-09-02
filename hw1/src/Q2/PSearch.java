package Q2;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * Created by andy on 8/26/16.
 */

public class PSearch implements Callable<Integer> {

    public static ExecutorService threadPool = Executors.newCachedThreadPool();

    int k = 0;
    int [] A = null;
    int partSize = 0;
    int partNum = 0;


    public PSearch(int k, int[] A, int partSize, int partNum){
        this.k = k;
        this.A = A;
        this.partSize = partSize;
        this.partNum = partNum;
    }

    public Integer call(){

        for (int i = 0; i < A.length; i++){
            if (A[i] == k ) {
                return partSize * partNum + i;
            }
        }
        return -1;
    }

    public static int parallelSearch(int k, int[] A, int numThreads){
        // TODO: Implement your parallel search function

        int listLength = A.length;
        int partLength = A.length;

        int numParts = numThreads;
        if (numThreads < listLength) {
            partLength = listLength / numThreads;
        } else {
            numParts = 1;
        }

        ArrayList<Future<Integer>> futureIntList = new  ArrayList<Future<Integer>>();

        for (int i = 0; i < numParts; i++) {
            int begin = i * partLength;
            int partSize = Math.min(A.length - begin, partLength);
            int [] temp = new int[partSize];
            System.arraycopy(A, begin, temp, 0, partSize);
            Future<Integer> f1 = threadPool.submit(new PSearch(k,temp, partSize, i));
            futureIntList.add(f1);
        }

        for (int i = 0; i< numThreads; i++) {
            try {
                int foundIndex = futureIntList.get(i).get();
                if ( foundIndex != -1){
                    return foundIndex;
                }
            } catch (Exception e) {}
        }

        return -1;
    }

}