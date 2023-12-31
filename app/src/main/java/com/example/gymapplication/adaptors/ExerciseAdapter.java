package com.example.gymapplication.adaptors;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gymapplication.R;
import com.example.gymapplication.methods.Exercise;
import com.example.gymapplication.methods.KeyboardVisibilityHelper;

import java.util.ArrayList;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    private Context context;


    public ExerciseAdapter(Context context, Exercise[] preferences) {
        super(context, 0, preferences);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_for_creation, parent, false);

            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image_preference);
            holder.listView = convertView.findViewById(R.id.text_preference);
            holder.checkBox = convertView.findViewById(R.id.checkBox);
            holder.setsEditText = convertView.findViewById(R.id.sets);
            holder.repsEditText = convertView.findViewById(R.id.reps);
            holder.weightEditText = convertView.findViewById(R.id.weight);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Exercise item = getItem(position);
        if (item != null) {
            holder.imageView.setImageResource(item.getImageResource());
            holder.listView.setText(item.getName());
            holder.checkBox.setChecked(item.isSelected());

            // Update the sets and reps for each exercise based on user input
            if (item.getSets() != null) {
                holder.setsEditText.setText(String.valueOf(item.getSets()));
            } else {
                holder.setsEditText.setText("");
            }

            if (item.getReps() != null) {
                holder.repsEditText.setText(String.valueOf(item.getReps()));
            } else {
                holder.repsEditText.setText("");
            }

            if (item.getWeight() != null) {
                holder.weightEditText.setText(String.valueOf(item.getWeight()));
            } else {
                holder.weightEditText.setText("");
            }


            // TextWatcher for setsEditText
            holder.setsEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()) {
                        try {
                            int sets = Integer.parseInt(s.toString());
                            item.setSets(sets);
                        } catch (NumberFormatException e) {
                            showToast("Invalid input. Please enter an integer.");
                        }
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });


            // TextWatcher for repsEditText
            holder.repsEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()) {
                        try {
                            int reps = Integer.parseInt(s.toString());
                            item.setReps(reps);
                        } catch (NumberFormatException e) {
                            showToast("Invalid input. Please enter an integer.");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            // TextWatcher for weightEditText
            holder.weightEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (!s.toString().isEmpty()) {
                        try {
                            int weight = Integer.parseInt(s.toString());
                            item.setWeight(weight);
                        } catch (NumberFormatException e) {
                            showToast("Invalid input. Please enter an integer.");
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setSelected(isChecked);
                }
            });



        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
        TextView listView;
        CheckBox checkBox;
        EditText setsEditText;
        EditText repsEditText;
        EditText weightEditText;
    }


    public ArrayList<Exercise> getSelectedExercise() {
        ArrayList<Exercise> selectedExercise = new ArrayList<>();
        for (int i = 0; i < getCount(); i++) {
            Exercise item = getItem(i);
            if (item.isSelected()) {
                selectedExercise.add(item);
            }
        }
        return selectedExercise;
    }

    public void showRepsSetsWeightFields(boolean show) {
        for (int i = 0; i < getCount(); i++) {
            Exercise item = getItem(i);
            if (item != null) {
                item.setShowRepsSetsWeight(show);
            }
        }
        notifyDataSetChanged();
    }

    private void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}

