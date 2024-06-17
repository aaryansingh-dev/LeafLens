package com.example.leaflens.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.leaflens.R;
import com.example.leaflens.entity.Disease;

public class SearchArrayAdapter extends ArrayAdapter<Disease> {
    public SearchArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.search_result_item, parent, false);
        } else {
            view = convertView;
        }

        Disease currentDisease = getItem(position);
        TextView severityText = view.findViewById(R.id.search_result_item_severity);
        TextView diseaseName = view.findViewById(R.id.search_result_item_Title);
        TextView diseaseCategory = view.findViewById(R.id.search_result_item_Disease_Category);

        severityText.setText(currentDisease.getSeverity());
        diseaseName.setText(currentDisease.getName());
        diseaseCategory.setText(currentDisease.getDiseaseCategory());

        return view;
    }
}
