import java.util.HashMap;
import java.util.Random;

/**
 * Created by quachv on 9/21/2016.
 */

public class SimpleTest {
    public static void main(String[] args) {
        int numOfPeople = 10;
        int numOfFemale = 5;
        People[] people = new People[numOfPeople];
        Thread[] threads = new Thread[numOfPeople];
        BathroomProtocol lockProtocol = new LockBathroomProtocol();
        BathroomProtocol syncBathRoomProtocol = new SyncBathroomProtocol();

        int numberOfTestCases = 10;
        HashMap<Integer, Boolean> choosingBag = new HashMap<>();
        boolean[] testCaseEnable = new boolean[numberOfTestCases];


        for (int i = 0; i < numOfFemale; i++) {
            people[i] = new Female(i, (i+1) * 1000);
            people[i].setBathroom(lockProtocol);
        }

        for (int i = numOfFemale; i < numOfPeople; i++) {
            people[i] = new Male(i, (i+1) * 1000);
            people[i].setBathroom(lockProtocol);
        }

        testCaseEnable[0] = false;
        if(testCaseEnable[0]) {
            log("Case 1: All female get in first, and then male. Expect: all female leave before male can get in");
            for (int i = numOfFemale - 1; i > 0 ; i--) {
                threads[i] = new Thread(people[i]);
                threads[i].start();
            }
            for (int i = numOfPeople - 1; i >= numOfFemale; i--) {
                threads[i] = new Thread(people[i]);
                threads[i].start();
            }
            log("End case 1");
        }

        testCaseEnable[1] = false;
        if(testCaseEnable[1]) {
            int peopleThreadIndex = -1;
            Random threadIndexGenerator = new Random();
            log("Case 2: People enter in randomly order. Expect: all female / male whom enter first leave before male can get in");

            for (int i = 0; i < numOfPeople; i++) {
                do {
                    peopleThreadIndex = threadIndexGenerator.nextInt(numOfPeople);
                }while (choosingBag.containsKey(peopleThreadIndex));
                choosingBag.put(peopleThreadIndex, true);
                threads[peopleThreadIndex] = new Thread(people[peopleThreadIndex]);
                threads[peopleThreadIndex].start();
            }
            log("End case 2");
        }

        testCaseEnable[2] = true;
        if(testCaseEnable[2]) {
            for (int i = 0; i < numOfPeople; i++) {
                people[i].setBathroom(syncBathRoomProtocol);
            }
            log("Case 3: Use Sync Protocol - All female get in first, and then male. Expect: all female leave before male can get in");
            for (int i = numOfFemale - 1; i > 0 ; i--) {
                threads[i] = new Thread(people[i]);
                threads[i].start();
            }
            for (int i = numOfPeople - 1; i >= numOfFemale; i--) {
                threads[i] = new Thread(people[i]);
                threads[i].start();
            }
            log("End case 3");
        }
    }

    private static void log(String message) {
        System.out.println(message);
    }
}

class People implements Runnable {
    protected final long brushTeethDuration;
    protected int id;
    protected BathroomProtocol bathroom;
    protected People(int id, long brushTeethDuration) {
        this.id = id;
        this.brushTeethDuration = brushTeethDuration;
    }

    public void setBathroom(BathroomProtocol bathroom) {
        this.bathroom = bathroom;
    }

    protected void brushTeeth() throws InterruptedException {
        say(String.format("%s: start to brush teeth in %d ms", this.toString(), brushTeethDuration));
        Thread.sleep(brushTeethDuration);
        say(String.format("%s: finish brush teeth", this.toString()));
    }

    protected void say(String message) {
        System.out.println(message);
    }

    @Override
    public void run() {

    }
}

class Female extends People {

    public Female(int id, long brushTeethDuration) {
        super(id, brushTeethDuration);
    }

    @Override
    public String toString() {
        return String.format("Female %d", this.id);
    }

    @Override
    public void run() {
        say(String.format("%s wants to enter bathroom.", this.toString()));
        this.bathroom.enterFemale();
        say(String.format("%s entered bathroom.", this.toString()));
        try {
            this.brushTeeth();
        } catch (InterruptedException e) {
            say(String.format("%s: Failed during brushing my teeth", this.toString()));
            e.printStackTrace();
        }
        this.bathroom.leaveFemale();
        say(String.format("%s left bathroom.", this.toString()));

    }
}

class Male extends People {
    public Male(int id, long brushTeethDuration) {
        super(id, brushTeethDuration);
    }

    @Override
    public void run() {
        say(String.format("%s wants to enter bathroom.", this.toString()));
        this.bathroom.enterMale();
        say(String.format("%s entered bathroom.", this.toString()));
        try {
            this.brushTeeth();
        } catch (InterruptedException e) {
            say(String.format("%s: Failed during brushing my teeth", this.toString()));
            e.printStackTrace();
        }
        this.bathroom.leaveMale();
        say(String.format("%s left bathroom.", this.toString()));
    }

    @Override
    public String toString() {
        return String.format("Male %d", this.id);
    }


}