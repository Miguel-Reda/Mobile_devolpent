package com.radwan.metroapp;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class MainActivity extends AppCompatActivity {

    static Graph graph = new Graph();
    Spinner startStationSpinner;
    Spinner endStationSpinner;

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
        ArrayList<String> Lines = new ArrayList<String>();
        Collections.addAll(Lines, "Please Select Line", "Line 1", "Line 2", "Line 3");

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


        Spinner positionLineSpinner;
        Spinner arrivalLineSpinner;
        
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
                ArrayAdapter<String> adapter = fillSpinners(selectedItem, line1Stations, line2Stations, line3Stations);
                startStationSpinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Do nothing
            }
        });
        arrivalLineSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                ArrayAdapter<String> adapter = fillSpinners(selectedItem, line1Stations, line2Stations, line3Stations);
                endStationSpinner.setAdapter(adapter);
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
        if(startStationSpinner.getSelectedItem().toString().equals("Please Select Station") || endStationSpinner.getSelectedItem().toString().equals("Please Select Station")) {
            Toast.makeText(this, "Please select a station", Toast.LENGTH_SHORT).show();
            return;
        }
        if(startStationSpinner.getSelectedItem().toString().equals(endStationSpinner.getSelectedItem().toString())) {
            Toast.makeText(this, "Start and end stations must be different", Toast.LENGTH_SHORT).show();
            return;
        }
        Vertex start = graph.getVertex(startStationSpinner.getSelectedItem().toString());
        Vertex end = graph.getVertex(endStationSpinner.getSelectedItem().toString());
        ArrayList<String> paths = graph.getAllPaths(start, end);
        Intent intent = new Intent(this, PathsActivity.class);
        intent.putStringArrayListExtra("paths", paths);
//        graph.clearGraph();
        startActivity(intent);
        
    }

    // Fill the spinners with the stations of the selected line
    public ArrayAdapter<String> fillSpinners(String selectedItem, ArrayList<String> line1Stations, ArrayList<String> line2Stations, ArrayList<String> line3Stations) {
        ArrayList<String> items = new ArrayList<>();
        items.add("Please Select Station");
        switch (selectedItem) {
            case "Line 1":
                items.addAll(line1Stations);
                break;
            case "Line 2":
                items.addAll(line2Stations);
                break;
            case "Line 3":
                items.addAll(line3Stations);
                break;
        }
        return new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
    }
}