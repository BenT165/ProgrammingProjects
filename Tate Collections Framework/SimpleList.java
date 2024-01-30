import java.util.Iterator;
import java.util.NoSuchElementException;

//double linked list, doesn't allow duplicate values to be added
public class SimpleList<T> extends CollectionTemplate<T> {

    protected Node<T> head;
    protected Node<T> tail;

    // initialize size
    public SimpleList() {

        super();
    }

    public Iterator<T> iterator() {

        return new Iterator<>() {

            private Node<T> current = head;

            public boolean hasNext() {
                return current != null;
            }

            public T next() throws NoSuchElementException {

                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T returnValue = current.data;
                current = current.right;
                return returnValue;
            }

        };
    }

    // adds element to end of list
    public void add(T element) throws Exception {

        if (element == null || contains(element))
            throw new Exception("Element is null or already in list");

        // node to add
        Node<T> newNode = new Node<T>(element);

        // if list is empty -> set and tail to new node
        if (head == null)
            head = newNode;

        // if list is not empty -> add at back
        else {

            // have tail point to new node
            tail.right = newNode;

            // have newNode point back to tail
            newNode.left = tail;

        }
        // update tail to be last node
        tail = newNode;

        size++;
    }

    // removes first node
    public T remove() throws Exception {

        if (this.size == 0)
            throw new Exception("List is empty");

        T removedData = head.data;

        if (size == 1)
            head = tail = null;
        else {
            this.head = head.right;
            head.left = null;
        }
        size--;

        return removedData;
    }

    // removes a node from list
    public boolean remove(T element) throws Exception {

        if (!contains(element))
            return false;

        // if removed node is the head
        if (element.equals(head.data))
            remove();

        // case 2: not front
        else {
            Node<T> current = this.head;

            // adjust pointers starting at predecessor of removedNode
            while (current.right != null && element.equals(current.data))
                current = current.right;

            current.left.right = current.right;

            // removal is at tail -> update tail
            if (current.right == null)
                tail = current.left;
            else
                current.right.left = current.left;
        }

        size--;
        return true;
    }

    // return true if element is in this list, false otherwise
    public boolean contains(T element) throws Exception {

        if (element == null || size == 0)
            return false;

        // make an iterator
        Iterator<T> itr = iterator();

        while (itr.hasNext()) {

            if (itr.next().equals(element))
                return true;
        }

        return false;
    }

    public T get(T element) throws Exception {

        Iterator<T> itr = this.iterator();
        T current = null;

        while (itr.hasNext()) {

            current = itr.next();
            if (current.equals(element))
                break;
        }

        return current;
    }

    public String toString() {

        StringBuilder str = new StringBuilder();
        Iterator<T> itr = this.iterator();

        while (itr.hasNext())
            str.append(itr.next() + " ");

        return str.toString();

    }

    public static void main(String[] args) throws Exception {

        Integer[] foo = { 1, 2, 3, 4, 5, 6, 7, 8 };

        SimpleList<Integer> sl = new SimpleList<>();

        for (int i = 0; i < foo.length; i++)
            sl.add(foo[i]);

        // sl.remove();
        // sl.remove();
        // sl.remove();
        // sl.remove();
        // sl.remove();
        // sl.remove();
        // sl.remove();
        // sl.remove();
        System.out.println(sl);

    }
}
