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

    private Pair<Boolean, Pair<Vertex, Vertex>> hasCycle(Vertex src) {
        src.setBeingVisited(true);
        for (var n: src.getAdjacencyList()) {
            if (n.isBeingVisited()) {
                return new Pair<>(true, new Pair<>(src, n));
            } else if (!n.isVisited() && hasCycle(n).first()) {
                return new Pair<>(true, new Pair<>(src, n));
            }
        }
        src.setBeingVisited(false);
        src.setVisited(true);
        return new Pair<>(false, new Pair<>(src, src));
    }

    public Pair<Boolean, Pair<Vertex, Vertex>> hasCycle() {
        for (var vertex : vertices) {
            if (!vertex.isVisited() && hasCycle(vertex).first()) {
                return hasCycle(vertex);
            }
        }
        return new Pair<>(false, new Pair<>(null, null));
    }
}
