import java.util.*;

public class Main {

    // Run "java -ea Main" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)
    
    	public static void testAddNode() {
		Graph g = new ListGraph();

		assert g.addNode("a");
		assert !g.addNode("a");
		assert g.hasNode("a");
	}
      
	public static void testHasNode() {
		Graph g = new ListGraph();
		assert !g.hasNode("a"); 

		g.addNode("a");
		assert g.hasNode("a"); 
      
		g.removeNode("a");
		assert !g.hasNode("a");
	}
      
	public static void testHasEdge() {
		Graph g = new ListGraph();
		g.addNode("a");
		g.addNode("b");
      
		assert !g.hasEdge("a", "b");

		g.addEdge("a", "b");
		assert g.hasEdge("a", "b");
	
		g.removeEdge("a", "b");
		assert !g.hasEdge("a", "b");
	}
      
	public static void testRemoveNode() {
		Graph g = new ListGraph();
		g.addNode("a");
		g.addNode("b");
		g.addEdge("a", "b");
	
		assert g.hasNode("a");
		assert g.removeNode("a"); 
		assert !g.hasNode("a");
		assert !g.hasEdge("a", "b");
	
		assert !g.removeNode("c");
	}
      
	public static void testRemoveEdge() {
		Graph g = new ListGraph();

		g.addNode("a");
		g.addNode("b");
		g.addEdge("a", "b");
	
		assert g.hasEdge("a", "b"); 
		assert g.removeEdge("a", "b"); 
		assert !g.hasEdge("a", "b"); 
	}

	public static void testNodes() {
		Graph g = new ListGraph();
		g.addNode("a");
		g.addNode("b");
		g.addNode("c");
	
		List<String> nodeList = g.nodes();
		assert nodeList.contains("a"); 
		assert nodeList.contains("b"); 
		assert nodeList.contains("c");  
	
		g.removeNode("b");
		nodeList = g.nodes();
		assert !nodeList.contains("b");  
	}
      
	public static void testSucc() {
		Graph g = new ListGraph();
		g.addNode("a");
		g.addNode("b");
		g.addNode("c");
		g.addEdge("a", "b");
		g.addEdge("a", "c");
	
		List<String> successors = g.succ("a");
		assert successors.size() == 2;  
		assert successors.contains("b");  
		assert successors.contains("c");
	
		boolean exceptionThrown = false;
		try {
			g.succ("d");  
		} catch (NoSuchElementException e) {
			exceptionThrown = true;
		}
		assert exceptionThrown; 
	}

	public static void testPred() {
		Graph g = new ListGraph();
		g.addNode("a");
		g.addNode("b");
		g.addNode("c");
		g.addEdge("b", "a");
		g.addEdge("c", "a");
	
		List<String> predecessors = g.pred("a");
		assert predecessors.size() == 2;
		assert predecessors.contains("b");
		assert predecessors.contains("c");
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
	
		assert unionGraph.hasNode("a");  
		assert unionGraph.hasNode("b"); 
		assert unionGraph.hasNode("c"); 

		assert unionGraph.hasEdge("a", "b"); 
		assert unionGraph.hasEdge("b", "c"); 
		assert !unionGraph.hasEdge("a", "c");  
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
	
		assert subGraph.hasNode("a");  
		assert subGraph.hasNode("b"); 
		assert subGraph.hasNode("c"); 
		assert !subGraph.hasNode("d"); 
	}
      
	public static void testConnected() {
		Graph g = new ListGraph();
		g.addNode("a");
		g.addNode("b");
		g.addNode("c");
		g.addNode("d");
		g.addEdge("a", "b");
		g.addEdge("b", "c");
	
		assert g.connected("a", "c");
		assert !g.connected("a", "d");
	}
      


	public static void testAddEdge2() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
	
		Edge e1 = new Edge("a", "b");
		assert eg.addEdge(e1);
	
		Edge e2 = new Edge("a", "b");
		assert !eg.addEdge(e2);
	}

	public static void testHasNode2() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
	
		assert !eg.hasNode("a"); 
	
		eg.addEdge(new Edge("a", "b")); 
		assert eg.hasNode("a");  
		assert eg.hasNode("b"); 
	}

	public static void testHasEdge2() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
	
		Edge e1 = new Edge("a", "b");
		assert !eg.hasEdge(e1); 
	
		eg.addEdge(e1); 
		assert eg.hasEdge(e1); 
	
		Edge e2 = new Edge("b", "c");
		assert !eg.hasEdge(e2); 
	}

	public static void testRemoveEdge2() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
	
		Edge e1 = new Edge("a", "b");
		eg.addEdge(e1); 
		assert eg.hasEdge(e1);  
	
		assert eg.removeEdge(e1); 
		assert !eg.hasEdge(e1); 
	
		Edge e2 = new Edge("b", "c");
		assert !eg.removeEdge(e2); 
	}

	public static void testOutEdges() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
	
		eg.addEdge(new Edge("a", "b"));
		eg.addEdge(new Edge("a", "c"));
	
		List<Edge> outEdges = eg.outEdges("a");
		assert outEdges.size() == 2; 
		assert outEdges.contains(new Edge("a", "b"));  
		assert outEdges.contains(new Edge("a", "c"));  
	
		assert eg.outEdges("b").isEmpty(); 
	}
      
	public static void testInEdges() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
	
		eg.addEdge(new Edge("b", "a"));
		eg.addEdge(new Edge("c", "a"));
	
		List<Edge> inEdges = eg.inEdges("a");
		assert inEdges.size() == 2; 
		assert inEdges.contains(new Edge("b", "a"));  
		assert inEdges.contains(new Edge("c", "a")); 
	
		assert eg.inEdges("b").isEmpty(); 
	}
      
	public static void testEdges() {
		Graph g = new ListGraph();
		EdgeGraph eg = new EdgeGraphAdapter(g);
	
		eg.addEdge(new Edge("a", "b"));
		eg.addEdge(new Edge("b", "c"));
		eg.addEdge(new Edge("c", "a"));
	
		List<Edge> allEdges = eg.edges();
		assert allEdges.size() == 3;  
		assert allEdges.contains(new Edge("a", "b")); 
		assert allEdges.contains(new Edge("b", "c")); 
		assert allEdges.contains(new Edge("c", "a"));  
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
		assert unionEdges.size() == 4;  
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

		List<Edge> validPath = Arrays.asList(new Edge("a", "b"), new Edge("b", "c"), new Edge("c", "d"));
		assert eg.hasPath(validPath);  
	}
      
      
	  public static void main(String[] args) {
	
	    // ListGraph
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
      
	    // EdgeGraphAdapter
	    testAddEdge2();
	    testHasNode2();
	    testHasEdge2();
	    testRemoveEdge2();
	    testOutEdges();
	    testInEdges();
	    testEdges();
	    testUnion2();
	    testHasPath2();
      
	    System.out.println("Passes all tests");
	  }
}