package maps;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads a map file and builds a Graph out of it.
 * File format:
 *   N,id,name,lat,lon
 *   E,fromId,toId,streetName,roadType,bidirectional
 * Blank lines and lines starting with '#' are ignored.
 */
public class MapTransformer {

    private final Path filePath;

    /** Stores the path only. The file is not read until buildGraph() is called. */
    public MapTransformer(String filePath) {
        this.filePath = Path.of(filePath);
    }

    /**
     * Reads the file and builds the graph.
     * Two passes: first all nodes, then all edges
     * (an Edge needs real Node objects, so every node must exist first).
     */
    public Graph buildGraph() throws IOException {
        List<String> lines = readCleanLines();

        Map<String, Node> nodesById = new HashMap<>();
        Graph graph = new Graph();

        // ---- Pass 1: nodes ----
        for (String line : lines) {
            String[] parts = line.split(",");
            if (!parts[0].equals("N")) {
                continue;
            }
            // parts = [N, id, name, lat, lon]
            Node node = new Node(parts[1],parts[2],Double.parseDouble(parts[3]),Double.parseDouble(parts[4]));
            nodesById.put(parts[1],node);
            graph.addNode(node);
        }

        // ---- Pass 2: edges ----
        for (String line : lines) {
            String[] parts = line.split(",");
            if (!parts[0].equals("E")) {
                continue;
            }
            // parts = [E,fromId,toId,streetName,roadType,bidirectional]
            Node node1 = nodesById.get(parts[1]);
            Node node2 = nodesById.get(parts[2]);
            if (node1 == null || node2 == null) {
                throw new IllegalArgumentException("Unknown node id in edge line: " + line);
            }
            double metersBetweenNodes = haversineMeters(node1.getLat(), node1.getLon(),node2.getLat(),node2.getLon())*1.3; //meters*1.3 to match road curvature
            int speed = speedByRoadType(parts[4]);
            double weightMinutes = ((metersBetweenNodes/1000)/speed)*60;
            Edge edge = new Edge(node1,node2,weightMinutes,metersBetweenNodes,parts[3]);
            // TODO: Add edge and edgeBack to the graph after Shahar will finish addEdge
            if (Boolean.parseBoolean(parts[5])){
                Edge edgeBack = new Edge(node2,node1,weightMinutes,metersBetweenNodes,parts[3]);
            }
        }

        return graph;
    }

    /**
     * Reads every line of the file, trims whitespace,
     * and drops blank lines and comment lines.
     */
    private List<String> readCleanLines() throws IOException {
        List<String> raw = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        List<String> clean = new ArrayList<>();

        for (String line : raw) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                continue;
            }
            clean.add(trimmed);
        }
        return clean;
    }

    /**
     * Straight-line distance between two points on Earth, in meters.
     * Used to fill the lengthMeters field of an Edge.
     */
    public static double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        final double EARTH_RADIUS_METERS = 6_371_000.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS_METERS * c;
    }

    public static int speedByRoadType(String type){
        return switch (type){
            case "local" -> 50;
            case "regional" -> 60;
            case "intercity" -> 90;
            case "highway" -> 110;
            default -> throw new IllegalArgumentException("Unknown road type: " + type);
        };
    }
}