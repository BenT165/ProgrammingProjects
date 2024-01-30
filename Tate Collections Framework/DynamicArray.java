
//unsorted DynamicArray that doesn't allow repeating elements
public class DynamicArray<T> {

    private int size;
    public static final int DEFAULT_CAPACITY = 2;
    private T[] array;

    public DynamicArray() {

        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public DynamicArray(int capacity) {

        this.size = 0;
        this.array = (T[]) new Comparable[capacity];
    }

    public int size() {
        return this.size;
    }

    public int capacity() {

        return this.array.length;
    }

    // adds element to back of array
    // doesn't allow repeating or null elements
    public void add(T element) throws Exception {

        if ((element == null))
            throw new Exception("Can't add item");

        // if array is at capacity -> double capacity
        if (array.length == size)
            doubleCapacity();

        // if array is empty -> put element at index 0
        if (size == 0)
            array[0] = element;

        else {

            array[size] = element;

            // PRIORITY ARRAY CODE
            // T temp = null;
            // // moving backward through the array
            // for (int i = size; i > 0; i--) {

            // // if current element is less than its predecessor
            // if (array[i].compareTo(array[i - 1]) < 0) {
            // // swap the elements
            // temp = array[i];
            // array[i] = array[i - 1];
            // array[i - 1] = temp;
            // }
            // }
        }

        size++;
    }

    @SuppressWarnings("unchecked")
    private void doubleCapacity() throws Exception {

        // throw exception if doubling would cause capacity to exceed max int value
        if ((Integer.MAX_VALUE / array.length) == 1)
            throw new Exception("Can't add any more elements");

        T[] newArray = (T[]) new Comparable[array.length * 2];

        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i];

        array = newArray;
    }

    // 0(N) checks if given element is in array
    public boolean contains(T element) {

        if (size == 0 || element == null)
            return false;

        for (int i = 0; i < size; i++) {

            if (array[i].equals(element))
                return true;
        }
        return false;
    }

    // removes an element from the array and returns it
    public T remove(T element) {

        // check if element is in array
        if (!contains(element))
            return null;

        T removedItem = null;

        for (int i = 0; i < size; i++) {

            // if we find element we want to remove -> store it, remove it
            if (array[i].equals(element)) {
                removedItem = array[i];
                array[i] = null;
                size--;
            }

            // fill gaps made by removed element
            if (removedItem != null && size > 0)
                array[i] = array[i + 1];
        }

        // resize if size becomes equal to half of capacity
        if (size == capacity() / 2 && capacity() > DEFAULT_CAPACITY)
            resize();

        return removedItem;
    }

    @SuppressWarnings("unchecked")
    public void resize() {

        T[] newArray = (T[]) new Comparable[capacity() / 2];

        for (int i = 0; i < array.length / 2; i++)
            newArray[i] = array[i];

        array = newArray;
    }

    public String toString() {

        if (size == 0)
            return "";

        StringBuilder str = new StringBuilder();

        for (int i = 0; i < size; i++)
            str.append(array[i].toString() + " ");

        return str.toString();
    }

    public SimpleList<T> toList() throws Exception {

        SimpleList<T> list = new SimpleList<T>();

        for (int i = 0; i < size; i++)
            list.add(array[i]);

        return list;
    }

    // TODO
    // update HeapSort to work with generics -> construct a heap from this array ->
    // call heapSort -> build a new array for this from sorted heap
    public void sort() throws Exception {

    }

    public static void main(String[] args) throws Exception {

        DynamicArray<Integer> array = new DynamicArray<Integer>();

        array.add(10);
        System.out.println(array.capacity());
        array.add(8);
        System.out.println(array.capacity());
        array.add(3);
        System.out.println(array.capacity());
        array.add(7);
        System.out.println(array.capacity());
        array.add(13);
        array.add(13);
        System.out.println(array.capacity());

        // System.out.println("List is " + array.toList());
        // System.out.println("List size is " + array.toList().size);

        System.out.println(array);
        System.out.println(array.capacity());
        array.remove(10);
        System.out.println(array);
        System.out.println(array.capacity());
        array.remove(3);
        System.out.println(array);
        System.out.println(array.capacity());
        array.remove(13);
        System.out.println(array);
        System.out.println(array.capacity());
        array.remove(7);
        System.out.println(array);
        System.out.println(array.capacity());
        array.remove(8);
        System.out.println

        (array.toString());
        System.out.println(array.capacity());
        System.out.println(array.size());
    }
}