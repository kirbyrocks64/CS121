import java.util.*;

public class EdgeGraphAdapter implements EdgeGraph {

    private Graph g;

    public EdgeGraphAdapter(Graph g) { this.g = g; }

    public boolean addEdge(Edge e) {
	    String source = e.getSrc();
      String dest = e.getDst();

      g.addNode(source); g.addNode(dest);
      if (g.hasEdge(source, dest)) {
        return false;
      }
      g.addEdge(source, dest);
      return true;
    }

    public boolean hasNode(String n) {
      if (g.hasNode(n)) {
        return true;
      }
      return false;
    }

    public boolean hasEdge(Edge e) {
	    String source = e.getSrc();
      String dest = e.getDst();

      if (!g.hasEdge(source, dest)) {
        return false;
      }
      return true;
    }

    public boolean removeEdge(Edge e) {
	    String source = e.getSrc();
      String dest = e.getDst();

      if (!g.hasEdge(source, dest)) {
        return false;
      }
      g.removeEdge(source, dest);
      return true;
    }

    public List<Edge> outEdges(String n) {
      List<Edge> out = new LinkedList<>();
      List<String> ends = g.succ(n);
      for (String s : ends) {
        Edge newEdge = new Edge(n, s);
        out.add(newEdge);
      }
      return out;
    }

    public List<Edge> inEdges(String n) {
      List<Edge> out = new LinkedList<>();
      List<String> starts = g.pred(n);
      for (String s : starts) {
        Edge newEdge = new Edge(s, n);
        out.add(newEdge);
      }
      return out;
    }

    public List<Edge> edges() {
      List<Edge> out = new LinkedList<>();
      List<String> nodes = g.nodes();
      for (String s : nodes) {
        List<String> ends = g.succ(s);
        for (String s1 : ends) {
          Edge newEdge = new Edge(s, s1);
          out.add(newEdge);
        }
      }
      return out;
    }

    public EdgeGraph union(EdgeGraph g) {
      Graph newListGraph = new ListGraph();
      EdgeGraph newGraph = new EdgeGraphAdapter(newListGraph);

      List<String> localNodes = this.g.nodes();
      for (String s : localNodes) {
        List<String> ends = this.g.succ(s);
        for (String s1 : ends) {
          Edge newEdge = new Edge(s, s1);
          newGraph.addEdge(newEdge);
        }
      }

      for (Edge e : g.edges()) {
        if (!newGraph.hasEdge(e)) {
          newGraph.addEdge(e);
        }
      }

      return newGraph;
    }

    public boolean hasPath(List<Edge> l) {
      EdgeGraph edgeGraph = new EdgeGraphAdapter(g);
      for (Edge e : l) {
        if (!edgeGraph.hasEdge(e)) {
          return false;
        }
      }

      for (int i = 1; i < l.size(); i++) {
        if (!l.get(i - 1).getDst().equals(l.get(i).getSrc())) {
          throw new BadPath();
        }
      }

      return true;
    }
}
