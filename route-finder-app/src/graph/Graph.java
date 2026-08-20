package graph;

import java.util.*;

public class Graph {
    private final Map<Node, List<Edge>> adjacency = new HashMap<>();

    public void addNode(Node node){
        adjacency.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node from, Node to, double weight, double lengthMeters, String streetName) {
        Objects.requireNonNull(from, "from must not be null");
        Objects.requireNonNull(to, "to must not be null");
        if (weight < 0) {
            throw new IllegalArgumentException("Travel time must not be negative");
        }
        addNode(from);
        addNode(to);
        adjacency.get(from).add(new Edge(from, to, weight, lengthMeters, streetName));
    }

    public void addBidirectionalEdge(Node a, Node b, double travelTime,
                                     double lengthMeters, String streetName) {
        addEdge(a, b, travelTime, lengthMeters, streetName);
        addEdge(b, a, travelTime, lengthMeters, streetName);
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
