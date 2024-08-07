package com.radwan.metroapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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
import com.github.nisrulz.sensey.Sensey;
import com.github.nisrulz.sensey.ShakeDetector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements ShakeDetector.ShakeListener {
    Graph graph = new Graph();
    List<String> line1Stations = List.of("Helwan", "Ain Helwan", "Helwan University",
            "Wadi Hof",  "Hadayek Helwan","El-Maasara", "Tora El-Asmant", "Kolet El-Maadi",
            "Tora El-Balad", "Sakanat El-Maadi", "Maadi", "Hadayek El-Maadi", "Dar El-Salam",
            "Zahraa El-Maadi", "Mar Girgis", "El-Malek El-Saleh", "Sayeda Zeinab", "Saad Zaghloul",
            "Sadat", "Nasser", "Orabi", "Al-Shohadaa", "Ghamra", "El-Demerdash", "Manshiet El-Sadr",
            "Kobri El-Qobba", "Hammamat El-Qobba", "Saray El-Qobba", "Hadayek El-Zaitoun",
            "Helmeyet El-Zaitoun", "El-Matareyya", "Ain Shams", "Ezbet El-Nakhl", "El-Marg",
            "New El-Marg");
    List<String> line2Stations = List.of("Shubra El-Kheima", "Kolleyet El-Zeraa",
            "El-Mazallat", "El-Khalafawi", "Saint Teresa", "Rod El-Farag", "Massara", "Al-Shohadaa",
            "Attaba", "Mohamed Naguib", "Sadat", "Opera", "Dokki", "El Bohoth", "Cairo University",
            "Faisal", "Giza", "Omm El-Misryeen", "Sakiat Mekki", "El-Mounib");
    List<String> line3Stations = List.of("Adly Mansour", "El Haykestep", "Omar Ibn El Khattab",
            "Qobaa", "Hesham Barakat", "El Nozha", "Nadi El Shams", "Alf Maskan", "Heliopolis", "Haroun",
            "Al Ahram", "Koleyet El Banat", "Stadium", "El Maarad", "Abbassia", "Abdou Pasha", "El Geish",
            "Bab El Shaaria", "Attaba", "Nasser", "Maspero", "Safaa Hegazy", "Kit Kat", "Sudan", "Imbaba",
            "El Bohy","Al Qawmia" , "Ring Road", "Rod al-Farag Axis");
    List<String> totalStations;
    AutoCompleteTextView startStationAutoComplete;
    AutoCompleteTextView endStationAutoComplete;
    String StartStation ="";
    String EndStation = "";
    TextView summaryText;
    String[] stations;
    SharedPreferences prevousData;
    StringBuilder routeString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        startStationAutoComplete=findViewById(R.id.startStationAutoComplete);
        endStationAutoComplete=findViewById(R.id.endStationAutoComplete);
        Sensey.getInstance().init(this);
        Sensey.getInstance().startShakeDetection(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        summaryText = findViewById(R.id.summaryText);
        prevousData = getSharedPreferences("prevousData", MODE_PRIVATE);


        totalStations = new ArrayList<>();
        totalStations.addAll(line1Stations);
        totalStations.addAll(line2Stations);
        totalStations.addAll(line3Stations);



        startStationAutoComplete = findViewById(R.id.startStationAutoComplete);
        endStationAutoComplete = findViewById(R.id.endStationAutoComplete);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,totalStations.stream().distinct().sorted().collect(Collectors.toList()));
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
    private void addVertices(Graph graph, List<String> lineStations) {
        graph.addVertex(lineStations.get(0));
        for (int i = 1; i < lineStations.size(); i++) {
            graph.addVertex(lineStations.get(i));
            graph.addEdge(graph.getVertex(lineStations.get(i - 1)), graph.getVertex(lineStations.get(i)));
        }
    }

    // Handle the done button click
    public void done(View view) {
//        Toast.makeText(this, StartStation + " " + EndStation, Toast.LENGTH_SHORT).show();
        if(StartStation.isEmpty()){
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            Toast.makeText(this, "Please select a start station", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(EndStation.isEmpty()){
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
            Toast.makeText(this, "Please select an end station", Toast.LENGTH_SHORT).show();
            // Animation for EndStationAutoComplete
            return;
        }
        else if(StartStation.equals(EndStation)) {
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);

            Toast.makeText(this, "Start and end stations must be different", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete and EndStationAutoComplete
            return;
        }
        else if(!totalStations.contains(StartStation)){
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);

            Toast.makeText(this, "Start station not found, Please select a valid start station", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(!totalStations.contains(EndStation)){
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);

            Toast.makeText(this, "End station not found, Please select a valid end station", Toast.LENGTH_SHORT).show();
            // Animation for EndStationAutoComplete
            return;
        }
        YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
        YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);

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
//        Toast.makeText(this, "Paths: " + paths.size(), Toast.LENGTH_SHORT).show();

        fillSummaryText(paths);
        // لو عايزه يستنى خمس دثواني و بعدها يروح للصفحة اللي بعدها
/*

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, PathsActivity.class);
            intent.putStringArrayListExtra("paths", paths);
            startActivity(intent);
        }, 5000);
*/



    }

    // Handle the swap button click event
    public void swap_station(View view) {
        if(StartStation.isEmpty()){
            YoYo.with(Techniques.Bounce).duration(700).repeat(1).playOn(startStationAutoComplete);
            Toast.makeText(this, "Please select a start station", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(EndStation.isEmpty()){
            YoYo.with(Techniques.Bounce).duration(700).repeat(1).playOn(endStationAutoComplete);

            Toast.makeText(this, "Please select an end station", Toast.LENGTH_SHORT).show();
            // Animation for EndStationAutoComplete
            return;
        }
        else if(StartStation.equals(EndStation)) {
            YoYo.with(Techniques.Bounce).duration(700).repeat(1).playOn(startStationAutoComplete);
            YoYo.with(Techniques.Bounce).duration(700).repeat(1).playOn(endStationAutoComplete);

            Toast.makeText(this, "Start and end stations must be different", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete and EndStationAutoComplete
            return;
        }
        else if(!totalStations.contains(StartStation)){
            YoYo.with(Techniques.Bounce).duration(700).repeat(1).playOn(startStationAutoComplete);

            Toast.makeText(this, "Start station not found, Please select a valid start station", Toast.LENGTH_SHORT).show();
            // Animation for StartStationAutoComplete
            return;
        }
        else if(!totalStations.contains(EndStation)){
            YoYo.with(Techniques.Bounce).duration(700).repeat(1).playOn(endStationAutoComplete);

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
        routeString = getRouteString();
        summary.append("\n\nShortest Path: ").append(routeString);

        // Display the number of stations
        int numberOfStations = stations.length;
        summary.append("\n\nNumber of Stations: ").append(numberOfStations);

        // Display the expected time
        summary.append(getTime(numberOfStations));


        // Display the ticket price
        byte ticketPrice = (byte) getTicketPrice(numberOfStations);
        summary.append("\n\nTicket Price: ").append(ticketPrice).append(" EGP\n\n");

        summaryText.setText(summary.toString());
    }
    private String getDirection() {
        if (line1Stations.contains(stations[0]) && line1Stations.contains(stations[1])) {
            return line1Stations.indexOf(stations[1]) > line1Stations.indexOf(stations[0]) ? "New El-Marg" : "Helwan";
        } else if (line2Stations.contains(stations[0]) && line2Stations.contains(stations[1])) {
            return line2Stations.indexOf(stations[1]) > line2Stations.indexOf(stations[0]) ? "El-Mounib" : "Shubra El-Kheima";
        } else if (line3Stations.contains(stations[0]) && line3Stations.contains(stations[1])) {
            return line3Stations.indexOf(stations[1]) > line3Stations.indexOf(stations[0]) ? "Rod al-Farag Axis" : "Adly Mansour";
        }
        return "";
    }
    private StringBuilder getRouteString() {
        StringBuilder routeString = new StringBuilder();
        String direction = getDirection();

        for (int i = 0; i < stations.length; i++) {
            routeString.append(stations[i]);
            if (i < stations.length - 1) {
                direction = updateDirection(stations[i], stations[i + 1], direction);
                routeString.append(" -> ");
            }
        }

        return routeString;
    }
    private static final Map<String, Map<String, String>> SWITCH_STATIONS = Map.of(
            "Al-Shohadaa", Map.of(
                    "Ghamra", "New El-Marg",
                    "Massara", "Shubra El-Kheima",
                    "Attaba", "El-Mounib",
                    "Orabi", "Helwan"
            ),
            "Sadat", Map.of(
                    "Opera", "El-Mounib",
                    "Mohamed Naguib", "Shubra El-Kheima",
                    "Saad Zaghloul", "Helwan",
                    "Nasser", "New El-Marg"
            ),
            "Attaba", Map.of(
                    "Al-Shohadaa", "Shubra El-Kheima",
                    "Mohamed Naguib", "El-Mounib",
                    "Bab El Shaaria", "Adly Mansour",
                    "Nasser", "Rod al-Farag Axis"
            ),
            "Nasser", Map.of(
                    "Maspero", "Rod al-Farag Axis",
                    "Attaba", "Adly Mansour",
                    "Sadat", "Helwan",
                    "Orabi", "New El-Marg"
            )
    );

    private String updateDirection(String currentStation, String nextStation, String currentDirection) {
        Map<String, String> stationDirections = SWITCH_STATIONS.get(currentStation);
        if (stationDirections != null) {
            String newDirection = stationDirections.get(nextStation);
            if (newDirection != null && !newDirection.equals(currentDirection)) {
                routeString.append("(Switch to ").append(newDirection).append(" Direction)");
                return newDirection;
            }
        }
        return currentDirection;
    }

    private String getTime(int numberOfStations) {
        int expectedTime = numberOfStations * 2;
        int hours = expectedTime / 60;
        int minutes = expectedTime % 60;

        return hours > 0 ?
                String.format("\n\nExpected time: %d hours and %d minutes", hours, minutes)
                : String.format("\n\nExpected time: %d minutes", minutes);
    }
    private int getTicketPrice(int numberOfStations) {
        return numberOfStations < 10 ? 5 : 7;
    }

    @Override
    public void onShakeDetected() {


    }

    @Override
    public void onShakeStopped() {
        summaryText.setText("");
        // Clear the AutoCompleteTextView fields
        startStationAutoComplete.setText("");
        endStationAutoComplete.setText("");

        // Reset StartStation and EndStation variables
        StartStation = "";
        EndStation = "";
    }
}