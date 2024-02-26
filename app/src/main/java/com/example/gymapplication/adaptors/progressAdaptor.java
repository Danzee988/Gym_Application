package com.example.gymapplication.adaptors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gymapplication.R;
import com.example.gymapplication.activites.plansPage;
import com.example.gymapplication.activites.workoutPage;
import com.example.gymapplication.methods.Exercise;
import com.example.gymapplication.methods.GymDatabase;
import com.google.gson.Gson;

import java.util.ArrayList;

import java.util.ArrayList;

public class progressAdaptor extends ArrayAdapter<String> {

    private ArrayList<ArrayList<Exercise>> planInfoList;


    public progressAdaptor(Context context, ArrayList<String> plans, ArrayList<ArrayList<Exercise>> planInfoList) {

        super(context, 0, plans);
        this.planInfoList = planInfoList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_of_progress_items, parent, false);
        }

        // Get the plan item at the specified position
        String planItem = getItem(position);

        // Get the views in the custom layout
        TextView planTypeTextView = convertView.findViewById(R.id.plan_type);
        TextView planNumberTextView = convertView.findViewById(R.id.plan_number);
        TextView planContentTextView = convertView.findViewById(R.id.plan_content);

        // Get the delete and run buttons
        Button progressButton = convertView.findViewById(R.id.btn_progress);

        // Split the plan item into title and exercises
        String[] titleAndExercises = planItem.split(": ", 3);
        if (titleAndExercises.length >= 3) {
            String title = titleAndExercises[0];
            String number = titleAndExercises[1];
            String exercises = titleAndExercises[2];

            // Set the data to the views
            planTypeTextView.setText(title);
            planNumberTextView.setText(number);
            planContentTextView.setText(exercises);
        } else {
            // Handle the case where the plan item doesn't contain enough data
            planTypeTextView.setText("");
            planNumberTextView.setText("");
            planContentTextView.setText("");
        }

        // Set the click listener for the run button
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the clicked plan details (list of exercises)
                ArrayList<Exercise> selectedPlanExercises = planInfoList.get(position);

                // Call the method to open the plan details activity
                openPlanDetails(selectedPlanExercises);
            }
        });

        return convertView;
    }

    public void openPlanDetails(ArrayList<Exercise> selectedPlanExercises) {
        // Create an intent to open the plan details activity
        Intent intent = new Intent(getContext(), workoutPage.class);
        for (int i = 0; i < selectedPlanExercises.size(); i++) {
            Log.d("PlanAdaptor", "exercise " + selectedPlanExercises.get(i).getName() +
                    " sets " + selectedPlanExercises.get(i).getSets() +
                    " reps " + selectedPlanExercises.get(i).getReps() +
                    " weight " + selectedPlanExercises.get(i).getWeight());
        }

        // Convert the list of exercises to JSON string using Gson
        String planDetailsJson = new Gson().toJson(selectedPlanExercises);

        // Pass the plan details JSON string to the new activity
        intent.putExtra("PLAN_DETAILS_JSON", planDetailsJson);

        // Start the activity
        getContext().startActivity(intent);
    }
}
