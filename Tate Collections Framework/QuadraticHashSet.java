public class QuadraticHashSet<T> extends LinearHashSet<T> {

    public QuadraticHashSet() {

        super();
    }

    public QuadraticHashSet(int capacity) {

        super(capacity);

    }

    public void add(T element) throws Exception {

        if (element == null || contains(element))
            throw new Exception("Element is null or already in array");

        if (load() >= MAX_LOAD)
            doubleCapacity();

        int probeCount = 0;

        // use quadratic probing to find where element shoud go
        while (array[(element.hashCode() + (probeCount * probeCount)) % capacity] != null)
            probeCount++;

        // add element
        array[(element.hashCode() + (probeCount * probeCount)) % capacity] = new Marker<T>(element);
        size++;

    }

    @SuppressWarnings("unchecked")
    private void doubleCapacity() throws Exception {

        if (Integer.MAX_VALUE / capacity == 1)
            throw new Exception("Array too big to be doubled!");

        Marker<T>[] newArray = (Marker<T>[]) new Marker[capacity *= 2];
        int probeCount = 0;

        for (int i = 0; i < array.length; i++) {

            probeCount = 0;

            // if current array index has an active element
            if (array[i] != null && array[i].checkActive()) {

                // find out where it should go in new array (first non null index found by
                // quadratic probing)
                while (newArray[(array[i].hashCode() + (probeCount * probeCount)) % capacity] != null)
                    probeCount++;

                newArray[(array[i].hashCode() + (probeCount * probeCount)) % capacity] = array[i];
            }
        }

        array = newArray;
    }

    public boolean contains(T element) {

        if (element == null || size == 0)
            return false;

        int probeCount = 0;

        while (this.array[(element.hashCode() + (probeCount * probeCount)) % capacity] != null) {

            if (this.array[(element.hashCode() + (probeCount * probeCount)) % capacity].getData().equals(element)) {

                // return true if an active marker is found with same value as element
                if (this.array[(element.hashCode() + (probeCount * probeCount)) % capacity].checkActive())
                    return array[(element.hashCode() + (probeCount * probeCount)) % capacity].checkActive();

            }
            probeCount++;
        }

        return false;
    }

    public boolean remove(T element) {

        // return false if element is not in hashSet
        if (!contains(element))
            return false;

        int probeCount = 0;

        //
        while (array[(element.hashCode() + (probeCount * probeCount)) % capacity] != null) {

            if (array[(element.hashCode() + (probeCount * probeCount)) % capacity].getData().equals(element)) {
                array[(element.hashCode() + (probeCount * probeCount)) % capacity].unsetActive();
                break;
            }
            probeCount++;
        }

        size--;
        return true;
    }

    // TODO Implement
    public T get(T element) {

        if (!contains(element))
            return null;

        int probeCount = 0;

        while (!array[(element.hashCode() + (probeCount * probeCount)) % capacity].getData().equals(element))
            probeCount++;

        return array[(element.hashCode() + (probeCount * probeCount)) % capacity].getData();
    }

    public static void main(String[] args) throws Exception {

        // LinearHashSet<Integer> lhs = new LinearHashSet<Integer>(8);

        QuadraticHashSet<Integer> lhs = new QuadraticHashSet<Integer>();

        lhs.add(8);
        lhs.add(13);
        lhs.add(23);
        lhs.add(16);
        System.out.println(lhs);
        lhs.add(42);
        System.out.println(lhs);
        lhs.add(29);
        System.out.println(lhs);
        lhs.add(55);
        System.out.println(lhs);
        lhs.add(68);
        System.out.println(lhs);
        System.out.println(

                lhs.get(42));

        // lhs.add(4);
        // System.out.println(lhs);

        lhs.add(15);
        lhs.add(24);
        lhs.add(32);
        System.out.println(lhs.size());
        System.out.println(lhs.capacity());

        lhs.add(42);
        // System.out.println(lhs);

        // lhs.remove(13);
        // lhs.remove(29);
        // lhs.remove(4);
        // lhs.remove(15);
        // lhs.remove(32);
        // lhs.remove(24);
        // lhs.remove(23);
        // lhs.remove(16);
        // lhs.remove(42);
        // lhs.remove(31);
        // lhs.remove(8);

        // System.out.println(lhs);
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
