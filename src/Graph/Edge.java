package Graph;
public class Edge {
    private Vertex startVertex;
    private Vertex endVertex;

    // Getters and Setters
    public Vertex getStart() {
        return startVertex;
    }
    public void setStart(Vertex startVertex) {
        this.startVertex = startVertex;
    }
    public Vertex getEnd() {
        return endVertex;
    }
    public void setEnd(Vertex endVertex) {
        this.endVertex = endVertex;
    }

    // Constructors
    public Edge() {
    }
    public Edge(Vertex startVertex, Vertex endVertex) {
        this.startVertex = startVertex;
        this.endVertex = endVertex;
    }
}
