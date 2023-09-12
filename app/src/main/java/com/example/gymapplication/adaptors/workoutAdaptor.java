package com.example.gymapplication.adaptors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gymapplication.R;
import com.example.gymapplication.activites.exercisePage;
import com.example.gymapplication.activites.workoutPage;
import com.example.gymapplication.methods.Exercise;
import com.google.gson.Gson;

import java.util.ArrayList;

public class workoutAdaptor extends ArrayAdapter<Exercise> {
    private Context context;


    public workoutAdaptor(Context context, Exercise[] exercises) {

        super(context, 0, exercises);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.plan_items, parent, false);

            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image_preference);
            holder.listView = convertView.findViewById(R.id.text_preference);
            holder.setsText = convertView.findViewById(R.id.sets);
            holder.repsText = convertView.findViewById(R.id.reps);
            holder.weightText = convertView.findViewById(R.id.weight);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Exercise item = getItem(position);
        if (item != null) {
            holder.imageView.setImageResource(item.getImageResource());
            holder.listView.setText(item.getName());

            // Update the sets and reps for each exercise based on user input
            holder.setsText.setText(String.valueOf(item.getSets()));
            holder.repsText.setText(String.valueOf(item.getReps()));
            holder.weightText.setText(String.valueOf(item.getWeight()));
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Exercise exercise = getItem(position);
                if (exercise != null) {
                    openPlanDetails(exercise);

                }
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView listView;
        TextView setsText;
        TextView repsText;
        TextView weightText;
    }


    public ArrayList<Exercise> getSelectedPreferences() {
        ArrayList<Exercise> selectedPreferences = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            Exercise item = getItem(i);
            if (item.isSelected()) {
                selectedPreferences.add(item);
            }
        }
        return selectedPreferences;
    }

    public void openPlanDetails(Exercise exercise) {
        // Create an intent to open the plan details activity
        Intent intent = new Intent(getContext(), exercisePage.class);

        // Convert the list of exercises to JSON string using Gson
        String planDetailsJson = new Gson().toJson(exercise);

        // Pass the plan details JSON string to the new activity
        intent.putExtra("PLAN_DETAILS_JSON", planDetailsJson);

        // Start the activity
        ((Activity) getContext()).startActivityForResult(intent, 123);
    }

}
