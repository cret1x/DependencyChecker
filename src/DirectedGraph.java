import java.util.ArrayList;
import java.util.List;

public final class DirectedGraph {

    private List<Vertex> vertices;

    public DirectedGraph() {
        vertices = new ArrayList<>();
    }

    public void addVertex(Vertex v) {
        vertices.add(v);
    }
    public void addEdge(Vertex from, Vertex to) {
        from.addNeighbor(to);
    }

    public List<Vertex> getVertices() {
        return vertices;
    }

    private boolean hasCycle(Vertex src) {
        src.setBeingVisited(true);
        for (var n: src.getAdjacencyList()) {
            if (n.isBeingVisited()) {
                return true;
            } else if (!n.isVisited() && hasCycle(n)) {
                return true;
            }
        }
        src.setBeingVisited(false);
        src.setVisited(true);
        return false;
    }

    public boolean hasCycle() {
        for (var vertex : vertices) {
            if (!vertex.isVisited() && hasCycle(vertex)) {
                return true;
            }
        }
        return false;
    }
}
