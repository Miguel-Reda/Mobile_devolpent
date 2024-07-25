package com.radwan.metroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class RouteActivity extends AppCompatActivity {
    ArrayList<String> line1Stations = new ArrayList<String>();
    ArrayList<String> line2Stations = new ArrayList<String>();
    ArrayList<String> line3Stations = new ArrayList<String>();
    TextView routeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        routeTextView = findViewById(R.id.routeTextView);
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

        String route = getIntent().getStringExtra("path");
        
        String[] stations = route.split(", ");

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
        StringBuilder routeString = new StringBuilder();
        for (int i = 0; i < stations.length; i++) {
            routeString.append(stations[i]).append(" ");
            // Check if the station is a switch station (Al-Shohadaa) and change the direction
            if (stations[i].equals("Al-Shohadaa")) {
                if (stations[i + 1].equals("Ghamra")) {
                    if (!direction.equals("New El-Marg"))
                        routeString.append("(Switch to New El-Marg Direction)");
                    direction = "New El-Marg";
                } else if (stations[i + 1].equals("Massara")) {
                    if (!direction.equals("Shubra El-Kheima"))
                        routeString.append("(Switch to Shubra El-Kheima Direction)");
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Attaba"))
                    if (!direction.equals("El-Mounib")) {
                        routeString.append("(Switch to El-Mounib Direction)");
                        direction = "El-Mounib";
                    } else if (stations[i + 1].equals("Orabi")) {
                        if (!direction.equals("Helwan"))
                            routeString.append("(Switch to Helwan Direction)");
                        direction = "Helwan";
                    }
            }
            // Check if the station is a switch station (Sadat) and change the direction
            else if (stations[i].equals("Sadat")) {
                if (stations[i + 1].equals("Opera")) {
                    if (!direction.equals("El-Mounib"))
                        routeString.append("(Switch to El-Mounib Direction)");
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!direction.equals("Shubra El-Kheima"))
                        routeString.append("(Switch to Shubra El-Kheima Direction)");
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Saad Zaghloul")) {
                    if (!direction.equals("Helwan"))
                        routeString.append("(Switch to Helwan Direction)");
                    direction = "Helwan";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!direction.equals("New El-Marg"))
                        routeString.append("(Switch to New El-Marg Direction)");
                    direction = "New El-Marg";
                }
            }
            // Check if the station is a switch station (Attaba) and change the direction
            else if (stations[i].equals("Attaba")) {
                if (stations[i + 1].equals("Al-Shohadaa")) {
                    if (!direction.equals("Shubra El-Kheima"))
                        routeString.append("(Switch to Shubra El-Kheima Direction)");
                    direction = "Shubra El-Kheima";
                } else if (stations[i + 1].equals("Mohamed Naguib")) {
                    if (!direction.equals("El-Mounib"))
                        routeString.append("(Switch to El-Mounib Direction)");
                    direction = "El-Mounib";
                } else if (stations[i + 1].equals("Bab El Shaaria")) {
                    if (!direction.equals("Adly Mansour"))
                        routeString.append("(Switch to Adly Mansour Direction)");
                    direction = "Adly Mansour";
                } else if (stations[i + 1].equals("Nasser")) {
                    if (!direction.equals("Rod al-Farag Axis"))
                        routeString.append("(Switch to Rod al-Farag Axis Direction)");
                    direction = "Rod al-Farag Axis ";
                }
            }
            // Check if the station is a switch station (Nasser) and change the direction
            else if (stations[i].equals("Nasser")) {
                if (stations[i + 1].equals("Maspero")) {
                    if (!direction.equals("Rod al-Farag Axis "))
                        routeString.append("(Switch to Rod al-Farag Axis Direction)");
                    direction = "Rod al-Farag Axis ";
                } else if (stations[i + 1].equals("Attaba")) {
                    if (!direction.equals("Adly Mansour"))
                        routeString.append("(Switch to Adly Mansour Direction)");
                    direction = "Adly Mansour";
                } else if (stations[i + 1].equals("Sadat")) {
                    if (!direction.equals("Helwan"))
                        routeString.append("(Switch to Helwan Direction)");
                    direction = "Helwan";
                } else if (stations[i + 1].equals("Orabi")) {
                    if (!direction.equals("New El-Marg"))
                        routeString.append("(Switch to New El-Marg Direction)");
                    direction = "New El-Marg";
                }
            }
            if (i + 1 < stations.length)
                routeString.append(" -> ");
        }
        
        routeString.append("\n\nDirection: ").append(direction);
        int numberOfStations = stations.length;
        routeString.append("\n\nNumber of Stations: ").append(numberOfStations);

        // Calculate the expected time
        int expectedTime = (numberOfStations * 2);
        if (expectedTime >= 60) {
            byte expectedTimeInHours = (byte) (expectedTime / 60);
            byte expectedTimeInMinutes = (byte) (expectedTime % 60);
            routeString.append("\n\nExpected time: ").append(expectedTimeInHours).append(" hours and ")
                    .append(expectedTimeInMinutes).append(" minutes");
        } else {
            routeString.append("\n\nExpected time: ").append(expectedTime).append(" minutes");
        }

        // Calculate ticket price
        byte ticketPrice = 0;
        if (numberOfStations < 10) {
            ticketPrice = 5;
        } else {
            ticketPrice = 7;
        }
        routeString.append("\n\nTicket Price: ").append(ticketPrice).append(" EGP");

        routeTextView.setText(routeString.toString());

    }

    public void GoToMap(View view) {
        Intent a =new Intent(this ,MapActivity.class);
        startActivity(a);
        Toast.makeText(this , "This a Map to guide you.",Toast.LENGTH_SHORT).show();
    }

    public void GoToPaths(View view) {
        Intent b =new Intent(this ,PathsActivity.class);
        startActivity(b);
    }
}