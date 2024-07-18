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
                "El Bohy", "Ring Road", "Rod al-Farag Axis ");

        System.out.println("Welcome to Cairo Metro Route Finder");
        System.out.println("Line 1 Stations: " + line1Stations);
        System.out.println("Line 2 Stations: " + line2Stations);
        System.out.println("Line 3 Stations: " + line3Stations);

        Graph graph = new Graph();
        addVertices(graph, line1Stations);
        addVertices(graph, line2Stations);
        addVertices(graph, line3Stations);

        System.out.println("\nEnter your start station:");
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
            int pathLength = paths.get(i).split(", ").length;
            System.out.println("Path " + (i + 1) + "(" + pathLength + " stations)" + ": " + paths.get(i));
            if (paths.get(i).split(", ").length < shortestPathLength) {
                shortestPath = paths.get(i);
                shortestPathLength = pathLength;
            }
        }

        System.out.println("\nShortest Path: " + shortestPath);
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
        ArrayList<String> switchStations = new ArrayList<String>();
        // Collections.addAll(switchStations, "Al-Shohadaa", "Sadat", "Attaba", "Nasser");
        String direction = "";
        for (int i = 0; i < stations.length; i++) {
            System.out.print(stations[i] + " ");
            // Check if the station is a switch station (Al-Shohadaa) and change the direction
            if (stations[i].equals("Al-Shohadaa")) {
                if (stations[i + 1].equals("Ghamra")) {
                    if (!direction.equals("New El-Marg"))
                        System.out.print("(Switch to New El-Marg Direction)");
                    direction = "New El-Marg";
                } else if (stations[i + 1].equals("Massara")) {
                    if (!direction.equals("Shubra El-Kheima"))
                        System.out.print("(Switch to Shubra El-Kheima Direction)");
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Attaba"))
                    if (!direction.equals("El-Mounib")) {
                        System.out.print("(Switch to El-Mounib Direction)");
                        direction = "El-Mounib";
                    } else if (stations[i + 1].equals("Orabi")) {
                        if (!direction.equals("Helwan"))
                            System.out.print("(Switch to Helwan Direction)");
                        direction = "Helwan";
                    }
            }
            // Check if the station is a switch station (Sadat) and change the direction
            else if (stations[i].equals("Sadat")) {
                if (stations[i + 1].equals("Opera")) {
                    if (!direction.equals("El-Mounib"))
                        System.out.print("(Switch to El-Mounib Direction)");
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!direction.equals("Shubra El-Kheima"))
                        System.out.print("(Switch to Shubra El-Kheima Direction)");
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Saad Zaghloul")) {
                    if (!direction.equals("Helwan"))
                        System.out.print("(Switch to Helwan Direction)");
                    direction = "Helwan";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!direction.equals("New El-Marg"))
                        System.out.print("(Switch to New El-Marg Direction)");
                    direction = "New El-Marg";
                }
            }
            // Check if the station is a switch station (Attaba) and change the direction
            else if (stations[i].equals("Attaba")) {
                if (stations[i + 1].equals("Al-Shohadaa")) {
                    if (!direction.equals("Shubra El-Kheima"))
                        System.out.print("(Switch to Shubra El-Kheima Direction)");
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!direction.equals("El-Mounib"))
                        System.out.print("(Switch to El-Mounib Direction)");
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Bab El Shaaria")) {
                    if (!direction.equals("Adly Mansour"))
                        System.out.print("(Switch to Adly Mansour Direction)");
                    direction = "Adly Mansour";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!direction.equals("Rod al-Farag Axis"))
                        System.out.print("(Switch to Rod al-Farag Axis  Direction)");
                    direction = "Rod al-Farag Axis ";
                }
            }
            // Check if the station is a switch station (Nasser) and change the direction
            else if (stations[i].equals("Nasser")) {
                if (stations[i + 1].equals("Maspero")) {
                    if (!direction.equals("Rod al-Farag Axis "))
                        System.out.print("(Switch to Rod al-Farag Axis  Direction)");
                    direction = "Rod al-Farag Axis ";
                } else if (stations[i + 1].equals("Attaba")) {
                    if (!direction.equals("Adly Mansour"))
                        System.out.print("(Switch to Adly Mansour Direction)");
                    direction = "Adly Mansour";
                } else if (stations[i + 1].equals("Sadat")) {
                    if (!direction.equals("Helwan"))
                        System.out.print("(Switch to Helwan Direction)");
                    direction = "Helwan";
                } else if (stations[i + 1].equals("Orabi")) {
                    if (!direction.equals("New El-Marg"))
                        System.out.print("(Switch to New El-Marg Direction)");
                    direction = "New El-Marg";
                }
            }
            if (i + 1 < stations.length)
                System.out.print(" ->  ");
        }

        System.out.println("\nDirection: " + direction);
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
