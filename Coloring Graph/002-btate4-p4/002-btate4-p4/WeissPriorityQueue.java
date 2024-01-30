import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.HashMap;

/**
 * PriorityQueue class implemented via the binary heap.
 * From your textbook (Weiss)
 * 
 * @author btate4/GTAs/professors
 * @param <T> an Object of any type.
 */
public class WeissPriorityQueue<T> extends WeissAbstractCollection<T> {

    /** The default size of the heap. **/
    private static final int DEFAULT_CAPACITY = 100;

    /** The number of elements in this heap. **/
    private int currentSize;

    /** An array representing this heap. **/
    private T[] array;

    /** A Comparator used for ordering heap elements. **/
    private Comparator<? super T> cmp;

    /**
     * A HashMap that maps heap elements to their respective indices in the array.
     **/
    private HashMap<T, Integer> indexMap;

    /**
     * Retrieves the array index for an element in the heap.
     * O(1)
     * 
     * @param x The element for which the heap index is being retrieved.
     * @return The index of an element in the heap or -1 if the element is not in
     *         the heap.
     **/
    public int getIndex(T x) {
        // average case O(1)

        /* if x is in the HashMaps */
        if (indexMap.containsKey(x)) {

            return indexMap.get(x).intValue(); /* try to retrieve x's Integer index from the HashMap */
        }

        return -1; // return -1 if x is not in the heap
    }

    /**
     * Moves an Object to its proper position in the heap to reflect changes to the
     * Object's existing fields.
     * The Value pairing for that Object in IndexMap is also updated to represent
     * the Object's updated array index.
     * O(lg n) average case
     * O(lg n) worst case if getIndex() is guarenteed O
     *
     * @param x The key to which an updated value will be mapped.
     * @return True if x is in the HashMap containing index mappings for the heap,
     *         false otherwise.
     **/
    public boolean update(T x) {

        if (!indexMap.containsKey(x))
            return false; /* return false if x is not in the HashMap */

        // Step1 Find out do we percolate x up or down?

        /*
         * true if we should percolate up, false if we should percolate down. We
         * percolate down by default (iff x is at array index 1)
         */
        boolean upDown = false;

        /*
         * x's index in the array (set to 1 by default because trees start at index 1 of
         * the array)
         */
        int arrayIndex = 1;

        if (indexMap.get(x) > 1) {
            arrayIndex = indexMap.remove(x); /* remove any items that are 'equal' to x from indexMap */
            indexMap.put(x, arrayIndex); /* map x to index value of the removed item */
            array[arrayIndex] = x; /* update array element at arrayIndex to be x */

            // percolate up if x < parant, percolate down if x >= parent
            upDown = compare(array[indexMap.get(x) / 2], x) > 0;
        }

        // Step2 Percolate x up or down in the array and get x's updated array index
        if (upDown)
            percolateUp(indexMap.get(x));
        else
            percolateDown(indexMap.get(x));

        arrayIndex = indexMap.get(x);

        return true;
    }

    /**
     * Construct an empty PriorityQueue.
     *
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue() {
        this.currentSize = 0;
        this.cmp = null;
        this.array = (T[]) new Object[DEFAULT_CAPACITY + 1];
        this.indexMap = new HashMap();

    }

    /**
     * Construct an empty PriorityQueue with a specified comparator.
     * 
     * @param c A Comparator used to order objects in the heap.
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue(Comparator<? super T> c) {
        this.currentSize = 0;
        this.cmp = c;
        this.array = (T[]) new Object[DEFAULT_CAPACITY + 1];
        this.indexMap = new HashMap();
    }

    /**
     * Construct a PriorityQueue from another Collection.
     * 
     * @param coll A Collection of generic Objects from which to build this heap.
     */
    @SuppressWarnings("unchecked")
    public WeissPriorityQueue(WeissCollection<? extends T> coll) {
        cmp = null;
        currentSize = coll.size();
        array = (T[]) new Oxbject[(currentSize + 2) * 11 / 10];
        this.indexMap = new HashMap();

        int i = 1;
        for (T item : coll)
            array[i++] = item;
        buildHeap();

    }

    /**
     * Compares lhs and rhs using comparator if
     * provided by cmp, or the default comparator.
     * 
     * @param lhs The first object being compared.
     * @param rhs The object the first object is being compared to.
     * @return a negative integer if lhs should come before rhs,
     *         0 if lhs side is equal to rhs,
     *         and a positive integer if lhs should come after rhs
     */
    @SuppressWarnings("unchecked")
    private int compare(T lhs, T rhs) {
        if (cmp == null)
            return ((Comparable) lhs).compareTo(rhs);
        else
            return cmp.compare(lhs, rhs);
    }

    /**
     * Internal method to percolate up in the heap.
     * Uses an array implementation of a tree to figure out where to put the
     * element.
     * O(log(N))
     * 
     * @param hole The last index of the array containing an element
     */
    private void percolateUp(int hole) {
        int parent; /* the index of tmp's parent */
        T tmp = array[hole]; /* the value that we are percolating up */

        /*
         * start from the last index of the array moving towards the first index of the
         * array
         */
        for (; (hole / 2) >= 1; hole = parent) {

            /* move up a level in the tree */
            parent = hole / 2;

            /*
             * if parent is lower priority than tmp--> swap parent with
             * element at our current position in heap
             */
            if (compare(array[parent], tmp) > 0) {

                // update the index for the value that was moved down in the indexMap
                array[hole] = array[parent];
                indexMap.put(array[hole], hole);
            }

            else
                break;
        }

        // set element at destination index (index we percolated up to) to tmp
        array[hole] = tmp;

        // update Hashmap value of tmp to be same as its updated array index
        indexMap.put(tmp, hole);
    }

    /**
     * Adds an item to this PriorityQueue.
     * O(N) where N is the number of elements in the Queue
     * 
     * @param x any object.
     * @return true.
     * @throws IllegalArgumentException if x is null or already in indexMap
     */
    public boolean add(T x) {

        if (x == null || indexMap.containsKey(x))
            throw new IllegalArgumentException("Error!  Element is null or already in indexMap");

        if (currentSize + 1 == array.length)
            doubleArray();

        /* append x to array */
        int hole = ++currentSize;
        array[0] = x;

        // Percolate up
        for (; compare(x, array[hole / 2]) < 0; hole /= 2) {

            // if current item is higher priority than its parent --> move it backward in
            // the heap (towards index 1)
            array[hole] = array[hole / 2];
            // update hashMap so x is paired with its array index
            indexMap.put(array[hole], hole);
        }

        // put x at the proper array index
        array[hole] = x;

        /* update hashMap so x is paired with its array index */
        indexMap.put(x, hole);

        /* update the highest priority item in the heap */
        update(array[1]);

        // make sure zero index is empty
        array[0] = null;

        return true;
    }

    /**
     * Returns the number of items in this PriorityQueue.
     * 
     * @return the number of items in this PriorityQueue.
     */
    public int size() {
        return currentSize;
    }

    /**
     * Make this PriorityQueue empty.
     */
    public void clear() {
        currentSize = 0;

    }

    /**
     * Returns an iterator over the elements in this PriorityQueue.
     * The iterator does not view the elements in any particular order.
     * 
     * @return An Iterator that can be used to efficiently traverse this heap.
     */
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int current = 0;

            /**
             * Determines if there are more elements in this Queue to traverse.
             * 
             * @return true if there are more elements in the heap, false otherwise
             **/
            public boolean hasNext() {
                return current != size();
            }

            /**
             * Get the next element in the heap.
             * 
             * @return The object at the next heap index.
             * @throws NoSuchElementException
             **/
            @SuppressWarnings("unchecked")
            public T next() {
                if (hasNext())
                    return array[++current];
                else
                    throw new NoSuchElementException();
            }

            /**
             * Not intended to be used, this method is included to fulfill the interface
             * contract.
             * 
             * @throws UnsupportedOperationException if method is called
             **/
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Returns the smallest item in the priority queue.
     * 
     * @return the smallest item.
     * @throws NoSuchElementException if empty.
     */
    public T element() {
        if (isEmpty())
            throw new NoSuchElementException();
        return array[1];
    }

    /**
     * Removes the smallest item in the priority queue.
     * O(N) where N is the number of elements in the Priority Queue.
     * 
     * @return the smallest item.
     * @throws NoSuchElementException if empty.
     */
    public T remove() {

        if (currentSize == 0 || indexMap.size() == 0)

            // throw exception if queue is empty
            throw new NoSuchElementException("Can't remove from an empty queue!");

        /* save min item for return */
        T minItem = element();

        /* override minitem with the element at the last array index */
        array[1] = array[currentSize--];

        // make sure element at first index is highest priority element
        percolateDown(1);

        // remove the min item from indexMap to represent that it was deleted from the
        // array
        indexMap.remove(minItem, indexMap.get(minItem));
        return minItem;
    }

    /**
     * Establish heap order property from an arbitrary
     * arrangement of items. Runs in linear time.
     */
    private void buildHeap() {
        for (int i = currentSize / 2; i > 0; i--)
            percolateDown(i);
    }

    /**
     * Internal method to percolate down in the heap.
     * O(logN) where N is the number of items in the heap
     * 
     * @param hole the index at which the percolate begins.
     */
    private void percolateDown(int hole) {

        int child;
        T tmp = array[hole];

        for (; hole * 2 <= currentSize; hole = child) {
            child = hole * 2;
            if (child != currentSize && compare(array[child + 1], array[child]) < 0)
                child++;
            if (compare(array[child], tmp) < 0) {
                array[hole] = array[child];

                // map the object that was swapped up (towards index 1) in the heap to its new
                // index
                indexMap.put(array[hole], hole);
            } else
                break;
        }
        array[hole] = tmp;

        // after we are done percolating down, tell the object we moved what its new
        indexMap.put(tmp, hole);

    }

    /**
     * Internal method to extend array.
     */
    @SuppressWarnings("unchecked")
    private void doubleArray() {
        T[] newArray;

        newArray = (T[]) new Object[array.length * 2];
        for (int i = 0; i < array.length; i++)
            newArray[i] = array[i];
        array = newArray;
    }

}
