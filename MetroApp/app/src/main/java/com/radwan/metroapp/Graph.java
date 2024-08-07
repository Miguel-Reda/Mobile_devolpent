
package com.radwan.metroapp;

import android.os.Build;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

public class Graph {
    private ArrayList<Vertex> vertices;
    private ArrayList<Vertex> visitedVertices = new ArrayList<Vertex>();
    private ArrayList<String> paths = new ArrayList<String>();

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
            if (v.getData().equalsIgnoreCase(data)) {
                return v;
            }
        }
        return null;
    }


    public void addEdge(Vertex vertex1, Vertex vertex2) {
        vertex1.addEdge(vertex2);
        vertex2.addEdge(vertex1);
    }
    
    private void depthFirstTraversal(Vertex start, Vertex end, String path) {
        visitedVertices.add(start);
        path += start.getData() + ", ";
        if (start == end) {
            path = path.substring(0, path.length() - 2);
            paths.add(path);
            visitedVertices.remove(end);
            return;
        }
        for (Edge e : start.getEdges()) {
            Vertex neighbor = e.getEnd();
            if (!visitedVertices.contains(neighbor)) {
                depthFirstTraversal(neighbor, end, path);
            }
        }
        visitedVertices.remove(start);
    }

    public ArrayList<String> getAllPaths(Vertex start, Vertex end) {
        paths.clear();
        visitedVertices.clear();
        depthFirstTraversal(start, end, "");
        paths.sort((String path1, String path2) -> {
            return path1.split(", ").length - path2.split(", ").length;
        });

        return paths;
    }

}

