import java.util.*;

public class ListGraph implements Graph {
    private HashMap<String, LinkedList<String>> nodes = new HashMap<>();

    public boolean addNode(String n) {
        if (nodes.containsKey(n)) {
            return false;
        }
        nodes.put(n, new LinkedList<String>());
        return true;
    }

    public boolean addEdge(String n1, String n2) {
	    if (nodes.get(n1).contains(n2)) {
            return false;
        }
        nodes.get(n1).add(n2);
        return true;
    }

    public boolean hasNode(String n) {
	    if (nodes.containsKey(n)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasEdge(String n1, String n2) {
	    if (nodes.get(n1).contains(n2)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean removeNode(String n) {
	    if (!nodes.containsKey(n)) {
            return false;
        }
        nodes.remove(n);
        return true;
    }

    public boolean removeEdge(String n1, String n2) {
        if (!nodes.containsKey(n1) || !nodes.containsKey(n2)) {
            throw new NoSuchElementException();
        } else if (!nodes.get(n1).contains(n2)) {
            return false;
        }
        nodes.get(n1).remove(n2);
        return true;
    }

    public List<String> nodes() {
        LinkedList<String> s = new LinkedList<>();
	    for (String n : nodes.keySet()) {
            s.addLast(n);
        }
        return s;
    }

    public List<String> succ(String n) {
	     throw new UnsupportedOperationException();
    }

    public List<String> pred(String n) {
	     throw new UnsupportedOperationException();
    }

    public Graph union(Graph g) {
	     throw new UnsupportedOperationException();
    }

    public Graph subGraph(Set<String> nodes) {
	     throw new UnsupportedOperationException();
    }

    public boolean connected(String n1, String n2) {
	     throw new UnsupportedOperationException();
    }
}
