package graph;

import java.util.*;

public class Graph {
    private final Map<Node, List<Edge>> adjacency = new HashMap<>();

    public void addNode(Node node){
        adjacency.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node from, Node to, double weight, double lengthMeters, String streetName){
        addNode(from);
        addNode(to);
        Edge edge = new Edge(from, to, weight, lengthMeters, streetName);
        adjacency.get(from).add(edge);
    }

    public List<Edge> getNeighbors(Node node) {
        return adjacency.getOrDefault(node, Collections.emptyList());
    }

    public Set<Node> getNodes() {
        return adjacency.keySet();
    }

    public int size() {
        return adjacency.size();
    }

}
