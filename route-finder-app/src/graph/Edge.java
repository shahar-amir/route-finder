package graph;

public class Edge {
    private final Node from;
    private final Node to;
    private final double weight;
    private final double lengthMeters;
    private final String streetName;

    public Edge(Node from, Node to, double weight,
                double lengthMeters, String streetName) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.lengthMeters = lengthMeters;
        this.streetName = streetName;
    }

    public Node getFrom() { return from; }
    public Node getTo() { return to; }
    public double getWeight() { return weight; }
    public double getLengthMeters() { return lengthMeters; }
    public String getStreetName() { return streetName; }

    @Override
    public String toString() {
        return from.getId() + " -> " + to.getId()
                + " (" + streetName + ", " + weight + ")";
    }
}
