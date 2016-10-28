/*
* Sorted linked list of integers.
*/
public interface SortedLinkedList {

    /* 
    * returns true if x was not in the list and was added to the list; otherwise, it returns false
    * (and doesn't add it multiple times)
    */
    boolean add(int toAdd);

    /*
    * returns true if x was in the list and was removed from the list; otherwise, it returns false
    */
    boolean remove(int toRemove);

    /*
    * returns true if x is in the list
    */
    boolean contains(int toFind);
    
}