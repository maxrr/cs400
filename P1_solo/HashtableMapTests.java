// --== CS400 Project One File Header ==--
// Name: Maximilian Rountree
// CSL Username: mrountree
// Email: mrrountree@wisc.edu
// Lecture #: 002
// Notes to Grader: none

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class contains tests for our HashtableMap class, to ensure that all of its
 * methods work properly.
 */
public class HashtableMapTests {
    /**
     * This test tests the HashtableMap constructor by creating HashtableMaps
     * of several capacities
     * @return true if test passed, false if anything failed
     */
    public static boolean test1() {
        try {
            // create some HashtableMaps and do nothing with them
            new HashtableMap<String, Object>();
            new HashtableMap<String, Object>(5);
            new HashtableMap<String, Object>(10);
            new HashtableMap<String, Object>(100);
        } catch (Exception e) {    
            // no exceptions expected, return false
            return false;
        }
        return true;
    }

    /**
     * This test creates a HashtableMap and tests adding to it
     * @return true if test passed, false if anything failed
     */
    public static boolean test2() {
        try {
            HashtableMap<Integer, String> HTM1 = new HashtableMap<Integer, String>();
            HTM1.put(1, "One");
            HTM1.put(2, "Two");
            HTM1.put(3, "Three");

            HashtableMap<String, Object> HTM2 = new HashtableMap<String, Object>();
            HTM2.put("LinkedList", new LinkedList<String>());
            HTM2.put("HashtableMap", new HashtableMap<String, String>());

            HashtableMap<Character, Integer> HTM3 = new HashtableMap<Character, Integer>();
            HTM3.put('a', 1);
            HTM3.put('b', 2);
            HTM3.put('z', 26);
        } catch (Exception e) {
            // no exceptions expected, return false
            return false;
        }
        return true;
    }

    /**
     * This test creates a HashtableMap and tests the containsKey method
     * @return true if test passed, false if anything failed
     */
    public static boolean test3() {
        try {
            HashtableMap<Integer, String> HTM1 = new HashtableMap<Integer, String>();
            HTM1.put(1, "One");
            HTM1.put(2, "Two");
            HTM1.put(3, "Three");
            if (!HTM1.containsKey(1) || !HTM1.containsKey(2) || !HTM1.containsKey(3) || HTM1.containsKey(4))
                return false;

            HashtableMap<String, Object> HTM2 = new HashtableMap<String, Object>();
            HTM2.put("LinkedList", new LinkedList<String>());
            HTM2.put("HashtableMap", new HashtableMap<String, String>());
            if (!HTM2.containsKey("LinkedList") || !HTM2.containsKey("HashtableMap") || HTM2.containsKey("Object"))
                return false;

            HashtableMap<Character, Integer> HTM3 = new HashtableMap<Character, Integer>();
            HTM3.put('a', 1);
            HTM3.put('b', 2);
            HTM3.put('z', 26);
            if (!HTM3.containsKey('a') || !HTM3.containsKey('b') || !HTM3.containsKey('z') || HTM3.containsKey('d'))
                return false;

            boolean t1 = HTM1.put(2, "Two Again!!");
            if (t1) return false;
        } catch (Exception e) {
            // no exceptions expected, return false
            return false;
        }
        return true;
    }

    /**
     * This test creates a HashtableMap and tests removing from it,
     * as well as testing the clear and size method
     * @return true if test passed, false if anything failed
     */
    public static boolean test4() {
        try {
            HashtableMap<Integer, String> HTM1 = new HashtableMap<Integer, String>();

            if (HTM1.size() != 0)
                return false;

            HTM1.put(1, "One");
            HTM1.put(2, "Two");
            HTM1.put(3, "Three");

            if (HTM1.size() != 3)
                return false;

            HTM1.remove(1);
            if (HTM1.containsKey(1) || HTM1.size() != 2)
                return false;
            HTM1.put(1, "One but a second time");
            if (!HTM1.containsKey(1) || HTM1.size() != 3)
                return false;

            HTM1.remove(1);
            HTM1.remove(2);   
            if (HTM1.containsKey(1) || HTM1.containsKey(2) || HTM1.size() != 1)
                return false;
            

            HTM1.put(4, "Four");
            if (HTM1.size() != 2)
                return false;
            
            HTM1.clear();
            if (HTM1.size() != 0)
                return false;

            HTM1.get(4);

            // we should have thrown an error by now
            return false;

        } catch (NoSuchElementException e) {
            // expected behavior, do nothing
        } catch (Exception e) {
            // not expecting any other kinds of errors
            return false;
        }
        return true;
    }

    /**
     * This test creates a HashtableMap and ensures the HashtableMap will grow
     * if it nears its capacity
     * @return true if test passed, false if anything failed
     */
    public static boolean test5() {
        try {
            HashtableMap<Integer, String> HTM1 = new HashtableMap<Integer, String>();
            for (int i = 0; i < 50; i++) {
                HTM1.put(i, "abcd" + i);
            }

            if (HTM1.size() != 50)
                return false;

            if (!HTM1.containsKey(48) || !HTM1.containsKey(2))
                return false;
        } catch (Exception e) {
            // not expecting any errors
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("Test 1: " + test1());
        System.out.println("Test 2: " + test2());
        System.out.println("Test 3: " + test3());
        System.out.println("Test 4: " + test4());
        System.out.println("Test 5: " + test5());
    }
}
