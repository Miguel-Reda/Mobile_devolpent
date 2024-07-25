package com.radwan.metroapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PathsActivity extends AppCompatActivity {

    TextView pathsTextView;
    TextView shortestPathTextView;
    Spinner chooseSpinner;
    Button pathButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paths);
        pathsTextView = findViewById(R.id.pathsTextView);
        shortestPathTextView = findViewById(R.id.shortestPathTextView);
        chooseSpinner = findViewById(R.id.chooseSpinner);
//        pathButton = findViewById(R.id.pathButton);
        ArrayList<String> paths = getIntent().getStringArrayListExtra("paths");
        String shortestPath = "";
        int shortestPathLength = Integer.MAX_VALUE;

        // print all paths and find the shortest path
        StringBuilder pathsString = new StringBuilder();
        for (int i = 0; i < paths.size(); i++) {
            paths.set(i, paths.get(i).substring(0, paths.get(i).length() - 2));
            int pathLength = paths.get(i).split(", ").length;
            pathsString.append("- Path ").append(i + 1).append("(").append(pathLength).append(" stations)").append(": ").append(paths.get(i)).append("\n\n");
            if (paths.get(i).split(", ").length < shortestPathLength) {
                shortestPath = paths.get(i);
                shortestPathLength = pathLength;
            }
        }
        pathsTextView.setText(pathsString.toString());
        pathsString.append("Shortest Path (").append(shortestPathLength).append(" stations): ").append(shortestPath).append("\n");
        pathsString.insert(0, "Please choose a path to view its route:\n\n");
        ArrayAdapter<String>adapterChoice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pathsString.toString().split("\n\n"));
        adapterChoice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSpinner.setAdapter(adapterChoice);
        shortestPathTextView.setText("Shortest Path (" + shortestPathLength + " stations): " + shortestPath);

//        chooseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                pathButton.setEnabled(true);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//                pathButton.setEnabled(false);
//            }
//
//        });


        
    }
    public void GoToMap(View view) {
        Intent a =new Intent(this ,MapActivity.class);
        startActivity(a);
        Toast.makeText(this , "This a Map to guide you.",Toast.LENGTH_SHORT).show();
    }
    public void GoToRoute(View view) {
        if(chooseSpinner.getSelectedItem().toString().equals("Please choose a path to view its route:")) {
            Toast.makeText(this, "Please choose a path", Toast.LENGTH_SHORT).show();
            return;
        }
        String selectedPath = chooseSpinner.getSelectedItem().toString();
        Intent intent = new Intent(PathsActivity.this, RouteActivity.class);
        intent.putExtra("path", selectedPath);
        startActivity(intent);
    }
}