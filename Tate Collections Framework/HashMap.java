
public class HashMap<K, V> {

    private SeperateChaining<Pair<K, V>> hashSet;

    private static class Pair<K, V> {

        K key;
        V value;

        public Pair(K key, V value) {

            this.key = key;
            this.value = value;
        }

        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {

            // if the object is an instance of Pair -> return if they are equal
            if (obj instanceof Pair) {
                Pair<K, V> pair = (Pair<K, V>) obj;
                return key.equals(pair.key);
            }
            return false;
        }

        public int hashCode() {

            return key.hashCode();
        }

        public String toString() {

            return "<" + key + "," + value + ">";

        }

        public K getKey() {

            return this.key;
        }

        public V getValue() {

            return this.value;
        }
    }

    // constructor with length
    public HashMap(int len) {

        this.hashSet = new SeperateChaining<Pair<K, V>>(len);
    }

    // construct w/o length
    public HashMap() {

        this.hashSet = new SeperateChaining<Pair<K, V>>();
    }

    // returns true if pair can be added
    public boolean add(K key, V value) throws Exception {

        if (key == null || value == null)
            return false;

        return hashSet.add(new Pair<K, V>(key, value));

    }

    public boolean update(K key, V value) throws Exception {

        if (key == null || value == null)
            return false;

        // if the key is not already in the hashSet-> return false
        if (!remove(key))
            return false;

        return hashSet.add(new Pair<K, V>(key, value));

    }

    public boolean contains(K key) throws Exception {

        if (key == null)
            return false;

        return hashSet.contains(new Pair<K, V>(key, null));

    }

    public boolean remove(K key) throws Exception {

        if (key == null)
            return false;

        return hashSet.remove(new Pair<K, V>(key, null));
    }

    // return true if key values match pair in map exactly
    public boolean has(K key, V value) throws Exception {

        if (key == null || value == null)
            return false;

        Pair<K, V> pair = new Pair<>(key, value);
        return this.contains(key) && hashSet.get(pair).getValue().equals(value);
    }

    public V getValue(K key) throws Exception {

        if (key == null)
            return null;

        return hashSet.get(new Pair<K, V>(key, null)).getValue();

    }

    public SimpleList<K> getKeys() throws Exception {

        if (hashSet.size() == 0)
            return null;

        SimpleList<Pair<K, V>> pairList = hashSet.allValues();
        SimpleList<K> keyList = new SimpleList<K>();

        for (Pair<K, V> element : pairList)
            keyList.add(element.getKey());

        return keyList;
    }

    public int size() {

        return hashSet.size();
    }

    public String toString() {

        return hashSet.toString();
    }
}
