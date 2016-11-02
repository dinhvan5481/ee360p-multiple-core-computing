/**
 * Created by quachv on 10/27/2016.
 */
class Node {
    private int data;
    private boolean isDeleted;
    private Node next;

    public Node() {
        this.next = null;
        this.isDeleted = false;
    }
    public Node(int data) {
        this.data = data;
        this.next = null;
        this.isDeleted = false;
    }

    public int getData() {
        return this.data;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return this.next;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsDeleted() {
        return this.isDeleted;
    }
}
