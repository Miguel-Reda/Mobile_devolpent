package com.radwan.metroapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> Lines = new ArrayList<String>();

    ArrayList<String> line1Stations = new ArrayList<String>();
    ArrayList<String> line2Stations = new ArrayList<String>();
    ArrayList<String> line3Stations = new ArrayList<String>();

    Spinner positionLineSpinner;
    Spinner startStationSpinner;

    Spinner arrivalLineSpinner;
    Spinner endStationSpinner;

    Graph graph = new Graph();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Add lines to the list
        Collections.addAll(Lines, "Please Select Line", "Line 1", "Line 2", "Line 3");

        // Add stations to the lines
        Collections.addAll(line1Stations, "Helwan", "Ain Helwan", "Helwan University",
                "Wadi Hof", "El-Maasara", "Hadayek Helwan", "Tora El-Asmant", "Kolet El-Maadi",
                "Tora El-Balad", "Sakanat El-Maadi", "Maadi", "Hadayek El-Maadi", "Dar El-Salam",
                "Zahraa El-Maadi", "Mar Girgis", "El-Malek El-Saleh", "Sayeda Zeinab", "Saad Zaghloul",
                "Sadat", "Nasser", "Orabi", "Al-Shohadaa", "Ghamra", "El-Demerdash", "Manshiet El-Sadr",
                "Kobri El-Qobba", "Hammamat El-Qobba", "Saray El-Qobba", "Hadayek El-Zaitoun",
                "Helmeyet El-Zaitoun", "El-Matareyya", "Ain Shams", "Ezbet El-Nakhl", "El-Marg",
                "New El-Marg");
        Collections.addAll(line2Stations, "Shubra El-Kheima", "Kolleyet El-Zeraa",
                "El-Mazallat", "El-Khalafawi", "Saint Teresa", "Rod El-Farag", "Massara", "Al-Shohadaa",
                "Attaba", "Mohamed Naguib", "Sadat", "Opera", "Dokki", "El Bohoth", "Cairo University",
                "Faisal", "Giza", "Omm El-Misryeen", "Sakiat Mekki", "El-Mounib");
        Collections.addAll(line3Stations, "Adly Mansour", "El Haykestep", "Omar Ibn El Khattab",
                "Qobaa", "Hesham Barakat", "El Nozha", "Nadi El Shams", "Alf Maskan", "Heliopolis", "Haroun",
                "Al Ahram", "Koleyet El Banat", "Stadium", "Fair Zone", "Abbassia", "Abdou Pasha", "El Geish",
                "Bab El Shaaria", "Attaba", "Nasser", "Maspero", "Zamalek", "Kit Kat", "Sudan", "Imbaba",
                "El Bohy", "Ring Road", "Rod al-Farag Axis");

        // Set the adapter for the spinners
        positionLineSpinner = findViewById(R.id.positionLineSpinner);
        arrivalLineSpinner = findViewById(R.id.arrivalLineSpinner);
        startStationSpinner = findViewById(R.id.startStationSpinner);
        endStationSpinner = findViewById(R.id.endStationSpinner);
        ArrayAdapter<String> adapterLine = new ArrayAdapter(this, android.R.layout.simple_spinner_item,Lines);
        adapterLine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionLineSpinner.setAdapter(adapterLine);
        arrivalLineSpinner.setAdapter(adapterLine);
        
        positionLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Create an array of items for the second spinner based on the selected item
                ArrayList<String> items2 = new ArrayList<>();
                items2.add("Please Select Station");
                switch (selectedItem) {
                    case "Line 1":
                        items2.addAll(line1Stations);
                        break;
                    case "Line 2":
                        items2.addAll(line2Stations);
                        break;
                    case "Line 3":
                        items2.addAll(line3Stations);
                        break;
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, items2);
                startStationSpinner.setAdapter(adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                
            }
        });
        arrivalLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Create an array of items for the second spinner based on the selected item
                ArrayList<String> items2 = new ArrayList<>();
                items2.add("Please Select Station");
                switch (selectedItem) {
                    case "Line 1":
                        items2.addAll(line1Stations);
                        break;
                    case "Line 2":
                        items2.addAll(line2Stations);
                        break;
                    case "Line 3":
                        items2.addAll(line3Stations);
                        break;
                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, items2);
                endStationSpinner.setAdapter(adapter2);
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });

         addVertices(graph, line1Stations);
         addVertices(graph, line2Stations);
         addVertices(graph, line3Stations);
        
    }

    // Add vertices to the graph
    public void addVertices(Graph graph, ArrayList<String> lineStations) {
        for (int i = 0; i < lineStations.size(); i++) {
            graph.addVertex(lineStations.get(i));
            if (i > 0) {
                graph.addEdge(graph.getVertex(lineStations.get(i - 1)), graph.getVertex(lineStations.get(i)));
            }
        }
    }

    public void done(View view) {
        if(startStationSpinner.getSelectedItem().toString().equals("Please Select Station") || endStationSpinner.getSelectedItem().toString().equals("Please Select Station")) {
            Toast.makeText(this, "Please select a station", Toast.LENGTH_SHORT).show();
            return;
        }
        Vertex start = graph.getVertex(startStationSpinner.getSelectedItem().toString());
        Vertex end = graph.getVertex(endStationSpinner.getSelectedItem().toString());
        ArrayList<String> paths = graph.getAllPaths(start, end);
        for(String path : paths) {
            System.out.println(path);
        }
        
        Intent intent = new Intent(this, PathsActivity.class);
        intent.putStringArrayListExtra("paths", paths);
        startActivity(intent);
        
    }
        
/*
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

    //     Calculate the number of stations and direction of the path
        int numberOfStations = -1;
        String[] stations = null;

        if (choice.equalsIgnoreCase("yes")) {
            stations = shortestPath.split(", ");
        } else {
            int pathNumber = Integer.parseInt(choice);
            stations = paths.get(pathNumber - 1).split(", ");
        }

        System.out.println("Your Route: ");
        String direction = "";
        if (line1Stations.contains(stations[0]) && line1Stations.contains(stations[1])) {
            if (line1Stations.indexOf(stations[1]) - line1Stations.indexOf(stations[0]) > 0) {
                direction = "New El-Marg";
            } else {
                direction = "Helwan";
            }
        } else if (line2Stations.contains(stations[0]) && line2Stations.contains(stations[1])) {
            if (line2Stations.indexOf(stations[1]) - line2Stations.indexOf(stations[0]) > 0) {
                direction = "El-Mounib";
            } else {
                direction = "Shubra El-Kheima";
            }
        } else if (line3Stations.contains(stations[0]) && line3Stations.contains(stations[1])) {
            if (line3Stations.indexOf(stations[1]) - line3Stations.indexOf(stations[0]) > 0) {
                direction = "Rod al-Farag Axis";
            } else {
                direction = "Adly Mansour";
            }
        }
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
        }*/

}