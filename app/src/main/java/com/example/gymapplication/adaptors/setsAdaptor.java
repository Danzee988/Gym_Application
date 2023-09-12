package com.example.gymapplication.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.gymapplication.R;
import com.example.gymapplication.methods.Exercise;

import java.util.ArrayList;

public class setsAdaptor extends ArrayAdapter<Exercise>{
    private Context context;
    public int set = 0;

    public int sets;

    public int reps;
    private String exerciseName;
    private ArrayList<Exercise> exercises;


    public setsAdaptor(Context context, ArrayList<Exercise> exercises, String exerciseName) {
        super(context, 0, exercises);
        this.exercises = exercises;
        this.exerciseName = exerciseName;
    }



    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Exercise item = getItem(position);
        if (exerciseName == item.getName()){
            Log.d("setsAdaptor", "sets: " + item.getSets());
            Log.d("setsAdaptor", "reps: " + item.getReps());
        }

        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sets, parent, false);

            holder = new ViewHolder();
            holder.setNumber = convertView.findViewById(R.id.set_number);
            holder.repsText = convertView.findViewById(R.id.number_of_reps);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Exercise exercise = exercises.get(position);
        if (exercise.getName().equals(exerciseName)) {
            // Update the sets and reps for each exercise based on user input
            holder.setNumber.setText(String.valueOf(exercise.getSets())); // Convert to String before setting text
            holder.repsText.setText(String.valueOf(exercise.getReps())); // Convert to String before setting text
        }

        holder.repsText.setText(String.valueOf(reps)); // Convert to String before setting text



        return convertView;
    }

    private static class ViewHolder {
        TextView setNumber;
        EditText repsText;
        EditText weightText;
    }
}
