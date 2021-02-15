package com.example.trainingo20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ExerciseStatsAdapter extends ArrayAdapter<String> {

    private static final String TAG = "ExerciseVolumeListAdapter";

    private Context mContext;
    int mResource;

    public ExerciseStatsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String exerciseName = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);


        TextView exerciseNameTextView = convertView.findViewById(R.id.textView_ExerciseName_row_Stats);
        exerciseNameTextView.setText(exerciseName);

        return convertView;
    }
}
