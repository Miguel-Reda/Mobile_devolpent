package com.radwan.metroapp;


//import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements ShakeDetector.ShakeListener {
    private static final String START_STATION_KEY = "START_STATION";
    private static final String END_STATION_KEY = "END_STATION";
    private static final String PREVIOUS_DATA_PREF = "previousData";
    private TextToSpeech textToSpeech;
    StringBuilder Speech;
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
    SharedPreferences previousData;
    StringBuilder route;

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
        previousData = getSharedPreferences(PREVIOUS_DATA_PREF, MODE_PRIVATE);


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

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);

                    // Set speech rate and pitch
                    textToSpeech.setSpeechRate(0.6f); // Slow down the speech
                    textToSpeech.setPitch(1.2f);     // Raise the pitch
                }
            }
        });
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

        
        if (savedInstanceState != null) {
            StartStation = savedInstanceState.getString(START_STATION_KEY, "");
            EndStation = savedInstanceState.getString(END_STATION_KEY, "");
        } else {
            StartStation = previousData.getString(START_STATION_KEY, "");
            EndStation = previousData.getString(END_STATION_KEY, "");
        }

        if (!StartStation.isEmpty()) {
            startStationAutoComplete.setText(StartStation);
        }
        if (!EndStation.isEmpty()) {
            endStationAutoComplete.setText(EndStation);
        }

//        if (!StartStation.isEmpty() && !EndStation.isEmpty()) {
//            Vertex start = graph.getVertex(StartStation);
//            Vertex end = graph.getVertex(EndStation);
//            ArrayList<String> paths = graph.getAllPaths(start, end);
//            fillSummaryText(paths);
//        }


    }

    public StringBuilder speakTextSubString(StringBuilder fullText) {
        int startIndex = fullText.indexOf("~");
        fullText.setCharAt(startIndex, ' ');
        int endIndex = fullText.indexOf("~");
        fullText.setCharAt(endIndex, ' ');
        
        // Check if both '~' and '~' are present and in the correct order
        if (startIndex != -1 && endIndex != -1 && endIndex > startIndex) {
            String substring = fullText.substring(startIndex + 1, endIndex);
            Speech.append(substring);
        } else {
            // Handle cases where the parentheses are not present or in the wrong order
            // textToSpeech.speak("No valid text found between parentheses", TextToSpeech.QUEUE_FLUSH, null, null);
        }
        return fullText;
    }
    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(START_STATION_KEY, StartStation);
        outState.putString(END_STATION_KEY, EndStation);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences.Editor editor = previousData.edit();
        editor.putString(START_STATION_KEY, StartStation);
        editor.putString(END_STATION_KEY, EndStation);
        editor.apply();
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
        if(StartStation.isEmpty()){
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            Toast.makeText(this, "Please select a start station", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(EndStation.isEmpty()){
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
            Toast.makeText(this, "Please select an end station", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(StartStation.equals(EndStation)) {
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
            Toast.makeText(this, "Start and end stations must be different", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!totalStations.contains(StartStation)){
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            Toast.makeText(this, "Start station not found, Please select a valid start station", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!totalStations.contains(EndStation)){
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
            Toast.makeText(this, "End station not found, Please select a valid end station", Toast.LENGTH_SHORT).show();
            return;
        }
//        YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
//        YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
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
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            Toast.makeText(this, "Please select a start station", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(EndStation.isEmpty()){
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
            Toast.makeText(this, "Please select an end station", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(StartStation.equals(EndStation)) {
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
            Toast.makeText(this, "Start and end stations must be different", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!totalStations.contains(StartStation)){
            YoYo.with(Techniques.Bounce).duration(700).playOn(startStationAutoComplete);
            Toast.makeText(this, "Start station not found, Please select a valid start station", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!totalStations.contains(EndStation)){
            YoYo.with(Techniques.Bounce).duration(700).playOn(endStationAutoComplete);
            Toast.makeText(this, "End station not found, Please select a valid end station", Toast.LENGTH_SHORT).show();
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
        route = new StringBuilder();
        route = getRouteString(direction);
        summary.append("\n\nShortest Path: ").append(route);

        // Display the number of stations
        int numberOfStations = stations.length;
        summary.append("\n\nNumber of Stations: ").append(numberOfStations);

        // Display the expected time
        summary.append(getTime(numberOfStations));


        // Display the ticket price
        byte ticketPrice = (byte) getTicketPrice(numberOfStations);
        summary.append("\n\nTicket Price: ").append(ticketPrice).append(" EGP\n\n");
        Speech = new StringBuilder();
        summary = speakTextSubString(summary);
        if(summary.toString().contains("~")) {
            Speech.append(" then ");
            summary = speakTextSubString(summary);
        }

        textToSpeech.speak(Speech, TextToSpeech.QUEUE_FLUSH, null, null);
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
//    private StringBuilder getRouteString() {
//        StringBuilder routeString = new StringBuilder();
//        String direction = getDirection();
//
//        for (int i = 0; i < stations.length; i++) {
//            routeString.append(stations[i]);
//            if (i < stations.length - 1) {
//                direction = updateDirection(stations[i], stations[i + 1], direction);
//                routeString.append(" -> ");
//            }
//        }
//
//        return routeString;
//    }
//    private static final Map<String, Map<String, String>> SWITCH_STATIONS = Map.of(
//            "Al-Shohadaa", Map.of(
//                    "Ghamra", "New El-Marg",
//                    "Massara", "Shubra El-Kheima",
//                    "Attaba", "El-Mounib",
//                    "Orabi", "Helwan"
//            ),
//            "Sadat", Map.of(
//                    "Opera", "El-Mounib",
//                    "Mohamed Naguib", "Shubra El-Kheima",
//                    "Saad Zaghloul", "Helwan",
//                    "Nasser", "New El-Marg"
//            ),
//            "Attaba", Map.of(
//                    "Al-Shohadaa", "Shubra El-Kheima",
//                    "Mohamed Naguib", "El-Mounib",
//                    "Bab El Shaaria", "Adly Mansour",
//                    "Nasser", "Rod al-Farag Axis"
//            ),
//            "Nasser", Map.of(
//                    "Maspero", "Rod al-Farag Axis",
//                    "Attaba", "Adly Mansour",
//                    "Sadat", "Helwan",
//                    "Orabi", "New El-Marg"
//            )
//    );
//
//    private String updateDirection(String currentStation, String nextStation, String currentDirection) {
//        Map<String, String> stationDirections = SWITCH_STATIONS.get(currentStation);
//        if (stationDirections != null) {
//            String newDirection = stationDirections.get(nextStation);
//            if (newDirection != null && !newDirection.equals(currentDirection)) {
//                route.append("(Switch to ").append(newDirection).append(" Direction)");
//                return newDirection;
//            }
//        }
//        return currentDirection;
//    }

    private StringBuilder getRouteString(String direction) {
        StringBuilder routeString = new StringBuilder();
        for (int i = 0; i < stations.length; i++) {
            routeString.append(stations[i]).append(" ");
            // Check if the station is a switch station (Al-Shohadaa) and change the direction
            if (stations[i].equals("Al-Shohadaa")) {
                if (stations[i + 1].equals("Ghamra")) {
                    if (!direction.equals("New El-Marg")){
                        routeString.append("(~Switch in Al-Shohadaa~ to New El-Marg Direction)");
                    }
                    direction = "New El-Marg";
                } else if (stations[i + 1].equals("Massara")) {
                    if (!direction.equals("Shubra El-Kheima")){
                        routeString.append("(~Switch in Al-Shohadaa~ to Shubra El-Kheima Direction)");
                    }
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Attaba")) {
                    if (!direction.equals("El-Mounib")){
                        routeString.append("(~Switch in Al-Shohadaa~ to El-Mounib Direction)");
                    }
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Orabi")) {
                    if (!direction.equals("Helwan")){
                        routeString.append("(~Switch in Al-Shohadaa~ to Helwan Direction)");
                    }
                    direction = "Helwan";
                }
            }
            // Check if the station is a switch station (Sadat) and change the direction
            else if (stations[i].equals("Sadat")) {
                if (stations[i + 1].equals("Opera")) {
                    if (!direction.equals("El-Mounib")){
                        routeString.append("(~Switch in Sadat~ to El-Mounib Direction)");
                    }
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!direction.equals("Shubra El-Kheima")){
                        routeString.append("(~Switch in Sadat~ to Shubra El-Kheima Direction)");
                    }
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Saad Zaghloul")) {
                    if (!direction.equals("Helwan")){
                        routeString.append("(~Switch in Sadat~ to Helwan Direction)");
                    }
                    direction = "Helwan";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!direction.equals("New El-Marg")){
                        routeString.append("(~Switch in Sadat~ to New El-Marg Direction)");
                    }
                    direction = "New El-Marg";
                }
            }
            // Check if the station is a switch station (Attaba) and change the direction
            else if (stations[i].equals("Attaba")) {
                if (stations[i + 1].equals("Al-Shohadaa")) {
                    if (!direction.equals("Shubra El-Kheima")){
                        routeString.append("(~Switch in Attaba~ to Shubra El-Kheima Direction)");
                    }
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!direction.equals("El-Mounib")){
                        routeString.append("(~Switch in Attaba~ to El-Mounib Direction)");
                    }
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Bab El Shaaria")) {
                    if (!direction.equals("Adly Mansour")){
                        routeString.append("(~Switch in Attaba~ to Adly Mansour Direction)");
                    }
                    direction = "Adly Mansour";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!direction.equals("Rod al-Farag Axis")){
                        routeString.append("(~Switch in Attaba~ to Rod al-Farag Axis Direction)");
                    }
                    direction = "Rod al-Farag Axis";
                }
            }
            // Check if the station is a switch station (Nasser) and change the direction
            else if (stations[i].equals("Nasser")) {
                if (stations[i + 1].equals("Maspero")) {
                    if (!direction.equals("Rod al-Farag Axis")){
                        routeString.append("(~Switch in Nasser~ to Rod al-Farag Axis Direction)");
                    }
                    direction = "Rod al-Farag Axis";
                } else if (stations[i + 1].equals("Attaba")) {
                    if (!direction.equals("Adly Mansour")){
                        routeString.append("(~Switch in Nasser~ to Adly Mansour Direction)");
                    }
                    direction = "Adly Mansour";
                } else if (stations[i + 1].equals("Sadat")) {
                    if (!direction.equals("Helwan")){
                        routeString.append("(~Switch in Nasser~ to Helwan Direction)");
                    }
                    direction = "Helwan";
                } else if (stations[i + 1].equals("Orabi")) {
                    if (!direction.equals("New El-Marg")){
                        routeString.append("(~Switch in Nasser~ to New El-Marg Direction)");
                    }
                    direction = "New El-Marg";
                }
            }
            if (i + 1 < stations.length)
                routeString.append(" -> ");
        }
        return routeString;
    }

//    @SuppressLint("DefaultLocale")
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