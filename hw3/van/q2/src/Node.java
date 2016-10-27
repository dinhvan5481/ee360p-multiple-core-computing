/**
 * Created by quachv on 10/27/2016.
 */
class Node {
    private int data;
    private Node prev;
    private Node next;

    public Node() {

    }
    public Node(int data) {
        this.data = data;
    }

    public int getData() {
        return this.data;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return this.next;
    }

    public Node getPrev() {
        return this.prev;
    }
}
