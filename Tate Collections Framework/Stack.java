//Stack Implementation of Linked List (removals at front and back)
public class Stack<T> extends SimpleList<T> {

    // class constructor
    public Stack() {
        super();
    }

    // Override parent classes' add method to add at front and allowing repeating
    // elements
    public void add(T element) throws Exception {

        // throw Exception if element is null
        if (element == null)
            throw new Exception("Can't add null element");

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
    }

    // returns top element in the stack
    public T peek() throws Exception {

        if (size == 0)
            throw new Exception("Stack is empty");

        return this.head.data;
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

        Stack<Float> myStack = new Stack<Float>();

        myStack.add(3.14f);
        System.out.println("Stack: " + myStack);
        System.out.println("Size: " + myStack.size());

        myStack.add(69.69f);
        System.out.println("Stack: " + myStack);
        System.out.println("Size: " + myStack.size());

        myStack.add(4.20f);
        System.out.println("Stack: " + myStack);
        System.out.println("Size: " + myStack.size());

        myStack.add(8.88f);
        System.out.println("Stack: " + myStack);
        System.out.println("Size: " + myStack.size());

        myStack.add(80085f);
        System.out.println("Stack: " + myStack);
        System.out.println("Size: " + myStack.size());

        System.out.println("----------------------------------------------------------------------");

        while (myStack.size() != 0) {

            System.out.println("Stack: " + myStack);
            System.out.println("Size: " + myStack.size());
            System.out.println("Head: " + myStack.head.data);
            System.out.println("Tail: " + myStack.tail.data);
            System.out.println(myStack.remove());
        }
    }

}
