// Q.4 (20 points) An old bridge has only one lane and can 
// only hold at most 4 cars at a time without risking collapse. 
// Create a monitor with methods arriveBridge(int direction) 
// and exitBridge() that controls traffic so that at any given time, 
// there are at most 4 cars on the bridge, and all of them are 
// going the same direction. A car calls arriveBridge when it 
// arrives at the bridge and wants to go in the specified 
// direction (0 or 1); arriveBridge should not return until the car 
// is allowed to get on the bridge. A car calls exitBridge when it 
// gets off the bridge, potentially allowing other cars to get on. 
// Don’t worry about starving cars trying to go in one direction; 
// just make sure cars are always on the bridge when they can be. 
// You must use Java ReentrantLock and Condition variables. 
// Ensure that you signal only the appropriate cars when you exit 
// the bridge.

public class Bridge {

    public void arriveBridge(int direction) {
        // TODO
    }

    public void exitBridge() {
        // TODO
    }
}