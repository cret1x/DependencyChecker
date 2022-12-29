import java.util.ArrayList;
import java.util.List;

/**
 * Class for graph to store dependencies
 */
public final class DirectedGraph {

    private List<Vertex> vertices;

    /**
     * Main constructor
     */
    public DirectedGraph() {
        vertices = new ArrayList<>();
    }

    /**
     * Adds new vertex to graph
     * @param v Vertex to add
     */
    public void addVertex(Vertex v) {
        vertices.add(v);
    }

    /**
     * Creates connection between 2 vertices
     * @param from Vertex from
     * @param to Vertex to
     */
    public void addEdge(Vertex from, Vertex to) {
        from.addDependency(to);
    }

    /**
     * Returns list of all vertices
     * @return List of vertices
     */
    public List<Vertex> getVertices() {
        return vertices;
    }

    /**
     * Function to find looooops in graph
     * @param src Vertex to start algo from
     * @return Pair of status of 'is loop' and conflicting vertices
     */
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

    /**
     * Wrapper function to start finding loops from each of vertices
     * @return Pair of status of 'is loop' and conflicting vertices
     */
    public Pair<Boolean, Pair<Vertex, Vertex>> hasCycle() {
        for (var vertex : vertices) {
            if (!vertex.isVisited() && hasCycle(vertex).first()) {
                return hasCycle(vertex);
            }
        }
        return new Pair<>(false, new Pair<>(null, null));
    }
}
