import java.util.Iterator;

//Sorted Linked List that doesn't allow repeating elements
public class PriorityQueue<T extends Comparable<T>> extends SimpleList<T> {

    // front
    // private Node<T> head;
    // back
    // private Node<T> tail;
    // number of items (non-null)

    public PriorityQueue() {

        super();
    }

    // adds item in order
    public void add(T element) throws Exception {

        // throw Exception if element is null
        if (element == null)
            throw new Exception("Can't add null element");

        // throw Exception if element is already in list
        if (contains(element))
            throw new Exception("Element already in list!");

        // if list is empty -> update head and tail
        else if (size == 0)
            this.head = this.tail = new Node<T>(element);

        else {
            Node<T> newNode = new Node<T>(element); // node being added

            // if node should go before head -> adjust head
            if (newNode.data.compareTo(head.data) < 0) {

                newNode.right = head;
                head.left = newNode;
                head = newNode;
            }
            // if node goes after head-> find out where it should go
            else {

                Node<T> current = this.head; // traversal node

                while (current.right != null && element.compareTo(current.right.data) > 0)
                    current = current.right;

                newNode.right = current.right;
                newNode.left = current;

                if (current.right != null)
                    current.right.left = newNode;
                else
                    tail = newNode;

                current.right = newNode;
            }
        }
        this.size++;
    }

    public DynamicArray<T> toArray() throws Exception {

        DynamicArray<T> priorityArray = new DynamicArray<T>();

        if (head != null) {

            Iterator<T> itr = iterator();
            while (itr.hasNext()) {
                priorityArray.add(itr.next());
            }
        }

        return priorityArray;
    }

    public static void main(String[] args) throws Exception {

        PriorityQueue<Integer> ll = new PriorityQueue<Integer>();

        System.out.println("size is " + ll.size());
        ll.add(10);
        System.out.println(ll);
        System.out.println("size is " + ll.size());
        System.out.println("tail is " + ll.tail.data);
        System.out.println("head is " + ll.tail.data);

        ll.add(8);
        System.out.println(ll);
        System.out.println("size is " + ll.size());
        System.out.println("head is " + ll.head.data);
        System.out.println("tail is " + ll.tail.data);

        ll.add(3);
        System.out.println(ll);
        System.out.println("size is " + ll.size());
        System.out.println("head is " + ll.head.data);
        System.out.println("tail is " + ll.tail.data);

        ll.add(7);
        System.out.println(ll);
        System.out.println("size is " + ll.size());
        System.out.println("head is " + ll.head.data);
        System.out.println("tail is " + ll.tail.data);

        ll.add(13);
        System.out.println(ll);
        System.out.println("size is " + ll.size());
        System.out.println("head is " + ll.head.data);
        System.out.println("tail is " + ll.tail.data);

        System.out.println("\nPriority array is " +
                ll.toArray() + "\n");

        while (ll.size() != 0) {

            if (ll.size() != 0) {
                System.out.println("head is " + ll.head.data);
                System.out.println("tail is " + ll.tail.data);
            }
            System.out.println("size is " + ll.size());
            System.out.println(ll.remove());
        }

        System.out.println("size is " + ll.size());

        // ll.remove(10);
        // System.out.println(ll);
        // System.out.println("size is " + ll.size());
        // System.out.println("head is " + ll.head.data);
        // System.out.println("tail is " + ll.tail.data);
        // ll.remove(13);
        // System.out.println(ll);
        // System.out.println("size is " + ll.size());
        // System.out.println("head is " + ll.head.data);
        // System.out.println("tail is " + ll.tail.data);
        // ll.remove(8);
        // System.out.println(ll);
        // System.out.println("size is " + ll.size());
        // System.out.println("head is " + ll.head.data);
        // System.out.println("tail is " + ll.tail.data);
        // ll.remove(3);
        // System.out.println(ll);
        // System.out.println("size is " + ll.size());
        // System.out.println("head is " + ll.head.data);
        // System.out.println("tail is " + ll.tail.data);
        // ll.remove(7);
        // System.out.println(ll);
        // System.out.println("size is " + ll.size());
        // System.out.println(ll.head == ll.tail);
    }

}
