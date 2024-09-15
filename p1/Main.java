public class Main {

    // Run "java -ea Main" to run with assertions enabled (If you run
    // with assertions disabled, the default, then assert statements
    // will not execute!)
    
    public static void test1() {
	Graph g = new ListGraph();
	assert g.addNode("a");
	assert g.hasNode("a");
    }

    public static void test2() {
	Graph g = new ListGraph();
	EdgeGraph eg = new EdgeGraphAdapter(g);
	Edge e = new Edge("a", "b");
	assert eg.addEdge(e);
	assert eg.hasEdge(e);
    }
    
    public static void main(String[] args) {
	test1();
	test2();
    }

}