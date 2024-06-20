package com.example.leaflens.homepage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.leaflens.R;
import com.example.leaflens.Entity.News;

import java.util.ArrayList;

public class NewsArrayAdapter extends ArrayAdapter<News> {

    public NewsArrayAdapter(Context context, ArrayList<News> newsList)
    {
        super(context, 0, newsList);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        } else {
            view = convertView;
        }

        News currentNews = getItem(position);
        TextView newsTitleView = view.findViewById(R.id.homepage_newsItemTitle);
        TextView newsAuthorView = view.findViewById(R.id.homepage_newsItemAuthor);
        TextView newsTextView = view.findViewById(R.id.homepage_newsItemText);

        newsTitleView.setText(currentNews.getNewsTitle());
        newsAuthorView.setText(currentNews.getAuthor());
        newsTextView.setText(currentNews.getText());

        return view;
    }
}
