package com.example.metroapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void GoToMap(View view) {
        Intent a =new Intent(this ,MapActivity.class);
        startActivity(a);
        Toast.makeText(this , "This a Map to guide you.",Toast.LENGTH_SHORT).show();
    }

    public void GoToPaths(View view) {
        Intent b =new Intent(this ,PathsActivity.class);
        startActivity(b);
        Toast.makeText(this , "This is the shortest paths to make you reach the destination.",Toast.LENGTH_LONG).show();
    }
}