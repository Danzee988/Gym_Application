package com.example.gymapplication.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.gymapplication.R;
import com.example.gymapplication.adaptors.ExerciseAdapter;
import com.example.gymapplication.adaptors.workoutAdaptor;
import com.example.gymapplication.methods.Exercise;
import com.example.gymapplication.methods.GymDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class workoutPage extends AppCompatActivity {
    Context context = this;
    private ArrayList<Exercise> exercises;
    private workoutAdaptor workoutAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_page);

        ListView workoutList = findViewById(R.id.workout_list);
        Button btnComplete = findViewById(R.id.btn_complete);

        // Retrieve the plan details JSON string from the intent
        String planDetailsJson = getIntent().getStringExtra("PLAN_DETAILS_JSON");

        // Deserialize the JSON string back into an ArrayList of Exercise objects
        Type type = new TypeToken<ArrayList<Exercise>>() {}.getType();
        exercises = new Gson().fromJson(planDetailsJson, type);

        // Create the custom adapter for the exercises
        workoutAdapter = new workoutAdaptor(this, exercises.toArray(new Exercise[0]));

        // Set the adapter to the ListView
        workoutList.setAdapter(workoutAdapter);
        workoutList.setDividerHeight(20);

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < exercises.size(); i++) {
                    Log.d("workoutPage", "exercise " + exercises.get(i).getName() +
                            " sets " + exercises.get(i).getSets() +
                            " reps " + exercises.get(i).getReps() +
                            " weight " + exercises.get(i).getWeight());
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 123 && resultCode == RESULT_OK) {
            // Check if the requestCode matches and if the result is successful
            if (data != null && data.hasExtra("UPDATED_EXERCISE_JSON")) {
                String updatedExerciseJson = data.getStringExtra("UPDATED_EXERCISE_JSON");

                // Parse the updated exercise from JSON
                Exercise updatedExercise = new Gson().fromJson(updatedExerciseJson, Exercise.class);
                // Find the index of the updated exercise in the original list
                for (int i = 0; i < exercises.size(); i++) {
                    if (exercises.get(i).getId().equals(updatedExercise.getId())) {
                        Log.d("workoutPage", "Index " + i);
                        exercises.get(i).setSets(updatedExercise.getSets());
                        exercises.get(i).setReps(updatedExercise.getReps());
                        exercises.get(i).setWeight(updatedExercise.getWeight());
                        break;
                    }
                }
                    // Notifies the adapter that the data has changed
                    workoutAdapter.notifyDataSetChanged();
            }
        }
    }
}
