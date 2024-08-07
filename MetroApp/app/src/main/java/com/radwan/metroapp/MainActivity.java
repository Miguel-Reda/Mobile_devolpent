package com.radwan.metroapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    Graph graph = new Graph();
    ArrayList<String> line1Stations;
    ArrayList<String> line2Stations;
    ArrayList<String> line3Stations;
    List<String> totalStations;
    AutoCompleteTextView startStationAutoComplete;
    AutoCompleteTextView endStationAutoComplete;
    String StartStation ="";
    String EndStation = "";
    TextView summaryText;
    String[] stations;
    SharedPreferences prevousData;

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
        summaryText = findViewById(R.id.summaryText);
        prevousData = getSharedPreferences("prevousData", MODE_PRIVATE);
        // Add stations to the lines
        line1Stations = new ArrayList<String>();
        Collections.addAll(line1Stations, "Helwan", "Ain Helwan", "Helwan University",
        "Wadi Hof", "El-Maasara", "Hadayek Helwan", "Tora El-Asmant", "Kolet El-Maadi",
        "Tora El-Balad", "Sakanat El-Maadi", "Maadi", "Hadayek El-Maadi", "Dar El-Salam",
        "Zahraa El-Maadi", "Mar Girgis", "El-Malek El-Saleh", "Sayeda Zeinab", "Saad Zaghloul",
        "Sadat", "Nasser", "Orabi", "Al-Shohadaa", "Ghamra", "El-Demerdash", "Manshiet El-Sadr",
        "Kobri El-Qobba", "Hammamat El-Qobba", "Saray El-Qobba", "Hadayek El-Zaitoun",
        "Helmeyet El-Zaitoun", "El-Matareyya", "Ain Shams", "Ezbet El-Nakhl", "El-Marg",
        "New El-Marg");

        line2Stations = new ArrayList<String>();
        Collections.addAll(line2Stations, "Shubra El-Kheima", "Kolleyet El-Zeraa",
        "El-Mazallat", "El-Khalafawi", "Saint Teresa", "Rod El-Farag", "Massara", "Al-Shohadaa",
        "Attaba", "Mohamed Naguib", "Sadat", "Opera", "Dokki", "El Bohoth", "Cairo University",
        "Faisal", "Giza", "Omm El-Misryeen", "Sakiat Mekki", "El-Mounib");

        line3Stations = new ArrayList<String>();
        Collections.addAll(line3Stations, "Adly Mansour", "El Haykestep", "Omar Ibn El Khattab",
        "Qobaa", "Hesham Barakat", "El Nozha", "Nadi El Shams", "Alf Maskan", "Heliopolis", "Haroun",
        "Al Ahram", "Koleyet El Banat", "Stadium", "Fair Zone", "Abbassia", "Abdou Pasha", "El Geish",
        "Bab El Shaaria", "Attaba", "Nasser", "Maspero", "Zamalek", "Kit Kat", "Sudan", "Imbaba",
        "El Bohy", "Ring Road", "Rod al-Farag Axis");

        totalStations = new ArrayList<String>();
        totalStations.addAll(line1Stations);
        totalStations.addAll(line2Stations);
        totalStations.addAll(line3Stations);


        totalStations = totalStations.stream()
                            .distinct()
                            .sorted().collect(Collectors.toList());


        startStationAutoComplete = findViewById(R.id.startStationAutoComplete);
        endStationAutoComplete = findViewById(R.id.endStationAutoComplete);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, totalStations);
        startStationAutoComplete.setAdapter(adapter);
        startStationAutoComplete.setThreshold(1);

        endStationAutoComplete.setAdapter(adapter);
        endStationAutoComplete.setThreshold(1);


        startStationAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            StartStation = (String) parent.getItemAtPosition(position);
        });

        endStationAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            EndStation = (String) parent.getItemAtPosition(position);
        });
        // Show all options when the AutoCompleteTextView is clicked or gains focus
        startStationAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startStationAutoComplete.showDropDown();
            }
        });

        // Show all options when the AutoCompleteTextView is clicked or gains focus
        endStationAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endStationAutoComplete.showDropDown();
            }
        });

        addVertices(graph, line1Stations);
        addVertices(graph, line2Stations);
        addVertices(graph, line3Stations);
        
    }

    // Add vertices to the graph
    private void addVertices(Graph graph, ArrayList<String> lineStations) {
        for (int i = 0; i < lineStations.size(); i++) {
            graph.addVertex(lineStations.get(i));
            if (i > 0) {
                graph.addEdge(graph.getVertex(lineStations.get(i - 1)), graph.getVertex(lineStations.get(i)));
            }
        }
    }

    // Handle the done button click
    public void done(View view) {
//        Toast.makeText(this, StartStation + " " + EndStation, Toast.LENGTH_SHORT).show();
        if(StartStation.isEmpty()){
            Toast.makeText(this, "Please select a start station", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(EndStation.isEmpty()){
            Toast.makeText(this, "Please select an end station", Toast.LENGTH_SHORT).show();
            // Animation for EndStationAutoComplete
            return;
        }
        else if(StartStation.equals(EndStation)) {
            Toast.makeText(this, "Start and end stations must be different", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete and EndStationAutoComplete
            return;
        }
        else if(!totalStations.contains(StartStation)){
            Toast.makeText(this, "Start station not found, Please select a valid start station", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(!totalStations.contains(EndStation)){
            Toast.makeText(this, "End station not found, Please select a valid end station", Toast.LENGTH_SHORT).show();
            // Animation for EndStationAutoComplete
            return;
        }
        // Animation for StartStationAutoComplete and EndStationAutoComplete
//        SharedPreferences.Editor editor = prevousData.edit();
//        editor.putString("StartStation", StartStation);
//        editor.putString("EndStation", EndStation);
//        editor.apply();
        YoYo.with(Techniques.ZoomIn)
                .duration(500)
                .playOn(findViewById(R.id.backbtn));
        Vertex start = graph.getVertex(StartStation);
        Vertex end = graph.getVertex(EndStation);
        ArrayList<String> paths = graph.getAllPaths(start, end);

        fillSummaryText(paths);
    }
    // Handle the swap button click event
    public void swap_station(View view) {
        if(StartStation.isEmpty()){
            Toast.makeText(this, "Please select a start station", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(EndStation.isEmpty()){
            Toast.makeText(this, "Please select an end station", Toast.LENGTH_SHORT).show();
            // Animation for EndStationAutoComplete
            return;
        }
        else if(StartStation.equals(EndStation)) {
            Toast.makeText(this, "Start and end stations must be different", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete and EndStationAutoComplete
            return;
        }
        else if(!totalStations.contains(StartStation)){
            Toast.makeText(this, "Start station not found, Please select a valid start station", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(!totalStations.contains(EndStation)){
            Toast.makeText(this, "End station not found, Please select a valid end station", Toast.LENGTH_SHORT).show();
            // Animation for EndStationAutoComplete
            return;
        }
        // Animate the AutoCompleteTextViews
        YoYo.with(Techniques.BounceInDown)
                .duration(500)
                .playOn(findViewById(R.id.startStationAutoComplete));
        YoYo.with(Techniques.BounceInUp)
                .duration(500)
                .playOn(findViewById(R.id.endStationAutoComplete));

        // Swap the start and end stations
        String temp;
        temp = StartStation;
        StartStation = EndStation;
        EndStation = temp;

        // Update the text in the AutoCompleteTextViews
        startStationAutoComplete.setText(StartStation);
        endStationAutoComplete.setText(EndStation);

        // Update the text in the SharedPreferences
//        SharedPreferences.Editor editor = prevousData.edit();
//        editor.putString("StartStation", StartStation);
//        editor.putString("EndStation", EndStation);
//        editor.apply();

        // update the summary text
        Vertex start = graph.getVertex(StartStation);
        Vertex end = graph.getVertex(EndStation);
        ArrayList<String> paths = graph.getAllPaths(start, end);
        fillSummaryText(paths);
    }


    private void fillSummaryText(ArrayList<String> paths) {
        StringBuilder summary = new StringBuilder();
        String shortestPath = "";
        int shortestPathLength = Integer.MAX_VALUE;
        summary.append("Paths between ").append(StartStation).append(" and ").append(EndStation).append(":\n");
        for (int i = 0; i < paths.size(); i++) {
            int pathLength = paths.get(i).split(", ").length;
            summary.append("- Path ").append(i + 1).append(" --> ").append(pathLength).append(" stations\n");
            if (pathLength < shortestPathLength) {
                shortestPath = paths.get(i);
                shortestPathLength = pathLength;
            }
        }
        stations = shortestPath.split(", ");
        summary.append("Total paths: ").append(paths.size());

        // Determine the initial direction of the route
        String direction = getDirection();
        summary.append("\n\nDirection: ").append(direction);

        // Construct the route string
        StringBuilder routeString = getRouteString(direction);
        summary.append("\n\nShortest Path: ").append(routeString);

        // Display the number of stations
        int numberOfStations = stations.length;
        summary.append("\n\nNumber of Stations: ").append(numberOfStations);

        // Display the expected time
        summary.append(getTime(numberOfStations));


        // Display the ticket price
        byte ticketPrice = getTicketPrice(numberOfStations);
        summary.append("\n\nTicket Price: ").append(ticketPrice).append(" EGP\n\n");
        
        summaryText.setText(summary.toString());
    }
    private String getDirection() {
        String direction = "";
        if (line1Stations.contains(stations[1]) && line1Stations.contains(stations[2])) {
            if (line1Stations.indexOf(stations[2]) - line1Stations.indexOf(stations[1]) > 0) {
                direction = "New El-Marg";
            } else {
                direction = "Helwan";
            }
        } else if (line2Stations.contains(stations[1]) && line2Stations.contains(stations[2])) {
            if (line2Stations.indexOf(stations[2]) - line2Stations.indexOf(stations[1]) > 0) {
                direction = "El-Mounib";
            } else {
                direction = "Shubra El-Kheima";
            }
        } else if (line3Stations.contains(stations[1]) && line3Stations.contains(stations[2])) {
            if (line3Stations.indexOf(stations[2]) - line3Stations.indexOf(stations[1]) > 0) {
                direction = "Rod al-Farag Axis";
            } else {
                direction = "Adly Mansour";
            }
        }
        return direction;
    }

    private StringBuilder getRouteString(String direction) {
        StringBuilder routeString = new StringBuilder();
        for (int i = 0; i < stations.length; i++) {
            routeString.append(stations[i]).append(" ");
            // Check if the station is a switch station (Al-Shohadaa) and change the direction
            if (stations[i].equals("Al-Shohadaa")) {
                if (stations[i + 1].equals("Ghamra")) {
                    if (!direction.equals("New El-Marg")){
                        routeString.append("(Switch to New El-Marg Direction)");
                    }
                    direction = "New El-Marg";
                } else if (stations[i + 1].equals("Massara")) {
                    if (!direction.equals("Shubra El-Kheima")){
                        routeString.append("(Switch to Shubra El-Kheima Direction)");
                    }
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Attaba")) {
                    if (!direction.equals("El-Mounib")){
                        routeString.append("(Switch to El-Mounib Direction)");
                    }
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Orabi")) {
                    if (!direction.equals("Helwan")){
                        routeString.append("(Switch to Helwan Direction)");
                    }
                    direction = "Helwan";
                }
            }
            // Check if the station is a switch station (Sadat) and change the direction
            else if (stations[i].equals("Sadat")) {
                if (stations[i + 1].equals("Opera")) {
                    if (!direction.equals("El-Mounib")){
                        routeString.append("(Switch to El-Mounib Direction)");
                    }
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!direction.equals("Shubra El-Kheima")){
                        routeString.append("(Switch to Shubra El-Kheima Direction)");
                    }
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Saad Zaghloul")) {
                    if (!direction.equals("Helwan")){
                        routeString.append("(Switch to Helwan Direction)");
                    }
                    direction = "Helwan";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!direction.equals("New El-Marg")){
                        routeString.append("(Switch to New El-Marg Direction)");
                    }
                    direction = "New El-Marg";
                }
            }
            // Check if the station is a switch station (Attaba) and change the direction
            else if (stations[i].equals("Attaba")) {
                if (stations[i + 1].equals("Al-Shohadaa")) {
                    if (!direction.equals("Shubra El-Kheima")){
                        routeString.append("(Switch to Shubra El-Kheima Direction)");
                    }
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!direction.equals("El-Mounib")){
                        routeString.append("(Switch to El-Mounib Direction)");
                    }
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Bab El Shaaria")) {
                    if (!direction.equals("Adly Mansour")){
                        routeString.append("(Switch to Adly Mansour Direction)");
                    }
                    direction = "Adly Mansour";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!direction.equals("Rod al-Farag Axis")){
                        routeString.append("(Switch to Rod al-Farag Axis Direction)");
                    }
                    direction = "Rod al-Farag Axis";
                }
            }
            // Check if the station is a switch station (Nasser) and change the direction
            else if (stations[i].equals("Nasser")) {
                if (stations[i + 1].equals("Maspero")) {
                    if (!direction.equals("Rod al-Farag Axis")){
                        routeString.append("(Switch to Rod al-Farag Axis Direction)");
                    }
                    direction = "Rod al-Farag Axis";
                } else if (stations[i + 1].equals("Attaba")) {
                    if (!direction.equals("Adly Mansour")){
                        routeString.append("(Switch to Adly Mansour Direction)");
                    }
                    direction = "Adly Mansour";
                } else if (stations[i + 1].equals("Sadat")) {
                    if (!direction.equals("Helwan")){
                        routeString.append("(Switch to Helwan Direction)");
                    }
                    direction = "Helwan";
                } else if (stations[i + 1].equals("Orabi")) {
                    if (!direction.equals("New El-Marg")){
                        routeString.append("(Switch to New El-Marg Direction)");
                    }
                    direction = "New El-Marg";
                }
            }
            if (i + 1 < stations.length)
                routeString.append(" -> ");
        }
        return routeString;
    }

    private StringBuilder getTime(int numberOfStations) {
        StringBuilder routeString = new StringBuilder();
        int expectedTime = (numberOfStations * 2);
        if (expectedTime >= 60) {
            byte expectedTimeInHours = (byte) (expectedTime / 60);
            byte expectedTimeInMinutes = (byte) (expectedTime % 60);
            routeString.append("\n\nExpected time: ").append(expectedTimeInHours).append(" hours and ")
                    .append(expectedTimeInMinutes).append(" minutes");
        } else {
            routeString.append("\n\nExpected time: ").append(expectedTime).append(" minutes");
        }
        return routeString;
    }
    private byte getTicketPrice(int numberOfStations){
        byte ticketPrice;
        if (numberOfStations < 10) {
            ticketPrice = 5;
        } else {
            ticketPrice = 7;
        }
        return ticketPrice;
    }
}