import java.util.Iterator;
import java.util.NoSuchElementException;

//use this to provide framework for other classes
public abstract class CollectionTemplate<T> implements Iterable<T> {

    protected int size;

    public CollectionTemplate() {

        this.size = 0;

    }

    // meant to be overriden
    public Iterator<T> iterator() throws UnsupportedOperationException {

        return new Iterator<>() {

            public boolean hasNext() {
                throw new UnsupportedOperationException();
            }

            public T next() throws NoSuchElementException {

                throw new NoSuchElementException();
            }

        };
    }

    public abstract void add(T element) throws Exception;

    public abstract boolean remove(T element) throws Exception;

    public abstract boolean contains(T element) throws Exception;

    public abstract T get(T element) throws Exception;

    public abstract String toString();

    public int size() {

        return this.size;

    }
}
