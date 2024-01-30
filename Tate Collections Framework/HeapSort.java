
//heap sort with max heap
public class HeapSort<T extends Comparable<T>> {

    int size;
    int upperBound; // determines how much of the array is already sorted
    T[] array;

    @SuppressWarnings("unchecked")
    public HeapSort() {

        this.size = 0;
        this.upperBound = 0;
        this.array = (T[]) new Comparable[10];
    }

    // heap sort
    // step 1: swap first and last elements
    // step 2: decrement lower bound
    // step 3 percolate first element down
    public void heapSort() {

        // if there are 1 or 0 items in heap, no need to sort!
        if (upperBound <= 1)
            return;

        T temp = null;

        // NOTE lower bound is 1

        // while there are still items left to be sorted
        while (upperBound > 1) {

            // swap first and last elements in unsorted array
            temp = array[upperBound];
            array[upperBound] = array[1];
            array[1] = temp;

            // decrement upper bound of sorted array
            upperBound--;

            // move max element in unsorted array to first index
            percolateDown(1);
        }

        upperBound = size;
    }

    public boolean add(T data) {

        if (data == null)
            return false;

        if (size == array.length - 1 && !resize())
            return false;

        // add element end of array
        array[++size] = data;
        upperBound = size;

        // percolate it up into proper position
        percolateUp(size);

        return true;
    }

    @SuppressWarnings("unchecked")
    public boolean resize() {

        if ((Integer.MAX_VALUE / array.length) == 1)
            return false;

        T[] newArray = (T[]) new Comparable[array.length * 2];

        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i];

        array = newArray;
        return true;
    }

    public void percolateUp(int hole) {

        // temp value for swapping
        T temp = array[hole];

        // go backwards through array
        for (; hole != 1 && array[hole].compareTo(array[hole / 2]) > 0; hole /= 2) {

            // if current item is bigger than its parent, swap the two
            temp = array[hole];
            array[hole] = array[hole / 2];
            array[hole / 2] = temp;
        }

    }

    public void percolateDown(int hole) {

        // temp value to keep track of value we are moving
        T temp = array[hole];
        int child = hole;

        for (; hole * 2 <= upperBound; hole = child) {

            // set child
            child = hole * 2;

            // pick greater child
            if (child != upperBound && (array[child + 1].compareTo(array[child]) > 0))
                child++;

            // set parent equal to child if child > value we are moving
            if (array[child].compareTo(temp) > 0) {

                array[hole] = array[child];
            }

            else {
                break;
            }
        }
        // put removed value into correct position
        array[hole] = temp;
    }

    // remove max item from heap
    public boolean remove() {

        if (size == 0)
            return false;
        // put last element at first heap index
        array[1] = array[size--];
        upperBound = size;

        // move element at array index 1 into the correct heap position
        percolateDown(1);
        return true;

    }

    @SuppressWarnings("unchecked")
    public void heapify(T[] unsortedArray) {

        // create new array that is one larger
        array = (T[]) new Comparable[unsortedArray.length + 1];

        for (int i = 0; i < unsortedArray.length; i++) {

            add(unsortedArray[i]);
        }
    }

    public String toString() {

        StringBuilder myString = new StringBuilder();
        for (int i = 0; i <= size; i++)
            myString.append(array[i] + " ");

        return myString.toString();

    }

    public static void main(String[] args) {

        Integer[] unsortedNums = { 69, 82, 13, 43, 420, 10, 6, 7, 15, 5, 17, 26 }; // array of random

        HeapSort<Integer> hs = new HeapSort<Integer>();

        // for (int i = 0; i < unsortedNums.length; i++)
        // hs.add(unsortedNums[i]);

        hs.heapify(unsortedNums);
        System.out.println(hs);
        hs.heapSort();
        System.out.println(hs);

    }

    // 420
    // 82 //26
    // 43 69 //13 6
}