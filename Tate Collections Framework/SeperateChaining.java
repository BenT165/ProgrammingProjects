//HashSet that implements seperate chaining
public class SeperateChaining<T> {

    private static final int DEFAULT_CAPACITY = 13;
    private static final float MAX_LOAD = 2.0f;

    private int size;
    private int capacity;
    private SimpleList<T>[] array;

    public SeperateChaining() {

        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public SeperateChaining(int capacity) {

        this.size = 0;

        // ensure capacity is 2 or greater
        this.capacity = capacity > 2 ? capacity : DEFAULT_CAPACITY;
        this.array = (SimpleList<T>[]) new SimpleList[capacity];

    }

    public int size() {

        return this.size;
    }

    public int capacity() {

        return this.capacity;

    }

    public float load() {

        return ((float) (this.size)) / (this.capacity);

    }

    // adds an element to this hashSet, using Seperate Chaining to address
    // collisions
    public boolean add(T element) throws Exception {

        // retrun false if element not in this hashSet
        if (element == null || contains(element))
            return false;

        // if hashSet is at or exceeds max load -> remap elements
        if (load() >= MAX_LOAD)
            reHash();

        // if there is no list at corresponding index -> create list
        if (array[element.hashCode() % capacity] == null)
            array[element.hashCode() % capacity] = new SimpleList<T>();

        // add new element to the end of queue at that index
        array[element.hashCode() % capacity].add(element);
        size++;
        return true;

    }

    @SuppressWarnings("unchecked")
    private void reHash() throws Exception {

        if (Integer.MAX_VALUE / capacity == 1)
            throw new Exception("Hash set is too big to accomodate any more elements");

        // create new hash set with twice the size
        SimpleList<T>[] newArray = (SimpleList<T>[]) new SimpleList[capacity *= 2];

        for (int i = 0; i < array.length; i++) {

            // for each non-empty list in current array
            if (array[i] != null) {

                // rehash all elements in that list to the new array
                for (T data : array[i]) {

                    if (newArray[data.hashCode() % capacity] == null)
                        newArray[data.hashCode() % capacity] = new SimpleList<T>();

                    newArray[data.hashCode() % capacity].add(data);
                }
            }
        }

        // have old array reference new array
        array = newArray;
    }

    public boolean contains(T element) throws Exception {

        if (element == null || size == 0)
            return false;

        // check if the list at the array index corresponding to the hash code of
        // element is null

        // if(array[element.hashCode() % capacity] == null &)

        boolean returnVal = false;

        returnVal = array[element.hashCode() % capacity] == null ? false
                : array[element.hashCode() % capacity].contains(element);
        return returnVal;
    }

    // removes a given element from this hashSet if it is present
    public boolean remove(T element) throws Exception {

        if (!contains(element))
            return false;

        array[element.hashCode() % capacity].remove(element);
        size--;

        return true;
    }

    public T get(T element) throws Exception {

        return array[element.hashCode() % capacity].get(element);
    }

    public SimpleList<T> allValues() throws Exception {

        SimpleList<T> allTheStuff = new SimpleList<>();

        for (int i = 0; i < array.length; i++) {

            // add values from each non null list to a bigger list
            if (array[i] != null) {

                for (T element : array[i])
                    allTheStuff.add(element);

            }
        }

        return allTheStuff;

    }

    public String toString() {

        if (size == 0)
            return "";

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < capacity; i++) {

            if (array[i] != null) {

                str.append("list " + i + ": " + array[i].toString() + "\n");
            }
        }

        return str.toString();

    }

    public static void main(String[] args) throws Exception {

        SeperateChaining<Integer> lhs = new SeperateChaining<Integer>(5);
        lhs.add(5);
        System.out.println(lhs);
        System.out.println(lhs.size());
        System.out.println(lhs.capacity());
        lhs.add(13);
        System.out.println(lhs);
        System.out.println(lhs.size());
        System.out.println(lhs.capacity());

        lhs.add(2);
        lhs.add(3);
        lhs.add(4);
        lhs.add(6);
        lhs.add(7);
        lhs.add(8);
        lhs.add(9);
        lhs.add(10);
        lhs.add(11);

        System.out.println(lhs);
        System.out.println(lhs.size());
        System.out.println(lhs.capacity());
        System.out.println(lhs.allValues());
    }

}
