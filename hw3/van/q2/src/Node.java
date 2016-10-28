/**
 * Created by quachv on 10/27/2016.
 */
class Node {
    private int data;
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

    public Node getNext() {
        return this.next;
    }

}
