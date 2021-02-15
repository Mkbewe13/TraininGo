package com.example.trainingo20;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ExerciseVolumeAdapter extends ArrayAdapter<Exercise> {

    private static final String TAG = "ExerciseVolumeListAdapter";

    private Context mContext;
    int mResource;


    public ExerciseVolumeAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Exercise> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final Exercise exercise = getItem(position);
        final EditText reps;
        final EditText series;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);


        TextView exerciseNameTextView = convertView.findViewById(R.id.textView_ExerciseName_row_createPlanSetVolume);
        exerciseNameTextView.setText(exercise.getName());

        series = convertView.findViewById(R.id.editTextNumber_series_row_createPlanSetVolume);
        reps = convertView.findViewById(R.id.editTextNumber_reps_row_createPlanSetVolume);

        reps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CheckEdittext(reps) && CheckEdittext(series)) {


                    exercise.setReps(Integer.parseInt(reps.getText().toString()));
                    exercise.setFieldOk(true);

                } else {
                    exercise.setFieldOk(false);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        series.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (CheckEdittext(series) && CheckEdittext(reps)) {
                    exercise.setSeries(Integer.parseInt(series.getText().toString()));
                    exercise.setFieldOk(true);
                } else {

                    exercise.setFieldOk(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return convertView;
    }


    private boolean CheckEdittext(EditText e) {
        String text = e.getText().toString();
        if (text.length() > 0 && text.matches("\\d{1,2}") && !text.matches("0\\d|0")) {
            e.setBackgroundResource(R.drawable.custom_edittext_round_rect_valid);
            return true;
        }
        e.setBackgroundResource(R.drawable.custom_edittext_round_rect_invalid);
        return false;

    }


}
