package com.example.gymapplication.methods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlanSelection extends ArrayAdapter<String> {
    private Context context;

    public PlanSelection(Context context, ArrayList<String> data) {
        super(context, 0, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Get the item at the current position
        String item = getItem(position);

        // Set the item text to the TextView in the layout
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(item);

        return convertView;
    }
}

