//Marker used to determine if a value in a hashSet has been silently deleted
public class Marker<T> {

    // data being held by this marker
    private T data;

    // true if active, false if deleted
    private boolean active;

    public Marker() {
        this.active = true;
    }

    public Marker(T data) {
        this.active = true;
        this.data = data;
    }

    public void unsetActive() {

        this.active = false;

    }

    public boolean checkActive() {

        return this.active;
    }

    public T getData() {

        return this.data;
    }

    public int hashCode() {

        return data.hashCode();

    }

    public String toString() {

        String str = checkActive() ? data.toString() : "";
        return str;

    }
}
