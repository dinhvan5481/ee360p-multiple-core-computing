

// Sorted linked list of integers.
public class FineLinkedList implements SortedLinkedList {

    private static class Node {
        int value;
        Node next;

        public Node(int v) {
            value = v;
            next = null;
        }
    }

    final Node root = new Node(Integer.MIN_VALUE);

    public FineLinkedList() {
        root.next = root;
    }

    public boolean add(int x) {
        Node toAdd = new Node(x);
        Node ptr = root;

        while(true) {
            synchronized (ptr) {
                if(ptr.next == null) {
                    ptr = root; // Restart search :(
                }
                if(ptr.next == root || ptr.next.value > x) {
                    Node next = ptr.next;
                    toAdd.next = next;
                    ptr.next = toAdd;
                    return true;
                }
                if(ptr.next.value == x) {
                    return false;
                }
                ptr = ptr.next;
            }
        }
    }

    public boolean remove(int x) {
        Node ptr = root;
        Node nodeToRemove = null;
        while(true) {
            synchronized (ptr.next) {
                if(ptr.next == root || ptr.next.value > x) {
                    return false;
                }
                if(ptr.next.value == x) {
                    nodeToRemove = ptr.next;
                    synchronized (ptr) {
                        ptr.next = nodeToRemove.next;
                    }
                    nodeToRemove.next = null;
                    break;
                }
                ptr = ptr.next;
            }
        }

        return true;
    }

    public boolean contains(int toFind) {
        Node ptr = root;

        while(true) {
            synchronized (ptr) {
                if(ptr.next == null) {
                    ptr = root; // Restart
                }
                if(ptr.next == root) {
                    return false;
                }
                if(ptr.next.value == toFind) {
                    return true;
                }
                ptr = ptr.next;
            }
        }
    }

    public String toString() {
        Node ptr = root.next;
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        while(ptr != root) {
            buffer.append(ptr.value + (ptr.next == root? "":", "));
            ptr = ptr.next;
        }
        buffer.append("]");
        return buffer.toString();
    }
}