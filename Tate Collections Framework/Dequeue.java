//Make this double ended Queue
public class Dequeue<T> extends SimpleList<T> {

    // constructor
    public Dequeue() {

        super();
    }

    // adds element to the end of queue
    public boolean addLast(T element) throws Exception {

        if (element == null)
            return false;

        // node to add
        Node<T> newNode = new Node<T>(element);

        // if queue is empty -> set and tail to new node
        if (head == null)
            head = newNode;

        // if queue is not empty -> add at back
        else {

            // have tail point to new node
            tail.right = newNode;

            // have newNode point back to tail
            newNode.left = tail;
        }
        // update tail to be last node
        tail = newNode;

        size++;
        return true;
    }

    // removes and returns elements at the front of queue
    public T removeFirst() throws Exception {

        if (size == 0)
            throw new Exception("Queue is empty");

        return super.remove();
    }

    // Override parent classes' add method to add at front
    public boolean addFirst(T element) throws Exception {

        // don't do anything if element is null
        if (element == null)
            return false;

        // create node to add
        Node<T> newNode = new Node<T>(element);

        // size is 0 -> update head tail
        if (size == 0)
            head = tail = newNode;

        else {
            // newNode points to head
            newNode.right = head;

            // head points back to newNode
            head.left = newNode;

            // have head reference newNode
            head = newNode;
        }

        size++; // increment size
        return true;
    }

    // removes and returns element at the end of the queue
    public T removeLast() throws Exception {

        // throw an exception for empty queue
        if (size == 0)
            throw new Exception("Stack is empty");

        T removedElement = tail.data;

        if (size == 1)
            head = tail = null;

        else {
            tail = tail.left;
            tail.right = null;
        }
        size--;

        return removedElement;
    }

    // returns element at front of list
    public T peekFirst() throws Exception {

        if (size == 0)
            throw new Exception("Queue is empty");

        return this.head.data;
    }

    // returns element at end of list
    public T peekLast() throws Exception {

        if (size == 0)
            throw new Exception("Queue is empty");

        return this.tail.data;
    }

    public void add(T element) throws Exception {

        throw new Exception("Unsupported Operation");
    }

    public boolean remove(T element) throws Exception {

        throw new Exception("Unsupported Operation");
    }

    public boolean contains(T element) throws Exception {

        throw new Exception("Unsupported Operation");
    }

    public T get(T element) throws Exception {

        throw new Exception("Unsupported Operation");
    }

    public static void main(String[] args) throws Exception {

        Dequeue<Float> myStack = new Dequeue<Float>();

        myStack.addFirst(3.14f);
        System.out.println("Queue: " + myStack);
        System.out.println("Size: " + myStack.size());

        myStack.addFirst(69.69f);
        System.out.println("Queue: " + myStack);
        System.out.println("Size: " + myStack.size());

        myStack.addFirst(4.20f);
        System.out.println("Queue: " + myStack);
        System.out.println("Size: " + myStack.size());

        myStack.addFirst(8.88f);
        System.out.println("Queue: " + myStack);
        System.out.println("Size: " + myStack.size());

        myStack.addFirst(80085f);
        System.out.println("Queue: " + myStack);
        System.out.println("Size: " + myStack.size());

        System.out.println("----------------------------------------------------------------------");

        while (myStack.size() != 0) {

            System.out.println("Queue: " + myStack);
            System.out.println("Size: " + myStack.size());
            System.out.println("Head: " + myStack.head.data);
            System.out.println("Tail: " + myStack.tail.data);
            System.out.println(myStack.removeLast());
        }

        System.out.println(myStack.size());
    }

}
