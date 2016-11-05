import java.lang.StringBuffer;

// Sorted linked list of integers.
public class CoarseGrainedListSet implements ListSet {

    private static class Node {
        int value;
        Node next;

        public Node(int v) {
            value = v;
            next = null;
        }
    }

    final Node root = new Node(Integer.MIN_VALUE);

    public CoarseGrainedListSet() {
        root.next = root;
    }

    public boolean add(int x) {
        Node toAdd = new Node(x);
        
        synchronized (root) {
            Node ptr = root;
            while(ptr.next != root && ptr.next.value < x) {
                ptr = ptr.next;
            }

            if (ptr.next == root || ptr.next.value != x) {
                Node next = ptr.next;
                toAdd.next = next;
                ptr.next = toAdd;    
                return true;
            }

            return false;
        }
    }

    public boolean remove(int toRemove) {
        synchronized (root) {
            Node ptr = root;
            while(ptr.next != root && ptr.next.value != toRemove) {
                ptr = ptr.next;
            }

            if(ptr.next == root) {
                return false;
            } else {
                Node nodeToRemove = ptr.next;
                ptr.next = nodeToRemove.next;
                nodeToRemove.next = null; // GC'ed!
                return true;
            }
        }
    }

    public boolean contains(int toFind){
        synchronized (root) {
            Node ptr = root;
            while(ptr.next != root && ptr.next.value != toFind) {
                ptr = ptr.next;
            }

            return ptr.next != root;
        }
    }

    public String toString() {
        synchronized (root) {
            StringBuffer buffer = new StringBuffer();
            Node ptr = root.next;
            buffer.append("[");
            while(ptr != root) {
                buffer.append(ptr.value + (ptr.next == root? "":", "));
                ptr = ptr.next;
            }
            buffer.append("]");
            return buffer.toString();
        }
    }
}