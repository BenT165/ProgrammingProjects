
//HashSet with linear probing
public class LinearHashSet<T> extends CollectionTemplate<T> {

    // how full the array can get before it must be resized

    protected int capacity;
    protected Marker<T>[] array;

    protected static final float MAX_LOAD = 0.75f;
    private static final int DEFAULT_CAPACITY = 13;

    public LinearHashSet() {

        this(DEFAULT_CAPACITY);

    }

    @SuppressWarnings("unchecked")
    public LinearHashSet(int capacity) {

        super();
        this.capacity = capacity > 2 ? capacity : DEFAULT_CAPACITY;
        this.array = (Marker<T>[]) new Marker[capacity];

    }

    // returns how full the array currently is
    public float load() {

        return ((float) this.size / this.capacity);
    }

    // number of items array can currently store
    public int capacity() {

        return this.capacity;

    }

    // override dynamic array add to use linear probing/ double at load 0.75
    public void add(T element) throws Exception {

        if (element == null || contains(element))
            throw new Exception("Can't add null or repeating element!");

        // if MAX_LOAD is reached -> doubleCapacity
        if (load() >= MAX_LOAD)
            doubleCapacity();

        int probeCount = 0;

        while (array[(element.hashCode() + probeCount) % capacity] != null)
            probeCount++;

        array[(element.hashCode() + probeCount) % capacity] = new Marker<T>(element);
        size++;
    }

    // rehashed all elements in current array to array with double length
    @SuppressWarnings("unchecked")
    private void doubleCapacity() throws Exception {

        // if array is too big to have its size doubled -> don't double size
        if (Integer.MAX_VALUE / this.capacity == 1)
            throw new Exception("Array too big to be doubled!");

        // create a new array with twice current array's capacity
        Marker<T>[] newArray = (Marker<T>[]) new Marker[capacity *= 2];

        // counter for linear probing
        int count = 0;

        for (int i = 0; i < array.length; i++) {

            // reset counter
            count = 0;

            // if current array index has an active element
            if (array[i] != null && array[i].checkActive()) {

                // probe until we find unoccupied index in newArray to move current element
                while (newArray[((array[i].hashCode() + count) % capacity)] != null)
                    count++;

                newArray[(array[i].hashCode() + count) % capacity] = array[i];
            }
        }

        this.array = newArray;
    }

    public boolean contains(T element) {

        if (this.size == 0 || element == null)
            return false;

        int currentIndex = element.hashCode() % capacity;

        // keep linear probing until we hit empty index
        while (array[currentIndex] != null) {

            // break and return if element found
            if (array[currentIndex].getData().equals(element)) {
                return array[currentIndex].checkActive();
            }
            currentIndex = (currentIndex + 1) % capacity;
        }

        return false;
    }

    public String toString() {

        if (size == 0)
            return "";

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < array.length; i++) {

            if (array[i] != null)
                str.append("[" + array[i] + "] ");
            else
                str.append("[] ");
        }

        return str.toString();
    }

    // removes element front the set
    public boolean remove(T element) {

        // return false if element not in array
        if (!contains(element))
            return false;

        int probeCount = 0;

        while (array[(element.hashCode() + probeCount) % capacity] != null) {

            if (array[(element.hashCode() + probeCount) % capacity].getData().equals(element)) {
                array[(element.hashCode() + probeCount) % capacity].unsetActive();
                break;
            }
            probeCount++;
        }

        size--;
        return true;
    }

    // TODO Implement
    public T get(T element) throws Exception {

        if (!contains(element))
            return null;

        int probeCount = 0;

        // linear probe until we find element
        while (!array[(element.hashCode() + probeCount) % capacity].getData().equals(element))
            probeCount++;

        return array[(element.hashCode() + probeCount) % capacity].getData();
    }

    public static void main(String[] args) throws Exception {

        // LinearHashSet<Integer> lhs = new LinearHashSet<Integer>(8);

        LinearHashSet<Integer> lhs = new LinearHashSet<Integer>();

        lhs.add(8);
        lhs.add(13);
        lhs.add(23);
        lhs.add(16);
        lhs.add(31);
        lhs.add(29);

        System.out.println(lhs);

        lhs.add(4);
        System.out.println(lhs);

        lhs.add(15);
        lhs.add(24);
        lhs.add(32);
        System.out.println(lhs.size());
        System.out.println(lhs.capacity());

        lhs.add(42);
        System.out.println(lhs);

        lhs.remove(13);
        lhs.remove(29);
        lhs.remove(4);
        lhs.remove(15);
        lhs.remove(32);
        lhs.remove(24);
        lhs.remove(23);
        lhs.remove(16);
        lhs.remove(42);
        System.out.println(lhs.get(31));
        lhs.remove(31);
        System.out.println(lhs.get(31));
        // lhs.remove(8);

        System.out.println(lhs);
        System.out.println(lhs.size());
        System.out.println(lhs.capacity());

        // lhs.add(3);
        // System.out.println(lhs);
        // System.out.println(lhs.size());
        // System.out.println(lhs.capacity());

        // lhs.add(68);
        // System.out.println(lhs);
        // System.out.println(lhs.size());
        // System.out.println(lhs.capacity());
    }

}