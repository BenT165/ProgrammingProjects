/* private node class */
public class Node<T> {

    protected T data;
    Node<T> left;
    Node<T> right;

    public Node() {
    }

    public Node(T data) {

        this.data = data;

    }
}