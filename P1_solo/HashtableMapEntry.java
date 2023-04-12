// --== CS400 Project One File Header ==--
// Name: Maximilian Rountree
// CSL Username: mrountree
// Email: mrrountree@wisc.edu
// Lecture #: 002
// Notes to Grader: none

/**
 * This class is a helper class that models a single key-value pair in a
 * HashtableMap
 */
public class HashtableMapEntry<KeyType, ValueType> {
    // declare our private variables to keep track of the data stored in this entry
    private ValueType val;
    private KeyType key;

    /**
     * Basic constructor
     * @param entry the value to be stored in this entry
     */
    public HashtableMapEntry(KeyType key, ValueType entry) {
        this.key = key;
        this.val = entry;
    }

    /**
     * Retrieves the key associated with this entry
     * @return the key associated with this entry
     */
    public KeyType key() {
        return this.key;
    }

    public ValueType val() {
        return this.val;
    }
}