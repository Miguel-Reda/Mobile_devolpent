import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import Graph.Graph;
import Graph.Vertex;

public class Main {
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

        // Fail fast (break early)
        if (startStation.equals(endStation)) {
            System.out.println("You are already at your destination.");
            return;
        }
        if (!line1Stations.contains(startStation) && !line2Stations.contains(startStation) && !line3Stations.contains(startStation)) {
            System.out.println("Invalid start station, please try again.");
            return;
        }
        if (!line1Stations.contains(endStation) && !line2Stations.contains(endStation) && !line3Stations.contains(endStation)) {
            System.out.println("Invalid end station, please try again.");
            return;
        }

        Vertex start = graph.getVertex(startStation);
        Vertex end = graph.getVertex(endStation);

        String shortestPath = "";
        int shortestPathLength = Integer.MAX_VALUE;

        // get all paths from start to end
        ArrayList<String> paths = graph.getAllPaths(start, end);

        // print all paths and find the shortest path
        for (int i = 0; i < paths.size(); i++) {
            paths.set(i, paths.get(i).substring(0, paths.get(i).length() - 2));
            System.out.println("Path " + (i + 1) + ": " + paths.get(i));
            if (paths.get(i).split(", ").length < shortestPathLength) {
                shortestPath = paths.get(i);
                shortestPathLength = paths.get(i).split(", ").length;
            }
        }
        System.out.println("Shortest Path: " + shortestPath);
        System.out.println("Shortest Path Length: " + shortestPathLength + " stations");
        System.out.println("If you want to take shortest path enter yes, otherwise enter number of path you want to take");

        String choice = iScanner.nextLine();

        // Calculate the number of stations and direction of the path
        int numberOfStations = -1;
        String[] stations = null;
        
        if (choice.equalsIgnoreCase("yes")) {
            stations = shortestPath.split(", ");
        } else {
            int pathNumber = Integer.parseInt(choice);
            stations = paths.get(pathNumber - 1).split(", ");
        }
        
        System.out.println("Your Route: ");
        System.out.print(stations[0]);
        String direction = "";
        if (line1Stations.contains(stations[0])) {
            direction = "Line 1";
        } else if (line2Stations.contains(stations[0])) {
            direction = "Line 2";
        } else if (line3Stations.contains(stations[0])) {
            direction = "Line 3";
        }
        int i = 0;
        while (i != stations.length - 1) {
            if(direction.equals("Line 1")) {
                if (line1Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 1")) {
                        System.out.print(" Change to Line 1");
                    }
                    direction = "Line 1";
                } else if (line2Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 2")) {
                        System.out.print(" Change to Line 2");
                    }
                    direction = "Line 2";
                } else if (line3Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 3")) {
                        System.out.print(" Change to Line 3");
                    }
                    direction = "Line 3";
                }
            } else if(direction.equals("Line 2")) {
                if (line2Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 2")) {
                        System.out.print(" Change to Line 2");
                    }
                    direction = "Line 2";
                } else if (line3Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 3")) {
                        System.out.print(" Change to Line 3");
                    }
                    direction = "Line 3";
                }
                else if (line1Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 1")) {
                        System.out.print(" Change to Line 1");
                    }
                    direction = "Line 1";
                }
            } else if(direction.equals("Line 3")) {
                if (line3Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 3")) {
                        System.out.print(" Change to Line 3");
                    }
                    direction = "Line 3";
                } else if (line1Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 1")) {
                        System.out.print(" Change to Line 1");
                    }
                    direction = "Line 1";
                } else if (line2Stations.contains(stations[i + 1])) {
                    if (!direction.equals("Line 2")) {
                        System.out.print(" Change to Line 2");
                    }
                    direction = "Line 2";
                }
            }
            i++;
            System.out.print(" -> " + stations[i]);
        }
        System.out.println();
        System.out.println("Direction: " + direction);
        numberOfStations = stations.length;
        System.out.println("Number of stations: " + numberOfStations + " stations");

        // Calculate the expected time
        short expectedTime = (short) (numberOfStations * 2);
        if (expectedTime >= 60) {
            byte expectedTimeInHours = (byte) (expectedTime / 60);
            byte expectedTimeInMinutes = (byte) (expectedTime % 60);
            System.out.println(
                    "Expected time: " + expectedTimeInHours + " hours and " + expectedTimeInMinutes + " minutes");
        } else {
            System.out.println("Expected time: " + expectedTime + " minutes");
        }

        // Calculate ticket price
        byte ticketPrice = 0;
        if (numberOfStations < 10) {
            ticketPrice = 5;
        } else {
            ticketPrice = 7;
        }
        System.out.println("Ticket Price: " + ticketPrice + " EGP");

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
}
