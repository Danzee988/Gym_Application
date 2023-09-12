package com.example.gymapplication.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.gymapplication.R;
import com.example.gymapplication.methods.Exercise;
import com.example.gymapplication.methods.GymDatabase;
import com.example.gymapplication.methods.Plan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class navigationPage extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_page);
    }

    public void onCreatePlanClick(View view) {
        Intent intent = new Intent(this, planCreationPage.class);
        startActivity(intent);
    }

    public void onPlanSelectionClick(View view) {
        GymDatabase db = new GymDatabase(this);
        //Cursor plans = db.getAllPlans();
        ArrayList<Plan> planList = (ArrayList<Plan>) db.getAllPlans1();
        //Log.d("plans", "HERE " + plans.toString());
        openPlanDetails(planList);
    }

//    private ArrayList<Plan> convertCursorToArrayList(Cursor cursor) {
//        ArrayList<Plan> planList = new ArrayList<>();
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                // Assuming you have a Plan class to represent your plan data
//                Plan plan = new Plan();
//                // Populate plan object from cursor columns
//                // plan.setId(cursor.getInt(cursor.getColumnIndex("id")));
//                plan.setName(cursor.getColumnName(cursor.getColumnIndex("name"))));
//                // plan.setName(cursor.getString(cursor.getColumnIndex("name")));
//                // ...
//
//                planList.add(plan);
//            } while (cursor.moveToNext());
//        }
//
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        return planList;
//    }
    public void openPlanDetails(ArrayList<Plan> planList) {
        // Create an intent to open the plan details activity
        Intent intent = new Intent(this, plansPage.class);

        // Create a TypeToken for Gson to correctly serialize the ArrayList<Plan>
        Type type = new TypeToken<ArrayList<Plan>>() {}.getType();

        // Convert the list of plans to JSON string using Gson
        String planListJson = new Gson().toJson(planList, type);


        // Pass the plan details JSON string to the new activity
        intent.putExtra("PLAN_DETAILS_JSON", planListJson);

        // Start the activity
        startActivity(intent);
    }

}
