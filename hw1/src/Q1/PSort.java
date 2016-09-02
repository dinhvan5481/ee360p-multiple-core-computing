package Q1;

/**
 * Created by andy on 8/25/16.
 */
public class PSort extends Thread {

    int [] A = new int[10000];
    int begin = 0;
    int end = 0;
    int partitionIndex = 0;

    // Constructor needed to pass in the values for run()
    public PSort(int[] A, int begin, int end){
        this.A = A;
        this.begin = begin;
        this.end = end;
    }

    public void run(){
        if (begin < end-1) { // begin == end-1 is end of sort
            final int pivot = A[end - 1];
            partitionIndex = begin;
            for (int i = begin; i < end - 1; i++) {
                if (A[i] <= pivot) {
                    int temp = A[i];
                    A[i] = A[partitionIndex];
                    A[partitionIndex] = temp; //move smaller element to the partition index
                    partitionIndex++; // points at the next element that can be bigger
                } else {
                    //skip paritition index increment because the element is larger, move to find the next small element
                }
            }

            A[end - 1] = A[partitionIndex];
            A[partitionIndex] = pivot; //swap pivot with partition index element

            PSort pSortLeft = new PSort (A, begin, partitionIndex);//sort left side
            PSort pSortRight = new PSort (A, partitionIndex+1,end);//soft right side

            pSortLeft.start();
            pSortRight.start();

            try {
                pSortLeft.join(); // wait for element to be sorted
                pSortRight.join();
            }  catch (InterruptedException e){}
        }
    }

    public static void parallelSort(int[] A, int begin, int end){
            PSort pSort = new PSort(A, begin, end);
            pSort.start();
       try{
            pSort.join(); //wait for element to be sorted
        } catch (InterruptedException e){};
    }
}

