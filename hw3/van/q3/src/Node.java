/**
 * Created by quachv on 11/2/2016.
 */
class Node {
    int value;
    private Node next;

    public Node() {}

    public Node(int value) {
        this.value = value;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Node getNext() {
        return this.next;
    }

    public int getValue() {
        return this.value;
    }
}
