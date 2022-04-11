package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author HANXICHEN
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private static final int INIT_SIZE = 16;
    private static final double LOAD_FACTOR = 0.75;
    private Collection<Node>[] buckets;

    private int size = 0;
    private int initialSize;
    private double maxLoad;

    /** Constructors */
    public MyHashMap() {
        this(INIT_SIZE, LOAD_FACTOR);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, LOAD_FACTOR);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.initialSize = initialSize;
        this.maxLoad = maxLoad;
        buckets = createTable(initialSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < initialSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    @Override
    public void clear() {
        buckets = createTable(initialSize);
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int index = Math.floorMod(key.hashCode(), initialSize);

        Collection<Node> c = buckets[index];
        Iterator<Node> iter = c.iterator();
        while (iter.hasNext()) {
            if (iter.next().key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int index = Math.floorMod(key.hashCode(), initialSize);

        Collection<Node> c = buckets[index];
        for (Node node : c) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        int index = Math.floorMod(key.hashCode(), initialSize);
        Collection<Node> c = buckets[index];

        if (containsKey(key)) {
            remove(key);
        }

        c.add(createNode(key, value));
        size++;

        if ((double)size / initialSize > maxLoad) {
            resize();
        }
    }

    private void resize() {
        MyHashMap<K, V> temp = new MyHashMap<>(initialSize * 2);
        Set<K> keys = keySet();
        for (K key : keys) {
            temp.put(key, get(key));
        }
        initialSize = temp.initialSize;
        buckets = temp.buckets;
    }
    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();

        for (int i = 0; i < initialSize; i++) {
            for (Node node : buckets[i]) {
                keys.add(node.key);
            }
        }

        return keys;
    }

    @Override
    public V remove(K key) {
        int index = Math.floorMod(key.hashCode(), initialSize);
        Collection<Node> c = buckets[index];

        Node node = getNode(key, c);

        if (node == null) {
            return null;
        }
        c.remove(node);
        size--;

        return node.value;
    }

    @Override
    public V remove(K key, V value) {
        int index = Math.floorMod(key.hashCode(), initialSize);
        Collection<Node> c = buckets[index];

        Node node = getNode(key, c);

        if (node == null || !node.value.equals(value)) {
            return null;
        }

        c.remove(node);
        size--;

        return value;
    }
    private Node getNode(K key, Collection<Node> c) {
        Node node = null;
        for (Node n : c) {
            if (n.key == key) {
                node = n;
            }
        }
        return node;
    }

    @Override
    public Iterator<K> iterator() {
        return new MyHashMapIterator();
    }

    private class MyHashMapIterator implements Iterator<K> {
        private Collection<Node> c;
        private int index;

        public MyHashMapIterator() {
            index = 0;
            c = buckets[index];
        }

        @Override
        public boolean hasNext() {
            return c.iterator().hasNext();
        }

        @Override
        public K next() {
            K key;
            if (hasNext()) {
                key = c.iterator().next().key;
            } else {
                c = buckets[++index];
            }
            return null;
        }
    }
}
