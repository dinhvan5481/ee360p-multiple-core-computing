package Q2_change;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * EE382 - Multicore computing
 * Assignment 1
 * 
 * Guillermo Leon   | gel494
 * Van Quach        | 
 * Andy Yang        | 
 * 
 */

public class PSearch implements Callable<Integer> {

    int k = 0;
    int begin = -1;
    int end = -1;
    int [] Array = null;
    int partSize = 0;
    int partNum = 0;


    public PSearch(int k, int[] array, int begin, int end){
        this.k = k;
        this.begin = begin;
        this.end = end;
        this.Array = array;
    }

    public Integer call(){
        for (int i = begin; i < end; i++){
            if (Array[i] == k ) {
                return i;
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
        // Let's create just that number of threads.
        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads); 

        for (int i = 0; i < numParts; i++) {
            int begin = i * partLength;
            int partSize = Math.min(A.length - begin, partLength);
            Future<Integer> f1 = threadPool.submit(new PSearch(k, A, begin, begin + partSize));
            futureIntList.add(f1);
        }

        for (int i = 0; i< numThreads; i++) {
            try {
                int foundIndex = futureIntList.get(i).get();
                if ( foundIndex != -1){
                    threadPool.shutdown();
                    return foundIndex;
                }
            } catch (Exception e) {}
        }
        threadPool.shutdown();
        return -1;
    }

}