import java.util.*;

public class Main {

    // Run "java -ea Main" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)
    
    // test 1
    public static void testAddNode() {
	Graph g = new ListGraph();
	assert g.addNode("a");  // Should return true (node "a" was added)
	assert g.addNode("a") == false;  // Should return false (node "a" already exists)
	assert g.hasNode("a");  // Check if the node "a" exists
	  }
      
      // test 2
	  public static void testHasNode() {
	Graph g = new ListGraph();
	assert !g.hasNode("a");  // Should return false, "a" is not in the graph
      
	g.addNode("a");
	assert g.hasNode("a");  // Should return true, "a" was added
      
	g.removeNode("a");
	assert !g.hasNode("a");  // Should return false, "a" was removed
	  }
      
      // test 3
	  public static void testHasEdge() {
	Graph g = new ListGraph();
	g.addNode("a");
	g.addNode("b");
      
	assert !g.hasEdge("a", "b");  // Should return false, no edge exists yet
      
	g.addEdge("a", "b");
	assert g.hasEdge("a", "b");  // Should return true, edge was added
      
	g.removeEdge("a", "b");
	assert !g.hasEdge("a", "b");  // Should return false, edge was removed
	  }
      
      
      // test 4
	  public static void testRemoveNode() {
	Graph g = new ListGraph();
	g.addNode("a");
	g.addNode("b");
	g.addEdge("a", "b");
      
	assert g.hasNode("a");  // Node "a" exists
	assert g.removeNode("a");  // Should return true, node "a" removed
	assert !g.hasNode("a");  // Node "a" should no longer exist
	assert !g.hasEdge("a", "b");  // Edge should also be removed
      
	assert !g.removeNode("c");  // Should return false, node "c" doesn't exist
	  }
      
      
      // test 5
	  public static void testRemoveEdge() {
	Graph g = new ListGraph();
	g.addNode("a");
	g.addNode("b");
	g.addEdge("a", "b");
      
	assert g.hasEdge("a", "b");  // Edge exists
	assert g.removeEdge("a", "b");  // Should return true, edge is removed
	assert !g.hasEdge("a", "b");  // Edge should no longer exist
      
	  }
      
      // test 6
	  public static void testNodes() {
	Graph g = new ListGraph();
	g.addNode("a");
	g.addNode("b");
	g.addNode("c");
      
	List<String> nodeList = g.nodes();
	assert nodeList.size() == 3;  // Should return a list with 3 nodes
	assert nodeList.contains("a");  // The list should contain "a"
	assert nodeList.contains("b");  // The list should contain "b"
	assert nodeList.contains("c");  // The list should contain "c"
      
	g.removeNode("b");
	nodeList = g.nodes();
	assert nodeList.size() == 2;  // Now the list should only have 2 nodes
	assert !nodeList.contains("b");  // "b" should no longer be in the list
	  }
      
      // test 7
      
	  public static void testSucc() {
	Graph g = new ListGraph();
	g.addNode("a");
	g.addNode("b");
	g.addNode("c");
	g.addEdge("a", "b");
	g.addEdge("a", "c");
      
	List<String> successors = g.succ("a");
	assert successors.size() == 2;  // Should have 2 successors
	assert successors.contains("b");  // "b" is a successor of "a"
	assert successors.contains("c");  // "c" is a successor of "a"
      
	boolean exceptionThrown = false;
	try {
	    g.succ("d");  // This should throw NoSuchElementException since "d" is not in the graph
	} catch (NoSuchElementException e) {
	    exceptionThrown = true;
	}
	assert exceptionThrown;  // Exception should be thrown for non-existing node
	  }
      
	  public static void testPred() {
	Graph g = new ListGraph();
	g.addNode("a");
	g.addNode("b");
	g.addNode("c");
	g.addEdge("b", "a");
	g.addEdge("c", "a");
      
	List<String> predecessors = g.pred("a");
	assert predecessors.size() == 2;  // "a" has 2 predecessors
	assert predecessors.contains("b");  // "b" is a predecessor of "a"
	assert predecessors.contains("c");  // "c" is a predecessor of "a"
	  }
      
	  public static void testUnion() {
	Graph g1 = new ListGraph();
	g1.addNode("a");
	g1.addNode("b");
	g1.addEdge("a", "b");
      
	Graph g2 = new ListGraph();
	g2.addNode("b");
	g2.addNode("c");
	g2.addEdge("b", "c");
      
	Graph unionGraph = g1.union(g2);
      
	assert unionGraph.hasNode("a");  // The union graph should have node "a"
	assert unionGraph.hasNode("b");  // The union graph should have node "b"
	assert unionGraph.hasNode("c");  // The union graph should have node "c"
      
	assert unionGraph.hasEdge("a", "b");  // The edge from "a" to "b" should exist
	assert unionGraph.hasEdge("b", "c");  // The edge from "b" to "c" should exist
      
	assert !unionGraph.hasEdge("a", "c");  // There should not be an edge from "a" to "c"
	  }
      
      
      
	  public static void testSubGraph() {
	Graph g = new ListGraph();
	g.addNode("a");
	g.addNode("b");
	g.addNode("c");
	g.addNode("d");
	g.addEdge("a", "b");
	g.addEdge("b", "c");
	g.addEdge("c", "d");
      
	Set<String> subset = new HashSet<>(Arrays.asList("a", "b", "c"));
	Graph subGraph = g.subGraph(subset);
      
	assert subGraph.hasNode("a");  // Node "a" should be in the subGraph
	assert subGraph.hasNode("b");  // Node "b" should be in the subGraph
	assert subGraph.hasNode("c");  // Node "c" should be in the subGraph
	assert !subGraph.hasNode("d");  // Node "d" should not be in the subGraph
	  }
      
	  public static void testConnected() {
	Graph g = new ListGraph();
	g.addNode("a");
	g.addNode("b");
	g.addNode("c");
	g.addNode("d");
	g.addEdge("a", "b");
	g.addEdge("b", "c");
      
	assert g.connected("a", "c");  // There is a path from "a" to "c"
	assert !g.connected("a", "d");  // No path from "a" to "d"
	  }
      
      //////////////////////////////////////////////////////////////////////////////////////
      ///////////////////////////////// TESTS for PART 2 ///////////////////////////////////
      //////////////////////////////////////////////////////////////////////////////////////
      
      // test 2.1 
	  public static void testAddEdge2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
      
	Edge e1 = new Edge("a", "b");
	assert eg.addEdge(e1);  // Should return true, edge added successfully
      
	Edge e2 = new Edge("a", "b");
	assert !eg.addEdge(e2);  // Should return false, edge already exists
	  }
      
      // test 2.2
	  public static void testHasNode2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
      
	assert !eg.hasNode("a");  // Should return false, "a" has not been added yet
      
	eg.addEdge(new Edge("a", "b"));  // Adding an edge should add both nodes
	assert eg.hasNode("a");  // Should return true, "a" was added
	assert eg.hasNode("b");  // Should return true, "b" was added
	  }
      
      // test 2.3
	  public static void testHasEdge2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
      
	Edge e1 = new Edge("a", "b");
	assert !eg.hasEdge(e1);  // Should return false, edge has not been added yet
      
	eg.addEdge(e1);  // Adding edge
	assert eg.hasEdge(e1);  // Should return true, edge from "a" to "b" exists
      
	Edge e2 = new Edge("b", "c");
	assert !eg.hasEdge(e2);  // Should return false, edge "b" to "c" has not been added
	  }
      
      
      // test 2.4
	  public static void testRemoveEdge2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
      
	Edge e1 = new Edge("a", "b");
	eg.addEdge(e1);  // Add edge "a" -> "b"
	assert eg.hasEdge(e1);  // Ensure edge exists
      
	assert eg.removeEdge(e1);  // Should return true, edge removed
	assert !eg.hasEdge(e1);  // The edge should no longer exist
      
	Edge e2 = new Edge("b", "c");
	assert !eg.removeEdge(e2);  // Should return false, edge "b" -> "c" doesn't exist
	  }
      
      // test 2.5
	  public static void testOutEdges2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
      
	eg.addEdge(new Edge("a", "b"));
	eg.addEdge(new Edge("a", "c"));
      
	List<Edge> outEdges = eg.outEdges("a");
	assert outEdges.size() == 2;  // Should return 2 outgoing edges
	assert outEdges.contains(new Edge("a", "b"));  // Edge "a" -> "b" should exist
	assert outEdges.contains(new Edge("a", "c"));  // Edge "a" -> "c" should exist
      
	assert eg.outEdges("b").isEmpty();  // Node "b" has no outgoing edges
	  }
      
      // test 2.6 
	  public static void testInEdges2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
      
	eg.addEdge(new Edge("b", "a"));
	eg.addEdge(new Edge("c", "a"));
      
	List<Edge> inEdges = eg.inEdges("a");
	assert inEdges.size() == 2;  // Should return 2 incoming edges
	assert inEdges.contains(new Edge("b", "a"));  // Edge "b" -> "a" should exist
	assert inEdges.contains(new Edge("c", "a"));  // Edge "c" -> "a" should exist
      
	assert eg.inEdges("b").isEmpty();  // Node "b" has no incoming edges
	  }
      
      // test 2.7
	  public static void testEdges2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
      
	eg.addEdge(new Edge("a", "b"));
	eg.addEdge(new Edge("b", "c"));
	eg.addEdge(new Edge("c", "a"));
      
	List<Edge> allEdges = eg.edges();
	assert allEdges.size() == 3;  // Should return 3 edges
	assert allEdges.contains(new Edge("a", "b"));  // Edge "a" -> "b" should exist
	assert allEdges.contains(new Edge("b", "c"));  // Edge "b" -> "c" should exist
	assert allEdges.contains(new Edge("c", "a"));  // Edge "c" -> "a" should exist
	  }
      
	  public static void testUnion2() {
	Graph g1 = new ListGraph();
	EdgeGraph eg1 = new EdgeGraphAdapter(g1);
      
	eg1.addEdge(new Edge("a", "b"));
	eg1.addEdge(new Edge("b", "c"));
      
	Graph g2 = new ListGraph();
	EdgeGraph eg2 = new EdgeGraphAdapter(g2);
	eg2.addEdge(new Edge("c", "d"));
	eg2.addEdge(new Edge("d", "a"));
      
	EdgeGraph unionGraph = eg1.union(eg2);
      
	List<Edge> unionEdges = unionGraph.edges();
	assert unionEdges.size() == 4;  // Union should contain all 4 edges
	assert unionEdges.contains(new Edge("a", "b"));
	assert unionEdges.contains(new Edge("b", "c"));
	assert unionEdges.contains(new Edge("c", "d"));
	assert unionEdges.contains(new Edge("d", "a"));
	  }
      
	  public static void testHasPath2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
      
	eg.addEdge(new Edge("a", "b"));
	eg.addEdge(new Edge("b", "c"));
	eg.addEdge(new Edge("c", "d"));
      
	// Test valid path
	List<Edge> validPath = Arrays.asList(new Edge("a", "b"), new Edge("b", "c"), new Edge("c", "d"));
	assert eg.hasPath(validPath);  // Should return true
	  }
      
      
      
      
	  public static void main(String[] args) {
	
	    // test1();
	    testAddNode();
	    testHasEdge();
	    testHasNode();
	    testRemoveNode();
	    testRemoveEdge();
	    testNodes();
	    testSucc();
	    testPred();
	    testUnion();
	    testSubGraph();
      
	    // test2();
	    testAddEdge2();
	    testHasNode2();
	    testHasEdge2();
	    testRemoveEdge2();
	    testOutEdges2();
	    testInEdges2();
	    testEdges2();
	    testUnion2();
	    testHasPath2();
      
	    System.out.println("Success after all testcases!");
      
	  }

}