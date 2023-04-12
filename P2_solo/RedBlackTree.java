// --== CS400 File Header Information ==--
// Name: Max Rountree
// Email: mrrountree@wisc.edu
// Team: BF
// TA: Yuye
// Lecturer: Gary Dahl
// Notes to Grader: none

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Stack;

// JUnit imports
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Red-Black Tree implementation with a Node inner class for representing the nodes of the tree.
 * Currently, this implements a Binary Search Tree that we will turn into a red black tree by
 * modifying the insert functionality. In this activity, we will start with implementing rotations
 * for the binary search tree insert algorithm. You can use this class' insert method to build a
 * regular binary search tree, and its toString method to display a level-order traversal of the
 * tree.
 */
public class RedBlackTree<T extends Comparable<T>> implements SortedCollectionInterface<T> {

  /**
   * This class represents a node holding a single value within a binary tree the parent, left, and
   * right child references are always maintained.
   */
  protected static class Node<T> {
    public T data;
    public Node<T> parent; // null for root node
    public Node<T> leftChild;
    public Node<T> rightChild;
    public int blackHeight;

    public Node(T data) {
      this.data = data;
      this.blackHeight = 0; // default to red
    }

    /**
     * @return true when this node has a parent and is the left child of that parent, otherwise
     *         return false
     */
    public boolean isLeftChild() {
      return parent != null && parent.leftChild == this;
    }

  }

  protected Node<T> root; // reference to root node of tree, null when empty
  protected int size = 0; // the number of values in the tree

  /**
   * Performs a naive insertion into a binary search tree: adding the input data value to a new node
   * in a leaf position within the tree. After this insertion, no attempt is made to restructure or
   * balance the tree. This tree will not hold null references, nor duplicate data values.
   * 
   * @param data to be added into this binary search tree
   * @return true if the value was inserted, false if not
   * @throws NullPointerException     when the provided data argument is null
   * @throws IllegalArgumentException when the newNode and subtree contain equal data references
   */
  @Override
  public boolean insert(T data) throws NullPointerException, IllegalArgumentException {
    // null references cannot be stored within this tree
    if (data == null)
      throw new NullPointerException("This RedBlackTree cannot store null references.");

    Node<T> newNode = new Node<>(data);
    if (root == null) {
      root = newNode;
      root.blackHeight = 1;
      size++;
      return true;
    } // add first node to an empty tree
    else {
      boolean returnValue = insertHelper(newNode, root); // recursively insert into subtree
      if (returnValue) {
        size++;
        root.blackHeight = 1;
      } else
        throw new IllegalArgumentException("This RedBlackTree already contains that value.");
      return returnValue;
    }
  }

  /**
   * Enforces Red-Black tree properties after an insertion, this method should correct any red child
   * of red node violations that are present after inserting a new node into the tree.
   * 
   * @param n the node to ensure RBT properties of
   */
  protected void enforceRBTreePropertiesAfterInsert(Node<T> n) {
    // we only want to do things if we have a red child
    // of red node violation, otherwise we can leave it be
    if (n.parent != null) {
      // targeted node has parent
      if (n.parent.blackHeight == 0) {
        // targeted node's parent is red (VIOLATION!!!)
        // we know that n has a grandparent too, since the root node is always black
        Node<T> parentSibling;

        if (n.parent.isLeftChild()) {
          // parent is left child
          parentSibling = n.parent.parent.rightChild;
        } else {
          // parent is right child
          parentSibling = n.parent.parent.leftChild;
        }

        if (parentSibling != null) {
          // parent has a sibling
          if (parentSibling.blackHeight == 1) {
            // node's uncle is a black node
            if (parentSibling.isLeftChild() == n.isLeftChild()) {
              // node's uncle and node are on the same side (case 1)
              // rotate and turn into a case 2, recurse
              Node<T> temp = n.parent;
              rotate(n, n.parent);
              enforceRBTreePropertiesAfterInsert(temp);

            } else {
              // node's uncle and node are on opposite sides (case 2)
              // rotate and color swap, recurse on parent
              Node<T> temp = n.parent.parent;
              int tbh = temp.blackHeight;
              rotate(n.parent, n.parent.parent);

              temp.blackHeight = temp.parent.blackHeight;
              temp.parent.blackHeight = tbh;
              enforceRBTreePropertiesAfterInsert(temp.parent);
            }

          } else {
            // node's uncle is a red node
            // move black height down 1, recurse on parent
            n.parent.blackHeight = 1;
            parentSibling.blackHeight = 1;
            enforceRBTreePropertiesAfterInsert(n.parent);
          }

        } else {
          // parent has no sibling, rotate and color swap, recurse on parent
          Node<T> temp = n.parent.parent;
          int tbh = temp.blackHeight;
          rotate(n.parent, n.parent.parent);

          temp.blackHeight = temp.parent.blackHeight;
          temp.parent.blackHeight = tbh;
          enforceRBTreePropertiesAfterInsert(temp.parent);
        }

      }

    }
  }

  /**
   * Recursive helper method to find the subtree with a null reference in the position that the
   * newNode should be inserted, and then extend this tree by the newNode in that position.
   * 
   * @param newNode is the new node that is being added to this tree
   * @param subtree is the reference to a node within this tree which the newNode should be inserted
   *                as a descenedent beneath
   * @return true is the value was inserted in subtree, false if not
   */
  private boolean insertHelper(Node<T> newNode, Node<T> subtree) {
    int compare = newNode.data.compareTo(subtree.data);
    // do not allow duplicate values to be stored within this tree
    if (compare == 0)
      return false;

    // store newNode within left subtree of subtree
    else if (compare < 0) {
      if (subtree.leftChild == null) { // left subtree empty, add here
        subtree.leftChild = newNode;
        newNode.parent = subtree;
        enforceRBTreePropertiesAfterInsert(newNode);
        return true;
        // otherwise continue recursive search for location to insert
      } else
        return insertHelper(newNode, subtree.leftChild);
    }

    // store newNode within the right subtree of subtree
    else {
      if (subtree.rightChild == null) { // right subtree empty, add here
        subtree.rightChild = newNode;
        newNode.parent = subtree;
        enforceRBTreePropertiesAfterInsert(newNode);
        return true;
        // otherwise continue recursive search for location to insert
      } else
        return insertHelper(newNode, subtree.rightChild);
    }
  }

  /**
   * Performs the rotation operation on the provided nodes within this tree. When the provided child
   * is a leftChild of the provided parent, this method will perform a right rotation. When the
   * provided child is a rightChild of the provided parent, this method will perform a left
   * rotation. When the provided nodes are not related in one of these ways, this method will throw
   * an IllegalArgumentException.
   * 
   * @param child  is the node being rotated from child to parent position (between these two node
   *               arguments)
   * @param parent is the node being rotated from parent to child position (between these two node
   *               arguments)
   * @throws IllegalArgumentException when the provided child and parent node references are not
   *                                  initially (pre-rotation) related that way
   */
  private void rotate(Node<T> child, Node<T> parent) throws IllegalArgumentException {
    // throw an error if the child is not actuall a child of the parent
    if (!child.parent.equals(parent))
      throw new IllegalArgumentException(
          "The child node supplied is not a child of the parent node supplied.");

    if (child.isLeftChild()) {

      // make child's right value the parent's left (if exists)
      parent.leftChild = child.rightChild;
      if (child.rightChild != null) {
        child.rightChild.parent = parent;
      }

      // deal with root
      if (parent.equals(root)) {
        // parent is root, so set root to child
        root = child;
        child.parent = null;
      } else {
        // parent is not root, so set its reference (from its parent) to child
        if (parent.isLeftChild())
          parent.parent.leftChild = child;
        else
          parent.parent.rightChild = child;
        child.parent = parent.parent;
      }

      // now, set the (before) parent to be a child of the (before) child
      child.rightChild = parent;
      parent.parent = child;

    } else {

      // make child's left value the parent's right (if exists)
      parent.rightChild = child.leftChild;
      if (child.leftChild != null) {
        child.leftChild.parent = parent;
      }

      // deal with root
      if (parent.equals(root)) {
        // parent is root, so set root to child
        root = child;
        child.parent = null;
      } else {
        // parent is not root, so set its reference (from its parent) to child
        if (parent.isLeftChild())
          parent.parent.leftChild = child;
        else
          parent.parent.rightChild = child;
        child.parent = parent.parent;
      }

      // now, set the (before) parent to be a child of the (before) child
      child.leftChild = parent;
      parent.parent = child;

    }

    // we would swap the colors right...
    // here
    // ...but that's not something we can do right now
  }

  /**
   * Get the size of the tree (its number of nodes).
   * 
   * @return the number of nodes in the tree
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Method to check if the tree is empty (does not contain any node).
   * 
   * @return true of this.size() return 0, false if this.size() > 0
   */
  @Override
  public boolean isEmpty() {
    return this.size() == 0;
  }

  /**
   * Checks whether the tree contains the value *data*.
   * 
   * @param data the data value to test for
   * @return true if *data* is in the tree, false if it is not in the tree
   */
  @Override
  public boolean contains(T data) {
    // null references will not be stored within this tree
    if (data == null)
      throw new NullPointerException("This RedBlackTree cannot store null references.");
    return this.containsHelper(data, root);
  }

  /**
   * Recursive helper method that recurses through the tree and looks for the value *data*.
   * 
   * @param data    the data value to look for
   * @param subtree the subtree to search through
   * @return true of the value is in the subtree, false if not
   */
  private boolean containsHelper(T data, Node<T> subtree) {
    if (subtree == null) {
      // we are at a null child, value is not in tree
      return false;
    } else {
      int compare = data.compareTo(subtree.data);
      if (compare < 0) {
        // go left in the tree
        return containsHelper(data, subtree.leftChild);
      } else if (compare > 0) {
        // go right in the tree
        return containsHelper(data, subtree.rightChild);
      } else {
        // we found it :)
        return true;
      }
    }
  }

  /**
   * Returns an iterator over the values in in-order (sorted) order.
   * 
   * @return iterator object that traverses the tree in in-order sequence
   */
  @Override
  public Iterator<T> iterator() {
    // use an anonymous class here that implements the Iterator interface
    // we create a new on-off object of this class everytime the iterator
    // method is called
    return new Iterator<T>() {
      // a stack and current reference store the progress of the traversal
      // so that we can return one value at a time with the Iterator
      Stack<Node<T>> stack = null;
      Node<T> current = root;

      /**
       * The next method is called for each value in the traversal sequence. It returns one value at
       * a time.
       * 
       * @return next value in the sequence of the traversal
       * @throws NoSuchElementException if there is no more elements in the sequence
       */
      public T next() {
        // if stack == null, we need to initialize the stack and current element
        if (stack == null) {
          stack = new Stack<Node<T>>();
          current = root;
        }
        // go left as far as possible in the sub tree we are in un8til we hit a null
        // leaf (current is null), pushing all the nodes we fund on our way onto the
        // stack to process later
        while (current != null) {
          stack.push(current);
          current = current.leftChild;
        }
        // as long as the stack is not empty, we haven't finished the traversal yet;
        // take the next element from the stack and return it, then start to step down
        // its right subtree (set its right sub tree to current)
        if (!stack.isEmpty()) {
          Node<T> processedNode = stack.pop();
          current = processedNode.rightChild;
          return processedNode.data;
        } else {
          // if the stack is empty, we are done with our traversal
          throw new NoSuchElementException("There are no more elements in the tree");
        }

      }

      /**
       * Returns a boolean that indicates if the iterator has more elements (true), or if the
       * traversal has finished (false)
       * 
       * @return boolean indicating whether there are more elements / steps for the traversal
       */
      public boolean hasNext() {
        // return true if we either still have a current reference, or the stack
        // is not empty yet
        return !(current == null && (stack == null || stack.isEmpty()));
      }

    };
  }

  /**
   * This method performs an inorder traversal of the tree. The string representations of each data
   * value within this tree are assembled into a comma separated string within brackets (similar to
   * many implementations of java.util.Collection, like java.util.ArrayList, LinkedList, etc). Note
   * that this RedBlackTree class implementation of toString generates an inorder traversal. The
   * toString of the Node class class above produces a level order traversal of the nodes / values
   * of the tree.
   * 
   * @return string containing the ordered values of this tree (in-order traversal)
   */
  public String toInOrderString() {
    // use the inorder Iterator that we get by calling the iterator method above
    // to generate a string of all values of the tree in (ordered) in-order
    // traversal sequence
    Iterator<T> treeNodeIterator = this.iterator();
    StringBuffer sb = new StringBuffer();
    sb.append("[ ");
    if (treeNodeIterator.hasNext())
      sb.append(treeNodeIterator.next());
    while (treeNodeIterator.hasNext()) {
      T data = treeNodeIterator.next();
      sb.append(", ");
      sb.append(data.toString());
    }
    sb.append(" ]");
    return sb.toString();
  }

  /**
   * This method performs a level order traversal of the tree rooted at the current node. The string
   * representations of each data value within this tree are assembled into a comma separated string
   * within brackets (similar to many implementations of java.util.Collection). Note that the Node's
   * implementation of toString generates a level order traversal. The toString of the RedBlackTree
   * class below produces an inorder traversal of the nodes / values of the tree. This method will
   * be helpful as a helper for the debugging and testing of your rotation implementation.
   * 
   * @return string containing the values of this tree in level order
   */
  public String toLevelOrderString() {
    String output = "[ ";
    LinkedList<Node<T>> q = new LinkedList<>();
    q.add(this.root);
    while (!q.isEmpty()) {
      Node<T> next = q.removeFirst();
      if (next.leftChild != null)
        q.add(next.leftChild);
      if (next.rightChild != null)
        q.add(next.rightChild);
      output += next.data.toString();
      if (!q.isEmpty())
        output += ", ";
    }
    return output + " ]";
  }

  @Override
  public String toString() {
    return "level order: " + this.toLevelOrderString() + "/nin order: " + this.toInOrderString();
  }


  // Implement at least 3 boolean test methods by using the method signatures below,
  // removing the comments around them and addind your testing code to them. You can
  // use your notes from lecture for ideas on concrete examples of rotation to test for.
  // Make sure to include rotations within and at the root of a tree in your test cases.
  // If you are adding additional tests, then name the method similar to the ones given below.
  // Eg: public static boolean test4() {}
  // Do not change the method name or return type of the existing tests.
  // You can run your tests by commenting in the calls to the test methods

  /**
   * This test ensures that the root node is always a black node.
   */
  @Test
  public void testBlackRoot() {
    // make a new RedBlackTree
    RedBlackTree<String> rbt = new RedBlackTree<String>();

    // insert the first value and make sure it sets the root properly
    rbt.insert("TestString1");
    assertEquals(rbt.root.blackHeight, 1, "Root node should be a black node");

    // make another new RedBlackTree
    RedBlackTree<Integer> rbt2 = new RedBlackTree<Integer>();

    // insert another first value (of a different type), and make sure it sets the root properly
    rbt2.insert(5);
    assertEquals(rbt2.root.blackHeight, 1, "Root node should be a black node");
  }

  /**
   * This test ensures the self-balancing nature of a RBT (when inserting a sorted list of values)
   */
  @Test
  public void testSortedInsert() {
    // make a new RedBlackTree
    RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();

    // insert the first value and make sure it sets the root properly
    rbt.insert(1);
    assertEquals(rbt.root.data, 1, "Root node should be node 1");

    // insert the second value and make sure it sets the children correctly
    rbt.insert(2);
    assertEquals(rbt.root.leftChild, null, "Root node should have no left child");
    assertEquals(rbt.root.rightChild.data, 2, "Node 2 should be right child of root node");

    // insert the third value and make sure it sets the children correctly
    rbt.insert(3);
    assertEquals(rbt.root.leftChild.data, 1, "Node 1 should be left child of root node");
    assertEquals(rbt.root.data, 2, "Node 2 should be root node");
    assertEquals(rbt.root.rightChild.data, 3, "Node 3 should be right child of root node");

    // insert the fourth value and make sure the blackHeights are set correctly
    rbt.insert(4);
    assertEquals(rbt.root.leftChild.blackHeight, 1, "Node 1 should be a black node");
    assertEquals(rbt.root.rightChild.blackHeight, 1, "Node 3 should be a black node");

    // insert the fifth value and make sure it rotates and sets the children correctly
    rbt.insert(5);
    assertEquals(rbt.root.rightChild.data, 4, "Node 4 should be right child of root node");
    assertEquals(rbt.root.rightChild.blackHeight, 1, "Node 4 should be a black node");
    assertEquals(rbt.root.rightChild.leftChild.data, 3,
        "Node 3 should be left child of node 4, which is right child of root node");
    assertEquals(rbt.root.rightChild.leftChild.blackHeight, 0, "Node 3 should be a red node");
    assertEquals(rbt.root.rightChild.rightChild.data, 5,
        "Node 5 should be right child or node 4, which is right child of root node");
    assertEquals(rbt.root.rightChild.rightChild.blackHeight, 0, "Node 5 should be a red node");
  }

  /**
   * This is essentially a copy of the Red Uncle test on gradescope.
   */
  @Test
  public void testRedUncle() {
    // make a new RedBlackTree and insert the to-be root node
    RedBlackTree<Integer> rbt = new RedBlackTree<Integer>();

    // insert a value and ensure the tree is structured correctly
    rbt.insert(23);
    assertEquals(23, rbt.root.data, "Root node should have value 23");
    assertEquals(1, rbt.root.blackHeight, "Root node should be a black node");

    // insert a value and ensure the tree is structured correctly
    rbt.insert(7);
    assertEquals(23, rbt.root.data, "Root node should have value 23");
    assertEquals(1, rbt.root.blackHeight, "Root node should be a black node");
    assertNotEquals(null, rbt.root.leftChild, "Root node's left child should not be null");
    assertEquals(null, rbt.root.rightChild, "Root node's right child should be null");
    assertEquals(7, rbt.root.leftChild.data, "Root node's left child should have value 7");
    assertEquals(0, rbt.root.leftChild.blackHeight, "Root node's left child should be a red node");

    // insert a value and ensure the tree is structured correctly
    rbt.insert(41);
    assertEquals(23, rbt.root.data, "Root node should have value 23");
    assertEquals(1, rbt.root.blackHeight, "Root node should be a black node");
    assertNotEquals(null, rbt.root.leftChild, "Root node's left child should not be null");
    assertEquals(7, rbt.root.leftChild.data, "Root node's left child should have value 7");
    assertEquals(0, rbt.root.leftChild.blackHeight, "Root node's left child should be a red node");
    assertNotEquals(null, rbt.root.rightChild, "Root node's right child should not be null");
    assertEquals(41, rbt.root.rightChild.data, "Root node's right child should have value 41");
    assertEquals(0, rbt.root.rightChild.blackHeight,
        "Root node's right child should be a red node");

    // insert a value and ensure the tree is structured correctly
    rbt.insert(37);
    assertEquals(23, rbt.root.data, "Root node should have value 23");
    assertEquals(1, rbt.root.blackHeight, "Root node should be a black node");
    assertNotEquals(null, rbt.root.leftChild, "Root node's left child should not be null");
    assertEquals(7, rbt.root.leftChild.data, "Root node's left child should have value 7");
    assertEquals(1, rbt.root.leftChild.blackHeight,
        "Root node's left child should be a black node");
    assertNotEquals(null, rbt.root.rightChild, "Root node's right child should not be null");
    assertEquals(41, rbt.root.rightChild.data, "Root node's right child should have value 41");
    assertEquals(1, rbt.root.rightChild.blackHeight,
        "Root node's right child should be a black node");
    assertEquals(null, rbt.root.rightChild.rightChild,
        "Root node's right child (41) should have no right child");
    assertNotEquals(null, rbt.root.rightChild.leftChild,
        "Root node's right child (41) should have a left child");
    assertEquals(37, rbt.root.rightChild.leftChild.data,
        "Root node's right child (41) should have node 37 as its left child");
  }

  /**
   * Main method to run tests. Comment out the lines for each test to run them.
   * 
   * @param args
   */
  public static void main(String[] args) {
  }

}
