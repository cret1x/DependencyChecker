import java.util.ArrayList;
import java.util.List;

/**
 * Vertex class
 */
public final class Vertex {

    private final String path;
    private boolean beingVisited;
    private boolean visited;
    private List<Vertex> adjacencyList;

    /**
     * Vertex constructor
     * @param path Path of file it represents
     */
    public Vertex(String path) {
        this.path = path;
        this.adjacencyList = new ArrayList<>();
    }

    /**
     * Adds other vertex as dependency
     * @param adjacent Other vertex
     */
    public void addDependency(Vertex adjacent) {
        this.adjacencyList.add(adjacent);
    }

    /**
     * Getter for path value
     * @return Path
     */
    public String getPath() {
        return path;
    }

    /**
     * Getter for being visited value
     * @return If being visited
     */
    public boolean isBeingVisited() {
        return beingVisited;
    }

    /**
     * Getter for is visited value
     * @return If is visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Getter for adj list
     * @return List of all dependencies
     */
    public List<Vertex> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Setter for being visited value
     * @param beingVisited If being visited
     */
    public void setBeingVisited(boolean beingVisited) {
        this.beingVisited = beingVisited;
    }

    /**
     * Setter for is visited value
     * @param visited If is visited
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}