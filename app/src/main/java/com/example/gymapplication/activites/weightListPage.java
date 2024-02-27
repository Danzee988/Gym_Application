package com.example.gymapplication.activites;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymapplication.R;

import java.util.ArrayList;

public class weightListPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_list);

        // Retrieve the weight list from the Intent extras
        ArrayList<String> weightList = getIntent().getStringArrayListExtra("WEIGHT_LIST");

        // Find the ListView in the layout
        ListView listView = findViewById(R.id.list);

        // Create an ArrayAdapter to display the weight list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, weightList);

        // Set the adapter for the ListView
        listView.setAdapter(adapter);
    }
}
