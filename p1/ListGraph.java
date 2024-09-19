import java.util.*;

public class ListGraph implements Graph {
    private HashMap<String, LinkedList<String>> nodes = new HashMap<>();

    public boolean addNode(String n) {
        if (nodes.containsKey(n)) {
            return false;
        }
        nodes.put(n, new LinkedList<>());
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
        }
        return false;
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
        List<String> s = new LinkedList<>();
	    for (String n : nodes.keySet()) {
            s.add(n);
        }
        return s;
    }

    public List<String> succ(String n) {
	    if (!nodes.containsKey(n)) {
            throw new NoSuchElementException();
        }
        List<String> s = new LinkedList<>();
        for (String string : nodes.get(n)) {
            s.add(string);
        }
        return s;
    }

    public List<String> pred(String n) {
	    if (!nodes.containsKey(n)) {
            throw new NoSuchElementException();
        }
        List<String> s = new LinkedList<>();
        for (String n2 : nodes.keySet()) {
            if (nodes.get(n2).contains(n)) {
                s.add(n2);
            }
        }
        return s;
    }

    public Graph union(Graph g) {
        Graph newGraph = new ListGraph();

        List<String> gNodes = g.nodes();
        for (String s : nodes.keySet()) {
            newGraph.addNode(s);
        }
        for (String s : gNodes) {
            if (!newGraph.hasNode(s)) {
                newGraph.addNode(s);
            }
        }

        for (String s : newGraph.nodes()) {
            for (String s1 : nodes.get(s)) {
                newGraph.addEdge(s, s1);
            }
            for (String s2 : g.succ(s)) {
                if (!newGraph.hasEdge(s, s2)) {
                    newGraph.addEdge(s, s2);
                }
            }
        }

        return newGraph;
    }

    public Graph subGraph(Set<String> newNodes) {
	    Graph newGraph = new ListGraph();
       
        for (String s : newNodes) {
            if (nodes.keySet().contains(s)) {
                newGraph.addNode(s);
            }
        }
        for (String s : nodes.keySet()) {
            if (newGraph.hasNode(s)) {
                for (String s1 : nodes.get(s)) {
                    if (newGraph.hasNode(s1)) {
                        newGraph.addEdge(s, s1);
                    }
                }
            }
        }

        return newGraph;
    }

    public boolean connected(String n1, String n2) {
	    if (!nodes.keySet().contains(n1) || !nodes.keySet().contains(n2)) {
            throw new NoSuchElementException();
        }
        Set<String> checkNodes = new HashSet<>();
        return connectedCheck(n1, n2, checkNodes);
    }

    boolean connectedCheck(String n1, String n2, Set<String> nSet) {
        nSet.add(n1);
        if (nodes.get(n1).contains(n2)) {
            return true;
        }

        for (String s : nodes.get(n1)) {
            if (!nSet.contains(s)) {
                boolean connected = connectedCheck(s, n2, nSet);
                if (connected) {
                    return true;
                }
            }
        }

        return false;
    }
}
