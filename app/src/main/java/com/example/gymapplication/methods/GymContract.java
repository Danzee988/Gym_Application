package com.example.gymapplication.methods;

import android.provider.BaseColumns;

public class GymContract {
    public static class PlanEntry implements BaseColumns {
        public static final String TABLE_NAME = "plans";
        public static final String COLUMN_NAME_TYPE = "type";
        public static final String COLUMN_NAME_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_NAME_PLAN_NUMBER = "plan_number";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_TYPE + " TEXT," +
                COLUMN_NAME_PLAN_NUMBER + " INTEGER," +
                COLUMN_NAME_EXERCISE_ID + " TEXT" +
                ");";
    }

    public static class ExercisesEntry implements BaseColumns {
        public static final String TABLE_NAME = "exercises";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_IMAGE_RESOURCE = "imageResource";
        public static final String COLUMN_NAME_SETS = "sets";
        public static final String COLUMN_NAME_REPS = "reps";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_EXERCISE = "exercise";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " TEXT," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_IMAGE_RESOURCE + " INTEGER," +
                COLUMN_NAME_SETS + " INTEGER," +
                COLUMN_NAME_REPS + " INTEGER," +
                COLUMN_NAME_WEIGHT + " INTEGER," +
                COLUMN_NAME_EXERCISE + " TEXT" +
                ");";
    }
}
