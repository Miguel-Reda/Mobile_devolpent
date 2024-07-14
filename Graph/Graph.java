package Graph;

import java.util.ArrayList;

public class Graph {
    private ArrayList<Vertex> vertices;
    private static ArrayList<Vertex> visitedVertices = new ArrayList<Vertex>();
    private static ArrayList<String> paths = new ArrayList<String>();

    public Graph() {
        this.vertices = new ArrayList<Vertex>();
    }

    public Vertex addVertex(String data) {
        Vertex newVertex = new Vertex(data);
        this.vertices.add(newVertex);
        return newVertex;
    }

    public Vertex getVertex(String data) {
        for (Vertex v : this.vertices) {
//            System.out.println(v.getData());
            if (v.getData().equalsIgnoreCase(data)) {
                return v;
            }
        }
        return null;
    }

    public ArrayList<Vertex> getVertices() {
        return this.vertices;
    }

    public void addEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.addEdge(vertex2);
        vertex2.addEdge(vertex1);
    }
    
    private static void depthFirstTraversal(Vertex start, Vertex end, String path) {
        visitedVertices.add(start);
        path += start.getData() + ", ";
        if (start == end) {
            paths.add(path);
            visitedVertices.remove(end);
            return;
        }
        for (Edge e : start.getEdges()) {
            Vertex neighbor = e.getEnd();
            if (!visitedVertices.contains(neighbor)) {
                Graph.depthFirstTraversal(neighbor, end, path);
            }
        }
        visitedVertices.remove(start);
    }

    public ArrayList<String> getAllPaths(Vertex start, Vertex end) {
        String path = "";
        depthFirstTraversal(start, end, path);
        return paths;
    }
}
