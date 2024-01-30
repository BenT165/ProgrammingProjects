public class minHeap<T extends Comparable<T>> {

    private int size;
    private int barrier; // upper bound of the unsorted array
    private T[] array;
    private static int DEFAULT_CAPACITY = 100;

    @SuppressWarnings("unchecked")
    public minHeap() {

        this.size = 0;
        this.barrier = 0;
        this.array = (T[]) new Comparable[DEFAULT_CAPACITY];

    }

    public void add(T data) throws Exception {

        if (data == null)
            throw new Exception("data is null");

        // add data at end and increment size
        array[++size] = data;

        // percolate data up
        percolateUp(size);

        // increment barrier
        barrier++;
    }

    /* moves item backward in the array until it is in correct heap position */
    public void percolateUp(int hole) {

        T temp = array[hole];

        // while hole is not at front of array and array[hole] < child -> swappity swap
        while (hole > 1 && array[hole].compareTo(array[hole / 2]) < 0) {

            temp = array[hole];
            array[hole] = array[hole / 2];
            array[hole / 2] = temp;
            hole /= 2;

        }

    }

    // removes min element from heap
    public void remove() throws Exception {

        /* throw exception if heap is empty */
        if (size <= 0)
            throw new Exception("Can't remove from empty heap!");

        // swap first and last element then remove last element
        array[1] = array[size];
        array[size] = null;

        // update size of the heap
        size--;

        // move the new element at the front of the heap into its proper position
        percolateDown(1);

    }

    public void percolateDown(int hole) {

        T temp = array[hole];
        int child = hole;

        for (; hole * 2 <= barrier; hole = child) {

            child = hole * 2;

            // pick smaller child
            if (child != barrier && array[child].compareTo(array[child + 1]) > 0)
                child++;

            // temp > child -> set parent equal to child
            if (temp.compareTo(array[child]) > 0)
                array[hole] = array[child];

            else
                break;

        }

        // insert temp value at hole
        array[hole] = temp;
    }

    public void heapSort() {

        /* if size is 0, don't sort array */
        if (size == 0)
            return;

        // set lower bound for unsorted array to be 1
        // this.barrier = 1;

        T temp;

        while (barrier != 1) {

            // swap first and last elements of unsorted array
            temp = array[1];
            array[1] = array[barrier];
            array[barrier] = temp;

            // decrement barrier
            barrier--;

            // percolate new first element down
            percolateDown(1);
        }

        barrier = size;

    }

    // sorts the array in ascending order
    public void sortAscending() {

        // first sort the array in descending order
        heapSort();

        T temp;

        for (int i = 1, j = barrier; i < j; i++, j--) {

            temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

    }

    public String toString() {

        if (size == 0)
            return "";

        StringBuilder str = new StringBuilder();

        for (int i = 0; i <= size; i++) {

            if (array[i] != null)
                str.append(array[i] + " ");

        }

        return str.toString();
    }

    @SuppressWarnings("unchecked")
    public void heapify(T[] unsortedArray) throws Exception {

        array = (T[]) new Comparable[unsortedArray.length + 1];

        for (int i = 0; i < unsortedArray.length; i++) {
            add(unsortedArray[i]);

        }

    }

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {

        minHeap<Integer> mh = new minHeap<Integer>();
        // mh.add(62);
        // mh.add(61);
        // mh.add(60);

        // mh.add(5);
        // mh.add(16);

        // System.out.println(mh);

        // mh.add(7);
        // mh.add(70);

        Integer[] intArray = { 62, 61, 60, 5, 16, 7, 70, 85 };

        mh.heapify(intArray);

        System.out.println(mh);

        // mh.heapSort();
        // System.out.println(mh);

        mh.sortAscending();
        System.out.println(mh);
        mh.heapSort();
        System.out.println(mh);

        // mh.remove();
        // System.out.println(mh);
        // mh.remove();
        // System.out.println(mh);
        // mh.remove();
        // System.out.println(mh);
        // mh.add(66);
        // System.out.println(mh);
        // mh.remove();
        // System.out.println(mh);
        // mh.remove();
        // System.out.println(mh);
        // mh.remove();
        // System.out.println(mh);
        // mh.remove();
        // System.out.println(mh);
        // mh.remove();
        // System.out.println(mh);

    }

}