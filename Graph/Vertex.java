package Graph;
import java.util.ArrayList;

public class Vertex {
    private String data;
    private ArrayList<Edge> edges;

    // Getters and Setters
    public void setData(String data) {
        this.data = data;
    }
    public String getData() {
        return this.data;
    }
    
    public ArrayList<Edge> getEdges() {
        return this.edges;
    }

    // Constructors
    public Vertex() {
        this.edges = new ArrayList<Edge>();
    }
    public Vertex(String inputData) {
        this.data = inputData;
        this.edges = new ArrayList<Edge>();
    }
    
    public void addEdge(Vertex endVertex) {
        this.edges.add(new Edge(this, endVertex));
    }
}
