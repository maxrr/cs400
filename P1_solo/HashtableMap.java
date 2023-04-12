// --== CS400 Project One File Header ==--
// Name: Maximilian Rountree
// CSL Username: mrountree
// Email: mrrountree@wisc.edu
// Lecture #: 002
// Notes to Grader: none

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class represents a Hashtable map that will be used in project 1
 * This implementation uses exactly one protected single-dimensional array 
 * instance field, and uses chaining to handle hash collisions (LinkedList)
 */
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
    // protected instance variable that will actually store the data for us
    protected LinkedList<HashtableMapEntry<KeyType, ValueType>>[] arr;
    
    // keep track of the quantity of items we are storing in `arr`
    int size = 0;

    /**
     * Basic constructor for HashtableMap
     * @param capacity the amount of objects to be stored in this HashtableMap
     */
    public HashtableMap(int capacity) {
        this.createLocalStorage(capacity);
    }
    
    /**
     * Creates a HashtableMap with a default capacity of 30
     */
    public HashtableMap() {
        // use default capacity of 30
        this(30);
    }
    
    /**
     * Helper function to consolidate @SuppressWarnings to one method, and
     * to avoid duplicating code. This method (re-)creates the protected instance
     * `arr` variable
     */
    @SuppressWarnings("unchecked")
    private void createLocalStorage(int cap) {
        // create a temporary Object array (cannot create an array using generics)
        LinkedList[] tempArr = new LinkedList[cap];
        this.arr = (LinkedList<HashtableMapEntry<KeyType, ValueType>>[]) tempArr;

        // reset size counter
        this.size = 0;
    }

    /**
     * Helper function to compute the index of a given key once hashed
     * @param key the key to find the index of
     * @return the index of a the given `key` once hashed
     */
    private int index(KeyType key) {
        return Math.abs(key.hashCode()) % this.arr.length;
    }

    /**
     * Helper function to look at the current load factor and determine whether this
     * HashtableMap should grow
     */
    private void checkLoadFactor() {
        float loadFactor = (float) this.size / (float) this.arr.length;
        if (loadFactor >= 0.75) grow();
    }

    /**
     * Helper function to grow table by rehashing and doubling capacity
     */
    private void grow() {
        // clone our local storage so we can iterate over it
        LinkedList<HashtableMapEntry<KeyType, ValueType>>[] tempArr = this.arr.clone();

        // recreate our local storage
        this.createLocalStorage(this.arr.length * 2);

        // iterate through the array
        for (LinkedList<HashtableMapEntry<KeyType, ValueType>> list : tempArr) {
            if (list != null) {
                // iterate through each LinkedList in the array
                for (HashtableMapEntry<KeyType, ValueType> entry : list) {
                    // rehash the entry at this location
                    this.put(entry.key(), entry.val());
                }
            }
        }
    }

    /**
     * Stores a new value in our hash table at the index corresponding to
     * { the absolute value of the key's hashCode() } modulus this HashtableMap's
     * current capacity
     * @param key the key of the new entry
     * @param value the value of the new entry
     * @return true if insertion successful, false if `key` is null or equal to a key
     *         that is already stored in the hash table
     */
    @Override
    public boolean put(KeyType key, ValueType value) {
        if (key == null) return false;

        // calculate index
        int index = index(key);

        // search if key already exists
        if (this.arr[index] != null) {
            for (HashtableMapEntry<KeyType, ValueType> entry : this.arr[index]) {
                if (entry.key().equals(key))
                    return false;
            }
        } else {
            // if entry is null, make a new LinkedList
            this.arr[index] = new LinkedList<HashtableMapEntry<KeyType, ValueType>>();
        }

        // if we have reached this point, there is not an entry in this hashtable that
        // contains this key, so add it and increment our size
        this.arr[index].add(new HashtableMapEntry<KeyType, ValueType>(key, value));
        this.size++;

        // evaluate our load factor (hand off to helper func)
        checkLoadFactor();

        // if we've gotten this far, then our insertion was successful
        return true;
    }

    /**
     * Retrieves a value associated with the provided `key`
     * @param key the key of the entry to lookup
     * @return the value of the entry to lookup
     * @throws NoSuchElementException if the element is not found in this HashtableMap
     */
    @Override
    public ValueType get(KeyType key) throws NoSuchElementException {
        // get index of key
        int index = index(key);

        // search if key already exists
        if (this.arr[index] != null) {
            for (HashtableMapEntry<KeyType, ValueType> entry : this.arr[index]) {
                if (entry.key().equals(key)) {
                    return entry.val();
                }
            }
        }

        // if we've gotten here without returning the value of an entry, 
        // then it's not in our collection, so throw an error
        throw new NoSuchElementException("Supplied key was not found in the collection.");
    }

    /**
     * Retrieves the number of key-value pairs stored in this HashtableMap
     * @return the number of key-value pairs stored in this HashtableMap
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Determines whether or not the given `key` map to a value in this HashtableMap
     * @param key the given `key` to search for
     * @return true if the given `key` maps to a value in this HashtableMap, false otherwise
     */
    @Override
    public boolean containsKey(KeyType key) {
        // get index of key
        int index = index(key);

        // skip to returning false if the entry is null
        if (this.arr[index] == null) return false;

        // search if key already exists
        for (HashtableMapEntry<KeyType, ValueType> entry : this.arr[index]) {
            if (key.equals(entry.key())) {
                return true;
            }
        }

        // if we haven't found it already, it's not in the collection
        return false;
    }

    /**
     * Returns a reference to the value associated with the given `key` that is being
     * removed (also removes the key, obviously...)
     * @param key the given `key` of the key-value pair to remove from this collection
     * @return a reference to the value if found, otherwise null
     */
    @Override
    public ValueType remove(KeyType key) {
        // get index of key
        int index = index(key);

        if (this.arr[index] == null)
            return null;

        // search if key already exists, if it does then remove it
        int s = this.arr[index].size();
        for (int i = 0; i < s; i++) {
            HashtableMapEntry<KeyType, ValueType> entry = this.arr[index].get(i);
            if (entry.key().equals(key)) {
                ValueType value = entry.val();
                this.arr[index].remove(i);
                if (this.arr[index].size() == 0)
                    this.arr[index] = null;
                this.size--;
                return value;
            }
        }

        // if we've gotten here then the key isn't in this collection, so return null
        return null;
    }

    /**
     * Clears the internal storage of this HashtableMap and resets the size to 0
     */
    @Override
    public void clear() {
        this.createLocalStorage(this.arr.length);
    }
}
