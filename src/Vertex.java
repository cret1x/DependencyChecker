import java.util.ArrayList;
import java.util.List;

public final class Vertex {

    private final String path;
    private boolean beingVisited;
    private boolean visited;
    private List<Vertex> adjacencyList;

    public Vertex(String label) {
        this.path = label;
        this.adjacencyList = new ArrayList<>();
    }

    public void addNeighbor(Vertex adjacent) {
        this.adjacencyList.add(adjacent);
    }

    public String getPath() {
        return path;
    }

    public boolean isBeingVisited() {
        return beingVisited;
    }

    public boolean isVisited() {
        return visited;
    }

    public List<Vertex> getAdjacencyList() {
        return adjacencyList;
    }

    public void setBeingVisited(boolean beingVisited) {
        this.beingVisited = beingVisited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}