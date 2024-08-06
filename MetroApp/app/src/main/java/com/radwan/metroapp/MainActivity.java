package com.radwan.metroapp;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    static Graph graph = new Graph();
    String StartStation ="";
    String EndStation = "";
    List<String> Stations;
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

        // Add stations to the lines
        ArrayList<String> line1Stations = new ArrayList<String>();
        Collections.addAll(line1Stations, "Helwan", "Ain Helwan", "Helwan University",
        "Wadi Hof", "El-Maasara", "Hadayek Helwan", "Tora El-Asmant", "Kolet El-Maadi",
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
        "El Bohy", "Ring Road", "Rod al-Farag Axis");

        Stations = new ArrayList<String>();
        Stations.addAll(line1Stations);
        Stations.addAll(line2Stations);
        Stations.addAll(line3Stations);


        Stations = Stations .stream()
                            .distinct()
                            .sorted().collect(Collectors.toList());


        AutoCompleteTextView startStationAutoComplete = findViewById(R.id.startStationAutoComplete);
        AutoCompleteTextView endStationAutoComplete = findViewById(R.id.endStationAutoComplete);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Stations);
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
        else if(!Stations.contains(StartStation)){
            Toast.makeText(this, "Start station not found", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(!Stations.contains(EndStation)){
            Toast.makeText(this, "End station not found", Toast.LENGTH_SHORT).show();
            // Animation for EndStationAutoComplete
            return;
        }

        Vertex start = graph.getVertex(StartStation);
        Vertex end = graph.getVertex(EndStation);
        ArrayList<String> paths = graph.getAllPaths(start, end);
        Intent intent = new Intent(this, PathsActivity.class);
        intent.putStringArrayListExtra("paths", paths);
        startActivity(intent);
        
    }
}