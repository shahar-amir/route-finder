package pathfinding;

import graph.Edge;
import graph.Node;

import java.util.Collections;
import java.util.List;

/**
 * Result of a path search.
 * Time is measured in seconds, distance in meters.
 */
public class Route {
    private final Node source;
    private final List<Edge> edges;
    private final double totalTimeSeconds;
    private final double totalDistanceMeters;

    public Route(Node source, List<Edge> edges) {
        this.source = source;
        this.edges = List.copyOf(edges);

        double time = 0;
        double distance = 0;
        for (Edge edge : this.edges) {
            time += edge.getWeight();
            distance += edge.getLengthMeters();
        }
        this.totalTimeSeconds = time;
        this.totalDistanceMeters = distance;
    }

    public Node getSource() { return source; }

    public Node getDestination() {
        if (edges.isEmpty()) {
            return source;
        } else {
            return edges.getLast().getTo();
        }
    }

    public List<Edge> getEdges() { return edges; }
    public double getTotalTimeSeconds() { return totalTimeSeconds; }
    public double getTotalDistanceMeters() { return totalDistanceMeters; }
}