package com.radwan.metroapp;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
    ArrayList<String> paths = new ArrayList<String>();
    
    Spinner chooseSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paths);

        TextView pathsTextView;
        TextView shortestPathTextView;
        
        pathsTextView = findViewById(R.id.pathsTextView);
        shortestPathTextView = findViewById(R.id.shortestPathTextView);
        chooseSpinner = findViewById(R.id.chooseSpinner);
        
        paths = getIntent().getStringArrayListExtra("paths");

        StringBuilder pathsStringBuilder = new StringBuilder();
        String shortestPath = "";
        int shortestPathLength = Integer.MAX_VALUE;

        // fill the spinner with the paths
        pathsStringBuilder.append("Please choose a path to view its route: \n\n");
        for (int i = 0; i < paths.size(); i++) {
            int pathLength = paths.get(i).split(", ").length;

            pathsStringBuilder.append("- Path ").append(i + 1).append("(").append(pathLength).append(" stations)").append(": , ").append(paths.get(i)).append("\n\n");

            if (pathLength < shortestPathLength) {
                shortestPath = paths.get(i);
                shortestPathLength = pathLength;
            }
        }

        pathsTextView.setText(pathsStringBuilder.toString());
        pathsStringBuilder.append("Shortest Path (").append(shortestPathLength).append(" stations): ").append(shortestPath).append("\n");

        ArrayAdapter<String>adapterChoice = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, pathsStringBuilder.toString().split("\n\n"));
        adapterChoice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseSpinner.setAdapter(adapterChoice);
        shortestPathTextView.setText("\nShortest Path (" + shortestPathLength + " stations): " + shortestPath + '\n');
    }

    public void GoToMap(View view) {
        Intent a =new Intent(this ,MapActivity.class);
        startActivity(a);
        Toast.makeText(this , "This a Map to guide you.",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}