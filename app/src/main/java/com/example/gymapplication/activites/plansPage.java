package com.example.gymapplication.activites;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymapplication.R;
import com.example.gymapplication.adaptors.PlanAdaptor;
import com.example.gymapplication.methods.Exercise;
import com.example.gymapplication.methods.GymContract;
import com.example.gymapplication.methods.GymDatabase;
import com.example.gymapplication.methods.Plan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class plansPage extends AppCompatActivity {
    private ArrayList<String> planData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plans_page);

        ListView planList = findViewById(R.id.plan_list);

        // Retrieve the plan details JSON string from the intent
        String planListJson = getIntent().getStringExtra("PLAN_DETAILS_JSON");

        // Deserialize the JSON string back into an ArrayList of Plan objects
        Type planListType = new TypeToken<ArrayList<Plan>>(){}.getType();
        ArrayList<Plan> plans = new Gson().fromJson(planListJson, planListType);


        GymDatabase db = new GymDatabase(this);
        Cursor cursor = db.getAllPlans();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No plans found", Toast.LENGTH_SHORT).show();
        }

        planData = new ArrayList<>(); // ArrayList to store the plan data
        // Updated to store ArrayList<Exercise>
        ArrayList<ArrayList<Exercise>> planInfo = new ArrayList<>(); // ArrayList to store the plan data


        // Create an ArrayAdapter to display the plan data in the ListView
        PlanAdaptor adapter = new PlanAdaptor(this, planData, planInfo);                                         // Create the ArrayAdapter
        planList.setAdapter(adapter);
        planList.setDividerHeight(20);

        // Set the listener for delete button clicks
        adapter.setOnDeleteButtonClickListener(new PlanAdaptor.OnDeleteButtonClickListener() {                          // Set the listener for delete button clicks
            @Override
            public void onDeleteButtonClick(int position) {                                                             // When the delete button is clicked
                // Get the plan ID from the database using the position
                GymDatabase db = new GymDatabase(plansPage.this);                                                // Create a GymDatabase object
                int planId = db.getPlanIdByPosition(position);                                                          // Get the plan ID from the database using the position

                // Delete the plan from the database
                db.deletePlan(planId);                                                                                  // Delete the plan from the database

                // Remove the plan from the planData ArrayList and notify the adapter
                planData.remove(position);                                                                              // Remove the plan from the planData ArrayList
                adapter.notifyDataSetChanged();                                                                         // Notify the adapter that the data has changed
            }
        });

        while (cursor.moveToNext()) {
            String planType = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.PlanEntry.COLUMN_NAME_TYPE)); // Get the plan type from the database
            String planExercises = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.PlanEntry.COLUMN_NAME_EXERCISE_ID)); // Get the plan exercise IDs from the database
            int planNumber = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.PlanEntry.COLUMN_NAME_PLAN_NUMBER)); // Get the plan number from the database

            ArrayList<Exercise> exercises = new ArrayList<>();

            // Extract only the exercise IDs
            if (planExercises != null) {
                Log.d("JSON", "JSON String: " + planExercises);

                // Parse the JSON string to get the list of exercise IDs
                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList<String> exerciseIds = new Gson().fromJson(planExercises, type);

                // Retrieve each exercise from the exercise table based on its ID
                db = new GymDatabase(this); // Initialize your database helper class
                SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

                for (String exerciseId : exerciseIds) {
                    Exercise exercise = GymDatabase.getExerciseByUUID(getApplicationContext() , exerciseId);

                    if (exercise != null) {
                        Log.d("Exercise", "Exercise Name: " + exercise.getName());
                        exercises.add(exercise);
                    }
                }

                // Close the database connection
                sqLiteDatabase.close();
            } else {
                Log.d("JSON", "planExercises is null");
            }

            // Format the plan data
            StringBuilder workoutDetails = new StringBuilder();
            workoutDetails.append(planType).append(planNumber).append(": ");

            for (Exercise exercise : exercises) {
                workoutDetails.append(exercise.getName()).append(", ");
            }

            planData.add(workoutDetails.toString()); // Add the plan details to the ArrayList

            // Add the list of Exercise objects to the planInfo ArrayList
            planInfo.add(exercises);
        }

        cursor.close();
    }

    public void openPlanDetails(ArrayList<Exercise> selectedPlanExercises) {
        // Create an intent to open the plan details activity
        Intent intent = new Intent(this, workoutPage.class);

        // Convert the list of exercises to JSON string using Gson
        String planDetailsJson = new Gson().toJson(selectedPlanExercises);

        // Pass the plan details JSON string to the new activity
        intent.putExtra("PLAN_DETAILS_JSON", planDetailsJson);

        // Start the activity
        startActivity(intent);
    }

}
