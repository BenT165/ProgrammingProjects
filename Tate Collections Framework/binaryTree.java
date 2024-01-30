import java.util.NoSuchElementException;

public class binaryTree<T extends Comparable<T>> {

    private int size;
    private Node<T> root;

    /* Class constructor */
    public binaryTree() {

        this.size = 0;
        this.root = null;

    }

    // returns true if element can be added, false otherwise
    public boolean insert(T data) {

        if (data == null)
            return false;

        if (size == 0)
            this.root = insert(data, this.root);
        else
            insert(data, this.root);

        size++;
        return true;
    }

    protected Node<T> insert(T data, Node<T> current) throws IllegalArgumentException {

        // base case: we hit a null node --> insert and increment bst size
        if (current == null) {
            current = new Node<T>(data);
        }

        // left traverse -> set left pointer from level above
        else if (data.compareTo(current.data) < 0)
            current.left = insert(data, current.left);

        // right traverse -> set right pointer from level above
        else if (data.compareTo(current.data) > 0)
            current.right = insert(data, current.right);

        else
            throw new IllegalArgumentException("data already in tree");
        return current;
    }

    public boolean remove(T data) {

        if (data == null || !contains(data))
            return false;

        // if removed node is root and root has no right child
        else if (root != null && data == root.data && root.right == null) {
            root = root.left;
            size--;
            return true;
        }

        remove(data, this.root);

        return true;

    }

    // successor replacement
    public Node<T> remove(T data, Node<T> current) throws NoSuchElementException {

        // base case 1: data not in tree
        if (current == null)
            throw new NoSuchElementException("Data not in tree");

        // set left link from level above
        else if (data.compareTo(current.data) < 0)
            current.left = remove(data, current.left);

        // set right link from level above
        else if (data.compareTo(current.data) > 0)
            current.right = remove(data, current.right);

        // base case 4: removed node has 2 children
        else if (current.left != null && current.right != null) {

            // put correct value at node being removed
            current.data = findMin(current.right).data;

            current.right = removeMin(current.right);
        }

        // base case 2-3: removed node has 1 or no children
        else {
            current = current.left != null ? current.left : current.right;
            size--;
        }

        return current;

    }

    // returns min Node in subtree starting at current
    private Node<T> findMin(Node<T> current) throws NoSuchElementException {

        // if tree is empty throw exception
        if (current == null && size == 0)
            throw new NoSuchElementException("no min found");

        Node<T> currentNode = current;

        while (currentNode.left != null)
            currentNode = currentNode.left;

        return currentNode;

    }

    // removes min Node in subtree starting at given root
    // returns root of subtree
    public Node<T> removeMin(Node<T> current) throws NoSuchElementException {

        // base case: current node is null or root was not in tree
        if (current == null)
            throw new NoSuchElementException("Node couldn't be removed");

        // go left until we hit node we want to remove
        else if (current.left != null)
            current.left = removeMin(current.left);

        // when we hit node we want to remove -> set that node equal to its right child
        // (in case the node we are removing has a right branch-> we want to maintain

        else {
            current = current.right;
            size--;
        }
        return current;
    }

    // checks if a data item is in the tree
    public boolean contains(T data) {

        return contains(this.root, data);

    }

    private boolean contains(Node<T> current, T data) {

        // base case 1; not in tree
        if (current == null) {
            return false;
        } else if (data.compareTo(current.data) < 0)
            contains(current.left, data);

        else if (data.compareTo(current.data) >= 0)
            contains(current.right, data);

        return true;
    }

    // main method
    public void printInOrder() {

        StringBuilder myString = new StringBuilder();
        printInOrder(this.root, myString);
        System.out.println(myString);
    }

    // helper
    private void printInOrder(Node<T> current, StringBuilder str) {

        // base case
        if (current == null)
            return;

        printInOrder(current.left, str);
        str.append(current.data + " ");
        printInOrder(current.right, str);

    }

    // main method
    public void printPostOrder() {

        StringBuilder str = new StringBuilder(); // empty stringbuilder to pass (thing we're changing)
        printPostOrder(this.root, str);
        System.out.println(str.toString());

    }

    // left right root
    // starting point, current string
    private void printPostOrder(Node<T> currentNode, StringBuilder str) {

        if (currentNode == null)
            return;

        printPostOrder(currentNode.left, str);
        printPostOrder(currentNode.right, str);
        str.append(currentNode.data + " ");

    }

    public void printPreOrder() {

        StringBuilder str = new StringBuilder();
        printPreOrder(this.root, str);
        System.out.println(str);
    }

    public void printPreOrder(Node<T> currentNode, StringBuilder str) {

        if (currentNode == null)
            return;

        str.append(currentNode.data + " ");
        printPreOrder(currentNode.left, str);
        printPreOrder(currentNode.right, str);
    }

    public void levelOrderTraversal() throws Exception {

        StringBuilder str = new StringBuilder();
        Dequeue<Node<T>> queue = new Dequeue<>(); // queue of tree nodes
        queue.addLast(root);
        levelOrderTraversal(queue, str);
        System.out.println(str);

    }

    public void levelOrderTraversal(Dequeue<Node<T>> queue, StringBuilder str) throws Exception {

        if (queue.size() == 0)
            return;

        // append data from first node in queue to stringbuilder
        str.append(queue.peekFirst().data + " ");

        // add left and right children of currentNode to the queue
        queue.addLast(queue.peekFirst().left);
        queue.addLast(queue.peekFirst().right);
        queue.removeFirst(); // dequeue current node
        levelOrderTraversal(queue, str); // recursive call
    }

    // pre -> root first -> root left right
    // in -> root middle -> left root right
    // post -> root last -> left righ root

    public static void main(String[] args) throws Exception {

        binaryTree<String> bst = new binaryTree<String>();
        // Node<String> bstRoot = bst.root;

        bst.insert("Mohsin");
        bst.insert("Ben");
        bst.insert("Abe");
        bst.insert("Tree");
        bst.insert("Nina");
        bst.insert("Nikhil");
        bst.insert("Marisa");
        bst.insert("Wynter");

        System.out.println(bst.contains("Mohsin"));
        System.out.println(bst.size);

        bst.printInOrder();
        bst.printPostOrder();
        bst.printPreOrder();
        bst.levelOrderTraversal();

        bst.remove(bst.root.data);
        bst.printInOrder();
        bst.remove(bst.root.data);
        bst.printInOrder();
        bst.remove(bst.root.data);
        bst.printInOrder();
        bst.remove(bst.root.data);
        bst.printInOrder();
        bst.remove(bst.root.data);
        bst.printInOrder();
        bst.remove(bst.root.data);
        bst.printInOrder();
        bst.remove(bst.root.data);
        bst.printInOrder();
        bst.remove(bst.root.data);
        bst.printInOrder();

        System.out.println(bst.size);
        // System.out.println(bst.preOrder());
        // bst.remove("Marisa");
        // System.out.println(bst.preOrder());

        // System.out.println(bst.toString());
        // System.out.println(bst.findMin(bst.root));
        // System.out.println(bst.preOrder());
        // System.out.println(bst.contains("Harry"));
        // System.out.println(bst.contains("Ben"));
        // System.out.println(bst.findMin(bst.root));

        // System.out.println(bst.findMin(bst.root));
        // System.out.println(bst.preOrder());
        // bst.removeMin(bst.root);
        // System.out.println(bst.findMin(bst.root));
        // System.out.println(bst.preOrder());
        // bst.removeMin(bst.root);
        // System.out.println(bst.findMin(bst.root));
        // System.out.println(bst.preOrder());
        // bst.removeMin(bst.root);
        // System.out.println(bst.findMin(bst.root));
        // System.out.println(bst.preOrder());

        // bst.removeMin(bst.root);
        // System.out.println(bst.preOrder());
        // bst.findMin(bst.root);
        // System.out.println(bst.findMin(bst.root));
        // System.out.println(bst.preOrder());
        // bst.findMin(bst.root);
        // bst.remove("Marisa");

        // System.out.println(bst.toString());
        // System.out.println(findMin(bst.getRoot()));
    }

}
