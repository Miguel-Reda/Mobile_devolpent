import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import Graph.Graph;
import Graph.Vertex;

public class Main1 {
    public static void main(String[] args) {
        Scanner iScanner = new Scanner(System.in);
        ArrayList<String> line1Stations = new ArrayList<String>();
        Collections.addAll(line1Stations, "Helwan", "Ain Helwan", "Helwan University",
                "Wadi Hof", "Hadayek Helwan", "El-Maasara", "Tora El-Asmant", "Kolet El-Maadi",
                "Tora El-Balad", "Sakanat El-Maadi", "Maadi", "Hadayek El-Maadi", "Dar El-Salam",
                "Zahraa El-Maadi", "Mar Girgis", "El-Malek El-Saleh", "Sayeda Zeinab", "Saad Zaghloul",
                "Sadat", "Nasser", "Orabi", "Al-Shohadaa", "Ghamra", "El-Demerdash", "Manshiet El-Sadr",
                "Kobri El-Qobba", "Hammamat El-Qobba", "Saray El-Qobba", "Hadayek El-Zaitoun",
                "Helmeyet El-Zaitoun", "El-Matareyya", "Ain Shams", "Ezbet El-Nakhl", "El-Marg",
                "New El-Marg");

        ArrayList<String> line2Stations = new ArrayList<String>();
        Collections.addAll(line2Stations, "Shubra El-Kheima", "Kolleyet El-Zeraa",
                "El-Mazallat", "El-Khalafawi", "Saint Teresa", "Rod El-Farag", "Massara", "Al-Shohadaa",
                "Attaba", "Mohamed Naguib", "Sadat", "Opera", "Dokki", "El Bohoth", "Cairo University",
                "Faisal", "Giza", "Omm El-Misryeen", "Sakiat Mekki", "El-Mounib");

        ArrayList<String> line3Stations = new ArrayList<String>();
        Collections.addAll(line3Stations, "Adly Mansour", "El Haykestep", "Omar Ibn El Khattab",
                "Qobaa", "Hesham Barakat", "El Nozha", "Nadi El Shams", "Alf Maskan", "Heliopolis", "Haroun",
                "Al Ahram", "Koleyet El Banat", "Stadium", "Fair Zone", "Abbassia", "Abdou Pasha", "El Geish",
                "Bab El Shaaria", "Attaba", "Nasser", "Maspero", "Zamalek", "Kit Kat", "Sudan", "Imbaba",
                "El Bohy", "Ring Road", "Rod El Farag");
        Graph graph = new Graph();
        addVertices(graph, line1Stations);
        addVertices(graph, line2Stations);
        addVertices(graph, line3Stations);
        
        System.out.println("Enter your start station:");
        String startStation = iScanner.nextLine();

        System.out.println("Enter your end station:");
        String endStation = iScanner.nextLine();

        if(!line1Stations.contains(startStation) && !line2Stations.contains(startStation) && !line3Stations.contains(startStation)) {
            System.out.println("Invalid start station, please try again.");
            return;
        }

        if(!line1Stations.contains(endStation) && !line2Stations.contains(endStation) && !line3Stations.contains(endStation)) {
            System.out.println("Invalid end station, please try again.");
            return;
        }



        Vertex start = graph.getVertex(startStation);
        Vertex end = graph.getVertex(endStation);

        String shortestPath ="";
        int shortestPathLength = Integer.MAX_VALUE;
        ArrayList<String> paths = graph.getAllPaths(start, end);
        for (int i = 0; i < paths.size(); i++){
            paths.set(i, paths.get(i).substring(0, paths.get(i).length() - 2));
            System.out.println("Path " + (i + 1) + ": " + paths.get(i));
            if(paths.get(i).split(", ").length < shortestPathLength){
                shortestPath = paths.get(i);
                shortestPathLength = paths.get(i).split(", ").length;
            }
        }
        System.out.println("Shortest Path: " + shortestPath);
        System.out.println("Shortest Path Length: " + shortestPathLength);
        
    }

    // Add vertices to the graph
    public static void addVertices(Graph graph, ArrayList<String> lineStations) {
        for (int i = 0; i < lineStations.size(); i++) {
            graph.addVertex(lineStations.get(i));
            if (i > 0) {
                graph.addEdge(graph.getVertex(lineStations.get(i - 1)), graph.getVertex(lineStations.get(i)));
            }
        }
    }

    public static void printDirectResults(ArrayList<String> lineStations, String startStation, String endStation) {
        // Calculate the start and end index
        byte startIndex = (byte) lineStations.indexOf(startStation);
        byte endIndex = (byte) lineStations.indexOf(endStation);
        if (startIndex > endIndex) {
            Collections.reverse(lineStations);
            startIndex = (byte) lineStations.indexOf(startStation);
            endIndex = (byte) lineStations.indexOf(endStation);
        }

        // Calculate the direction
        String direction = "";
        direction = lineStations.get(endIndex);

        // Calculate the ticket price
        byte numberOfStations = (byte) Math.abs(endIndex - startIndex);
        byte ticketPrice = 0;
        if (numberOfStations < 10) {
            ticketPrice = 5;
        } else {
            ticketPrice = 7;
        }

        System.out.println("Direction: " + direction);
        System.out.println("Ticket Price: " + ticketPrice);
    }
}
