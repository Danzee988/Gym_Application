package com.example.gymapplication.activites;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymapplication.R;
import com.example.gymapplication.methods.Exercise;
import com.example.gymapplication.methods.GymContract;
import com.example.gymapplication.methods.GymDatabase;
import com.google.gson.Gson;

public class exercisePage extends AppCompatActivity {
    private Button btnDone, btnReturn;
    private TextView nameText, totalSetsText, setNumberText,setNumberText1,setNumberText2,setNumberText3;
    private EditText repsText,repsText1,repsText2,repsText3, weightText, weightText1, weightText2, weightText3;
    private ImageView imageResource;
    //private ArrayList<Exercise> exercises;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_page);

        LinearLayout setsReps1Layout = findViewById(R.id.sets_reps1);
        LinearLayout setsReps2Layout = findViewById(R.id.sets_reps2);
        LinearLayout setsReps3Layout = findViewById(R.id.sets_reps3);

        nameText = findViewById(R.id.exercise_name);
        imageResource = findViewById(R.id.image_preference);

        totalSetsText = findViewById(R.id.number_of_sets);
        setNumberText = findViewById(R.id.set_number);
        setNumberText1 = findViewById(R.id.set_number1);
        setNumberText2 = findViewById(R.id.set_number2);
        setNumberText3 = findViewById(R.id.set_number3);

        repsText = findViewById(R.id.reps_number);
        repsText1 = findViewById(R.id.reps_number1);
        repsText2 = findViewById(R.id.reps_number2);
        repsText3 = findViewById(R.id.reps_number3);

        weightText = findViewById(R.id.weight_number);
        weightText1 = findViewById(R.id.weight_number1);
        weightText2 = findViewById(R.id.weight_number2);
        weightText3 = findViewById(R.id.weight_number3);
        btnDone = findViewById(R.id.btn_done);
        btnReturn = findViewById(R.id.btn_return);

        // Retrieve the plan details JSON string from the intent
        String exerciseDetailsJson = getIntent().getStringExtra("PLAN_DETAILS_JSON");

        // Deserialize the JSON string back into an Exercise of object
        Exercise exercise = new Gson().fromJson(exerciseDetailsJson, Exercise.class);

        String exerciseName = exercise.getName();
        int imageResource = exercise.getImageResource();
        int sets = exercise.getSets();
        int reps = exercise.getReps();
        int weight = exercise.getWeight();

        nameText.setText(exerciseName);
        this.imageResource.setImageResource(imageResource);
        totalSetsText.setText(String.valueOf(sets));
        setNumberText.setText(String.valueOf(1));

        if (sets == 2) {
            setsReps1Layout.setVisibility(View.VISIBLE);
            setNumberText1.setText(String.valueOf(2));
            setsReps2Layout.setVisibility(View.GONE);
            setsReps3Layout.setVisibility(View.GONE);
        } else if (sets == 3) {
            setsReps1Layout.setVisibility(View.VISIBLE);
            setNumberText1.setText(String.valueOf(2));
            setsReps2Layout.setVisibility(View.VISIBLE);
            setNumberText2.setText(String.valueOf(3));
            setsReps3Layout.setVisibility(View.GONE);
        } else if (sets == 4) {
            setsReps1Layout.setVisibility(View.VISIBLE);
            setNumberText1.setText(String.valueOf(2));
            setsReps2Layout.setVisibility(View.VISIBLE);
            setNumberText2.setText(String.valueOf(3));
            setsReps3Layout.setVisibility(View.VISIBLE);
            setNumberText3.setText(String.valueOf(4));
        }

        repsText.setText(String.valueOf(reps));
        repsText1.setText(String.valueOf(reps));
        repsText2.setText(String.valueOf(reps));
        repsText3.setText(String.valueOf(reps));
        weightText.setText(String.valueOf(weight));
        weightText1.setText(String.valueOf(weight));
        weightText2.setText(String.valueOf(weight));
        weightText3.setText(String.valueOf(weight));

        weightText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String str = weightText.getText().toString();
                    int weight = str.isEmpty() ? 0 : Integer.parseInt(str);
                    Log.d("weight", String.valueOf(weight));

                    String str1 = weightText1.getText().toString();
                    int weight1 = str1.isEmpty() ? 0 : Integer.parseInt(str1);
                    Log.d("weight1", String.valueOf(weight1));

                    String str2 = weightText2.getText().toString();
                    int weight2 = str2.isEmpty() ? 0 : Integer.parseInt(str2);
                    Log.d("weight2", String.valueOf(weight2));

                    String str3 = weightText3.getText().toString();
                    int weight3 = str3.isEmpty() ? 0 : Integer.parseInt(str3);
                    Log.d("weight3", String.valueOf(weight3));

                    if (weight > weight1 && weight > weight2 && weight > weight3) {
                        exercise.setWeight(weight2);
                    } else if (weight1 > weight && weight1 > weight2 && weight1 > weight3) {
                        exercise.setWeight(weight1);
                    } else if (weight2 > weight && weight2 > weight1 && weight2 > weight3) {
                        exercise.setWeight(weight2);
                    } else if (weight3 > weight && weight3 > weight1 && weight3 > weight2) {
                        exercise.setWeight(weight3);
                    }
                }
            }
        });


        if (exercise.getWeight() > 0) {
                weightText.setText(String.valueOf(exercise.getWeight()));
            }


            btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the updated values from the UI
                    int updatedSets = Integer.parseInt(totalSetsText.getText().toString());
                    int updatedReps = Integer.parseInt(repsText.getText().toString());
                    int updatedWeight = Integer.parseInt(weightText.getText().toString());

                    // Update the exercise object with the new values
                    exercise.setReps(updatedReps);
                    exercise.setWeight(updatedWeight);

                    // Get the exercise ID
                    String exerciseId = exercise.getId();

                    // Update the exercise in the database
                    GymDatabase db = new GymDatabase(exercisePage.this);
                    SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_SETS, updatedSets);
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_REPS, updatedReps);
                    contentValues.put(GymContract.ExercisesEntry.COLUMN_NAME_WEIGHT, updatedWeight);

                    // Define the WHERE clause to update the exercise with a matching ID
                    String whereClause = GymContract.ExercisesEntry.COLUMN_NAME_ID + " = ?";
                    String[] whereArgs = {exerciseId};

                    int rowsUpdated = sqLiteDatabase.update(
                            GymContract.ExercisesEntry.TABLE_NAME,
                            contentValues,
                            whereClause,
                            whereArgs
                    );

                    if (rowsUpdated > 0) {
                        // Exercise updated successfully
                        Log.d("Exercise Update", "Exercise with ID " + exerciseId + " updated successfully.");
                    } else {
                        // Exercise update failed
                        Log.d("Exercise Update", "Exercise update failed.");
                    }

                    // Close the database connection
                    sqLiteDatabase.close();
                }
            });

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to pass the updated exercise back to the previous activity
                Intent resultIntent = new Intent();
                resultIntent.putExtra("UPDATED_EXERCISE_JSON", new Gson().toJson(exercise));

                // Set the result code to indicate success (you can define your own result code)
                setResult(RESULT_OK, resultIntent);

                // Finish the current activity (exercisePage)
                finish();
            }
        });
        }

    }

    // Iterate through the exercises list
//                        String Name = exercise.getName();
//                        int exerciseSets = exercise.getSets();
//                        int exerciseReps = exercise.getReps();
//                        int exerciseWeight = exercise.getWeight();
//                        exerciseId = exercise.getId();
//
//                        Log.d("Exercise Info", "Exercise: " + Name +
//                                ", ID: " + exerciseId +
//                                ", Sets: " + exerciseSets +
//                                ", Reps: " + exerciseReps +
//                                ", Weight: " + updatedWeight);
