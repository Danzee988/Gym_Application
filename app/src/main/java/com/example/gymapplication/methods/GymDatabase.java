package com.example.gymapplication.methods;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;

public class GymDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "gym.db";
    private static final int DATABASE_VERSION = 2;

    public GymDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the "exercise" table to store individual exercises
        db.execSQL("CREATE TABLE exercises (id TEXT, name TEXT, imageResource INTEGER, sets INTEGER, reps INTEGER, weight INTEGER,exercise TEXT)"); // Create the "exercise" table to store individual exercises

        // Create the "plans" table to store plans and their associated exercises
        db.execSQL("CREATE TABLE plans (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT,exercise_id TEXT, plan_number INTEGER)"); // Add the "plan_number" column
    }

    public static Exercise getExerciseByUUID(Context context , String exerciseUUID) {
        SQLiteDatabase sqLiteDatabase = new GymDatabase(context).getReadableDatabase();

        // Define the columns you want to retrieve from the database
        String[] projection = {
                GymContract.ExercisesEntry.COLUMN_NAME_NAME,
                GymContract.ExercisesEntry.COLUMN_NAME_SETS,
                GymContract.ExercisesEntry.COLUMN_NAME_REPS,
                GymContract.ExercisesEntry.COLUMN_NAME_WEIGHT,
                GymContract.ExercisesEntry.COLUMN_NAME_ID,
                GymContract.ExercisesEntry.COLUMN_NAME_IMAGE_RESOURCE
        };

        // Define the WHERE clause to filter by exercise UUID
        String selection = GymContract.ExercisesEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { exerciseUUID };

        // Perform the database query
        Cursor cursor = sqLiteDatabase.query(
                GymContract.ExercisesEntry.TABLE_NAME,  // The table to query
                projection,                       // The columns to return
                selection,                        // The columns for the WHERE clause
                selectionArgs,                    // The values for the WHERE clause
                null,                             // Don't group the rows
                null,                             // Don't filter by row groups
                null                              // The sort order
        );

        Exercise exercise = null;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                // Extract the exercise details from the cursor
                String name = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.ExercisesEntry.COLUMN_NAME_NAME));
                int sets = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.ExercisesEntry.COLUMN_NAME_SETS));
                int reps = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.ExercisesEntry.COLUMN_NAME_REPS));
                int weight = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.ExercisesEntry.COLUMN_NAME_WEIGHT));
                String id = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.ExercisesEntry.COLUMN_NAME_ID));
                int imageResource = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.ExercisesEntry.COLUMN_NAME_IMAGE_RESOURCE));

                // Create an Exercise object with the retrieved details
                exercise = new Exercise(name, sets, reps, weight, id,imageResource);
            }

            // Close the cursor when done
            cursor.close();
        }

        // Close the database connection
        sqLiteDatabase.close();

        return exercise;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS exercise");  // Drop the "exercise" table
        db.execSQL("DROP TABLE IF EXISTS plans");   // Drop the "plans" table
        onCreate(db); // Recreate the tables
    }

    public Cursor getAllPlans() {  // Method to fetch all plans from the "plans" table
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                GymContract.PlanEntry.COLUMN_NAME_TYPE,
                GymContract.PlanEntry.COLUMN_NAME_PLAN_NUMBER,
                GymContract.PlanEntry.COLUMN_NAME_EXERCISE_ID
        };

        return db.query(
                GymContract.PlanEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }

    public ArrayList<Plan> getAllPlans1() {
        ArrayList<Plan> planList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                GymContract.PlanEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int planId = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.PlanEntry._ID));
                String planName = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.PlanEntry.COLUMN_NAME_TYPE));
                String planExercises = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.PlanEntry.COLUMN_NAME_EXERCISE_ID));

                String[] exerciseNames = planExercises.split(","); // Split the exercise names

                Exercise[] exercises = new Exercise[exerciseNames.length];
                for (int i = 0; i < exerciseNames.length; i++) {
                    exercises[i] = new Exercise(exerciseNames[i]); // Create Exercise objects
                }

                Plan plan = new Plan(planName, exercises);
                planList.add(plan);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return planList;
    }

    public int getPlanIdByPosition(int position) { // Method to get the plan ID from the database using the position
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                GymContract.PlanEntry._ID
        };

        Cursor cursor = db.query(
                GymContract.PlanEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Move the cursor to the specified position
        if (cursor.moveToPosition(position)) {
            // Get the plan ID from the cursor
            int planId = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.PlanEntry._ID));
            cursor.close();
            return planId;
        }

        cursor.close();
        // If the position is invalid or the cursor cannot move to the specified position, return -1 or any other error code
        return -1;
    }
    public void deletePlan(int planId) { // Method to delete a plan from the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Define the selection and selectionArgs for the delete query
        String selection = GymContract.PlanEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(planId)};

        // Perform the delete operation
        db.delete(
                GymContract.PlanEntry.TABLE_NAME,
                selection,
                selectionArgs
        );
    }

    public void checkSavedPlanTypes() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns you want to retrieve
        String[] projection = {
                GymContract.PlanEntry._ID,
                GymContract.PlanEntry.COLUMN_NAME_TYPE,
                GymContract.PlanEntry.COLUMN_NAME_PLAN_NUMBER
        };

        // Perform the query to retrieve all rows from the "plans" table
        Cursor cursor = db.query(
                GymContract.PlanEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        // Iterate through the cursor to check the saved plan types
        if (cursor.moveToFirst()) {
            do {
                // Get the plan ID and type from the cursor
                int planNum = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.PlanEntry.COLUMN_NAME_PLAN_NUMBER));
                String planType = cursor.getString(cursor.getColumnIndexOrThrow(GymContract.PlanEntry.COLUMN_NAME_TYPE));
                int planId = cursor.getInt(cursor.getColumnIndexOrThrow(GymContract.PlanEntry._ID));

                // Do something with the plan ID and type (e.g., print or log them)
                Log.d("SavedPlan", "Plan number: " + planNum + ", Plan Type: " + planType + "Plan id" + planId);
            } while (cursor.moveToNext());
        }

        // Close the cursor after use
        cursor.close();
    }


}
