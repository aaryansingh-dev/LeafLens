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
import com.example.leaflens.Entity.Disease;

import java.util.ArrayList;

public class SearchArrayAdapter extends ArrayAdapter<Disease> {
    public SearchArrayAdapter(@NonNull Context context, ArrayList<Disease> arrayList) {
        super(context, 0, arrayList);
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

        if(currentDisease != null)
        {
            severityText.setText(currentDisease.getSeverity());
            diseaseName.setText(currentDisease.getName());
            diseaseCategory.setText(currentDisease.getDiseaseCategory());
        }

        return view;
    }
}
