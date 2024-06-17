package com.example.leaflens.Search;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leaflens.FirebaseManager;
import com.example.leaflens.R;
import com.example.leaflens.entity.Disease;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchResultFragment extends Fragment {

    private static final String ARG_PARAM1 = "query";

    private FirebaseManager firebaseManager;
    private String query;
    private ListView searchListView;
    private ArrayList<Disease> diseaseArrayList;
    private SearchArrayAdapter searchArrayAdapter;


    public SearchResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment SearchResultFragmnent.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchResultFragment newInstance(String param1, String param2) {
        SearchResultFragment fragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            query = getArguments().getString(ARG_PARAM1);
        }
        firebaseManager = FirebaseManager.getInstance();
        diseaseArrayList = new ArrayList<>();
        searchArrayAdapter = new SearchArrayAdapter(requireContext(), diseaseArrayList);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_result, container, false);
        TextView searchQuery = view.findViewById(R.id.SearchResult_queryPassed);

        searchListView = view.findViewById(R.id.SearchResult_searchListView);
        searchListView.setAdapter(searchArrayAdapter);

        //starting search
        getRelevantSearchList();

        searchQuery.setText(query);

        return view;
    }

    public void getRelevantSearchList()
    {
        Log.d("SearchFragment", "Starting search call from firebase");
        firebaseManager.fetchRelevantSearchDiseases(query, diseaseArrayList, new FirebaseManager.OnSearchListFetchListener() {
            @Override
            public void onListFetched(ArrayList<Disease> diseaseList) {
                diseaseArrayList = diseaseList;
                searchArrayAdapter.notifyDataSetChanged();
                Log.e("SearchFragment", Integer.toString(diseaseList.size()));
            }
        });
    }
}