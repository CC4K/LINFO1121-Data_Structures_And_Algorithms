package Part4_Maps_Strings;

/**
 * With the given partial implementation of LinearProbingHashST, we ask you to
 * implement the resize, get and put methods
 * You are not allowed to use already existing classes and methods from Java
 */
public class LinearProbingHashST<Key, Value> {
    private static final int INIT_CAPACITY = 4;
    
    // Please do not add/remove variables/modify their visibility.
    protected int N;           // number of key-value pairs in the symbol table
    protected int M;           // size of linear probing table
    protected Key[] keys;      // the keys
    protected Value[] vals;    // the values


    /**
     * Initializes an empty symbol table.
     */
    public LinearProbingHashST() {
        this(INIT_CAPACITY);
    }

    /**
     * Initializes an empty symbol table with the specified initial capacity.
     * @param capacity the initial capacity
     */
    public LinearProbingHashST(int capacity) {
        M = capacity;
        N = 0;
        keys = (Key[])   new Object[M];
        vals = (Value[]) new Object[M];
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() { return N; }

    /**
     * Returns the current capacity of the table
     * @return the current capacity of the table
     */
    public int capacity() { return M; }

    /**
     * Returns true if this symbol table is empty.
     * @return {@code true} if this symbol table is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() { return size() == 0; }

    /**
     * Returns true if this symbol table contains the specified key.
     * @param  key the key
     * @return {@code true} if this symbol table contains {@code key};
     *         {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(Key key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    /// Hash function for keys - returns value between 0 and M-1
    protected int hash(Key key) { return (key.hashCode() & 0x7fffffff) % M; }

    /**
     * Resizes the hash table to the given capacity by re-hashing all of the keys
     * @param capacity the capacity
     */
    protected void resize(int capacity) {
        // TODO
        // simply create a new one LOL
        LinearProbingHashST<Key, Value> ret = new LinearProbingHashST<>(capacity);
        for (int i = 0; i < M; i++) { // go through entire array
            if (keys[i] != null) ret.put(keys[i], vals[i]); // fill new HashTable
        }
        // N already correct with put()
        M = ret.M;
        keys = ret.keys;
        vals = ret.vals;
    }

    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old 
     * value with the new value if the symbol table already contains the specified key.
     * The load factor should never exceed 50% so make sure to resize correctly
     * @param  key the key
     * @param  val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void put(Key key, Value val) {
        // TODO
        if (key == null) throw new IllegalArgumentException("argument to put() is null");
        // check array capacity (if N too close to M)
        if (N == M /2) resize(M *2); // double the capacity
        int hashedKey = hash(key);
        int i;
        for (i = hashedKey; keys[i] != null ; i = (i+1) % M) { // keep looking right until empty space
            if (keys[i].equals(key)) {
                vals[i] = val;
                return; // key match => replace value
            }
        }
        // once we stop the loop => place new <Key, Val> at i
        keys[i] = key; vals[i] = val;
        N++;
    }

    /**
     * Returns the value associated with the specified key.
     * @param key the key
     * @return the value associated with {@code key};
     *         {@code null} if no such value
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public Value get(Key key) {
        // TODO
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        // hash the key and look in array where it would be
        int hashedKey = hash(key);
        for (int i = hashedKey; keys[i] != null; i = (i+1) % M) { // keep looking right until empty space
            if (keys[i].equals(key)) {
                return vals[i]; // key match
            }
        }
        return null; // no match
    }

    /**
     * Returns all keys in this symbol table
     */
    public Object[] keys() {
        Object[] exportKeys = new Object[N];
        int j = 0;
        for (int i = 0; i < M; i++)
            if (keys[i] != null) exportKeys[j++] = keys[i];
        return exportKeys;
    }

}
