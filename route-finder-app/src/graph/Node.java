package graph;

import java.util.Objects;

public class Node {
    private final String id;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Objects.equals(id, node.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public Node(String id){
        this.id = id;
    }

    public String getId(){return id;}
}
