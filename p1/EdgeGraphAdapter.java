import java.util.*;

public class EdgeGraphAdapter implements EdgeGraph {

    private Graph g;

    public EdgeGraphAdapter(Graph g) { this.g = g; }

    public boolean addEdge(Edge e) {
	     throw new UnsupportedOperationException();
    }

    public boolean hasNode(String n) {
	     throw new UnsupportedOperationException();
    }

    public boolean hasEdge(Edge e) {
	     throw new UnsupportedOperationException();
    }

    public boolean removeEdge(Edge e) {
	     throw new UnsupportedOperationException();
    }

    public List<Edge> outEdges(String n) {
      throw new UnsupportedOperationException();
    }

    public List<Edge> inEdges(String n) {
      throw new UnsupportedOperationException();
    }

    public List<Edge> edges() {
      throw new UnsupportedOperationException();
    }

    public EdgeGraph union(EdgeGraph g) {
      throw new UnsupportedOperationException();
    }

    public boolean hasPath(List<Edge> e) {
	     throw new UnsupportedOperationException();
    }

}
