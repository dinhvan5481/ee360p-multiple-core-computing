import java.util.HashMap;
import java.util.Random;

/**
 * Created by van on 11/4/2016.
 */
public class BagValues {
    private final Random numGen;
    private int min;
    private int max;
    private int numValues;
    private HashMap<Integer, Boolean> bagValuesDict;
    private int[] bagValues;

    public BagValues(int min, int max, int numOfValues) {
        this.min = min;
        this.max = max;
        this.numValues = numOfValues;
        this.bagValues = new int[numOfValues];
        this.bagValuesDict = new HashMap<>();
        this.numGen = new Random();
        int randNum = 0;
        for (int i = 0; i < numOfValues; i++) {
            do {
                randNum = numGen.nextInt(max - min) + min;
            }while (bagValuesDict.containsKey(new Integer(randNum)));
            bagValuesDict.put(randNum, false);
            bagValues[i] = randNum;
        }

    }

    public int getValue() {
        Random indexRand = new Random();
        int index = indexRand.nextInt(this.numValues);
        return bagValues[index];
    }

    public synchronized int getValueAndMarkAdded() {
        Random indexRand = new Random();
        int index = indexRand.nextInt(this.numValues);
        int value = bagValues[index];
        bagValuesDict.put(value, true);
        return value;
    }

    public synchronized boolean isAdded(int value) {
        return bagValuesDict.get(value);
    }

    public synchronized void markAsAdded(int value) {
        bagValuesDict.put(value, true);
    }

    public synchronized void markAsRemoved(int value) {
        bagValuesDict.put(value, false);
    }
}
