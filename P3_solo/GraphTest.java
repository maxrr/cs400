// --== CS400 File Header Information ==--
// Name: Max Rountree
// Email: mrrountree@wisc.edu
// Team: BF
// TA: Yuye
// Lecturer: Gary
// Notes to Grader: none

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the implementation of CS400Graph for the individual component of
 * Project Three: the implementation of Dijsktra's Shortest Path algorithm.
 */
public class GraphTest {

    private CS400Graph<String> graph;
    
    /**
     * Instantiate graph from last week's shortest path activity.
     */
    @BeforeEach
    public void createGraph() {
        graph = new CS400Graph<>();
        // insert vertices A-F
        graph.insertVertex("A");
        graph.insertVertex("B");
        graph.insertVertex("C");
        graph.insertVertex("D");
        graph.insertVertex("E");
        graph.insertVertex("F");
        // insert edges from Week 11. Shortest Path Activity
        graph.insertEdge("A","B",6);
        graph.insertEdge("A","C",2);
        graph.insertEdge("A","D",5);
        graph.insertEdge("B","E",1);
        graph.insertEdge("B","C",2);
        graph.insertEdge("C","B",3);
        graph.insertEdge("C","F",1);
        graph.insertEdge("D","E",3);
        graph.insertEdge("E","A",4);
        graph.insertEdge("F","A",1);
        graph.insertEdge("F","D",1);
    }

    /**
     * Checks the distance/total weight cost from the vertex A to F.
     */
    @Test
    public void testPathCostAtoF() {
        assertTrue(graph.getPathCost("A", "F") == 3);
    }

    /**
     * Checks the distance/total weight cost from the vertex A to D.
     */
    @Test
    public void testPathCostAtoD() {
        assertTrue(graph.getPathCost("A", "D") == 4);
    }

    /**
     * Checks the ordered sequence of data within vertices from the vertex 
     * A to D.
     */
    @Test
    public void testPathAtoD() {
        assertTrue(graph.shortestPath("A", "D").toString().equals(
            "[A, C, F, D]"
        ));
    }

    /**
     * Checks the ordered sequence of data within vertices from the vertex 
     * A to F.
     */
    @Test
    public void testPathAtoF() {
        assertTrue(graph.shortestPath("A", "F").toString().equals(
            "[A, C, F]"
        ));
    }

    /**
     * This test satisfies step 9 in the instructions:
     * "Add an extra test method to confirm that the distance you computed for this
     * node in last week's activity is correct"
     */
    @Test
    public void step9Test() {
        assertTrue(graph.getPathCost("D", "B") == 12);
    }

    /**
     * This test satisfies step 10 in the instructions:
     * "Add an extra test method to confirm the sequence of nodes along the path
     * from your source node to this same end node (the end node that is furthest
     * from your source node) is correct"
     */
    @Test
    public void step10Test() {
        assertTrue(graph.shortestPath("D", "B").toString().equals(
            "[D, E, A, C, B]"
        ));
    }

    /**
     * This test satisfies step 11 in the instructions:
     * "Add another test method to confirm the path cost you reported for the
     * question about path cost from last week's activity"
     */
    @Test
    public void step11Test() {
        assertTrue(graph.getPathCost("E", "F") == 7);
    }

    /**
     * This test satisfies step 12 in the instructions:
     * "Add another test method to confirm the predecessor you reported for question
     * about a final node's predecessor from last week's activity"
     */
    @Test
    public void step12Test() {
        // would love to store graph.getShortestPath("F", "B") as a variable
        // to avoid calling the expensive method twice, but i don't want to 
        // import .util.List in case we're not supposed to
        assertTrue(graph.shortestPath("F", "B").get(graph.shortestPath("F", "B").size() - 2).equals("C"));
    }

    /**
     * This test satisfies step 13 in the instructions:
     * "Add at least one more test method to this class to help convince yourself of
     * the correctness of your implementation"
     */
    @Test
    public void step13Test() {
        // here we make sure that a node's cost to path to itself is *always* zero
        assertTrue(graph.getPathCost("A", "A") == 0);
        assertTrue(graph.getPathCost("B", "B") == 0);
        assertTrue(graph.getPathCost("C", "C") == 0);
        assertTrue(graph.getPathCost("D", "D") == 0);
        assertTrue(graph.getPathCost("E", "E") == 0);
        assertTrue(graph.getPathCost("F", "F") == 0);

        // here we check some more weights, just to be sure
        assertTrue(graph.getPathCost("A", "B") == 5);
        assertTrue(graph.getPathCost("B", "A") == 4);
        assertTrue(graph.getPathCost("B", "E") == 1);
        assertTrue(graph.getPathCost("F", "B") == 6);
        assertTrue(graph.getPathCost("C", "E") == 4);
    }

}
