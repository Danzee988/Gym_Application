package com.example.gymapplication.activites;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymapplication.methods.Exercise;
import com.example.gymapplication.adaptors.ExerciseAdapter;
import com.example.gymapplication.R;
import com.example.gymapplication.methods.GymContract;
import com.example.gymapplication.methods.GymDatabase;
import com.example.gymapplication.methods.KeyboardVisibilityHelper;
import com.example.gymapplication.methods.Plan;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class planCreationPage extends AppCompatActivity {
    private Button btnPush, btnPull,btnSubmit;
    private ListView listExercise;
    private String workoutType;
    private ExerciseAdapter adapter;
    public HashMap<String, ArrayList<Exercise>> pushList = new HashMap<>();
    public HashMap<String, ArrayList<Exercise>> pullList = new HashMap<>();
    private HashMap<String, Plan> plansMap = new HashMap<>();
    int x;
    int y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_creation_page);

        btnPull = findViewById(R.id.btnPull);
        btnPush = findViewById(R.id.btnPush);
        listExercise = findViewById(R.id.list_exercises);
        btnSubmit = findViewById(R.id.btn_submit);


        btnPush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutType = "Push: ";
                Exercise[] preferences = {new Exercise("Incline Dumbbell Press", R.drawable.incline_dumbbell_press), new Exercise("Flat Dumbbell Press", R.drawable.flat_press),
                        new Exercise("Incline Fly's", R.drawable.incline_flyes),new Exercise("Machine Press", R.drawable.machine_press),
                        new Exercise("Machine Fly's", R.drawable.machine_flys),new Exercise("Shoulder Press", R.drawable.shoulder_press),
                        new Exercise("Lateral Raises", R.drawable.lateral_raises), new Exercise("Rear Delts", R.drawable.rear_delts),
                        new Exercise("Cable Triceps Extensions ", R.drawable.triceps_extentions), new Exercise("Revers Triceps Extensions", R.drawable.revers_pushdown)};

                adapter = new ExerciseAdapter(planCreationPage.this, preferences);
                listExercise.setAdapter(adapter);
                listExercise.setDividerHeight(20);

            }
        });

        btnPull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutType = "Pull: ";
                Exercise[] preferences = {new Exercise("Wide Lat Pull Down", R.drawable.wide_lat_puldown),
                        new Exercise("Wide Seated Rows", R.drawable.wide_rows), new Exercise("Close Seated Rows", R.drawable.close_rows),new Exercise("Close Lat Pull Down", R.drawable.close_pulldown),
                        new Exercise("Cable Curls", R.drawable.cable_curls),new Exercise("Cable Hammers", R.drawable.cable_hammers),new Exercise("Dumbbell Hammers", R.drawable.dumbbell_hammers),
                        new Exercise("Dumbbell Curls", R.drawable.dumbbell_curls)};

                adapter = new ExerciseAdapter(planCreationPage.this, preferences);
                listExercise.setAdapter(adapter);
                listExercise.setDividerHeight(20);

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = workoutType;

                GymDatabase db = new GymDatabase(planCreationPage.this);                         // Create a new instance of the database
                SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                // Retrieve the selected preferences from the adapter if needed
                ArrayList<Exercise> selectedExercise = adapter.getSelectedExercise();                // Retrieve the selected preferences from the adapter
                Exercise[] selectedExercisesArray = selectedExercise.toArray(new Exercise[0]);          // Convert the ArrayList to an array
                Plan plan = new Plan(type, selectedExercisesArray);
                // Add the plan to the HashMap
                plansMap.put(type, plan);

                ArrayList<Exercise> exercises = new ArrayList<>();                                      // Create a list to store the selected exercises

                for (int i = 0; i < selectedExercise.size(); i++) {                                     // Loop through all the selected exercises
                    Exercise exercise = selectedExercise.get(i);                                      // Retrieve the selected exercise

                    String exerciseId = UUID.randomUUID().toString();
                    String Name = exercise.getName();
                    int exerciseSets = exercise.getSets();
                    int exerciseReps = exercise.getReps();
                    int exerciseWeight = exercise.getWeight();
                    int exerciseImage = exercise.getImageResource();

                    exercise.setId(exerciseId);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_ID, exerciseId); // Store the generated UUID in the "id" column
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_NAME, Name);
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_SETS, exerciseSets);
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_REPS, exerciseReps);
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_WEIGHT, exerciseWeight);
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_IMAGE_RESOURCE, new Gson().toJson(exerciseImage));
                    exercises.add(exercise);                                            // Add the selected exercise to the list
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_EXERCISE, new Gson().toJson(exercises));

                    long newRowId = sqLiteDatabase.insert(GymContract.ExercisesEntry.TABLE_NAME, null, contentValues);

                }

                if (workoutType.equals("Push: ")) {                                                     // If the workout type is push, add the exercises to the pushList
                    x++;
                    pushList.put(type + x , exercises);                                                 // Add the exercises to the pushList
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(GymContract.PlanEntry.COLUMN_NAME_TYPE, type);
                    contentValues.put(GymContract.PlanEntry.COLUMN_NAME_PLAN_NUMBER, x);
                    // Create a list to store exercise IDs
                    ArrayList<String> exerciseIds = new ArrayList<>();

                    // Iterate through exercises and add their IDs to the list
                    for (int i = 0; i < exercises.size(); i++) {
                        exerciseIds.add(exercises.get(i).getId());
                    }

                    // Convert the list of exercise IDs to a JSON string
                    String exerciseIdsJson = new Gson().toJson(exerciseIds);

                    // Put the JSON string in the COLUMN_NAME_EXERCISE_IDS column
                    contentValues.put(GymContract.PlanEntry.COLUMN_NAME_EXERCISE_ID, exerciseIdsJson);

                    long newRowId = sqLiteDatabase.insert(GymContract.PlanEntry.TABLE_NAME, null, contentValues);

                } else if (workoutType.equals("Pull: ")) {                                              // If the workout type is pull, add the exercises to the pullList
                    y++;
                    pullList.put(type + y , exercises);                                                 // Add the exercises to the pullList
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(GymContract.PlanEntry.COLUMN_NAME_TYPE, type);
                    contentValues.put(GymContract.PlanEntry.COLUMN_NAME_PLAN_NUMBER, y);
                    // Create a list to store exercise IDs
                    ArrayList<String> exerciseIds = new ArrayList<>();

                    // Iterate through exercises and add their IDs to the list
                    for (int i = 0; i < exercises.size(); i++) {
                        exerciseIds.add(exercises.get(i).getId());
                    }

                    // Convert the list of exercise IDs to a JSON string
                    String exerciseIdsJson = new Gson().toJson(exerciseIds);

                    // Put the JSON string in the COLUMN_NAME_EXERCISE_IDS column
                    contentValues.put(GymContract.PlanEntry.COLUMN_NAME_EXERCISE_ID, exerciseIdsJson);

                    long newRowId = sqLiteDatabase.insert(GymContract.PlanEntry.TABLE_NAME, null, contentValues);

                }

                Intent intent = new Intent(planCreationPage.this, plansPage.class);
                intent.putExtra("pushList", pushList);
                Log.d("pushList", String.valueOf(pushList));
                intent.putExtra("pullList", pullList);
                startActivity(intent);

            }
        });

    }
}

