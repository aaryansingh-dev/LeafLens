package com.example.leaflens.homepage;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.leaflens.FirebaseManager;
import com.example.leaflens.R;
import com.example.leaflens.SearchResultFragment;
import com.example.leaflens.entity.News;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomepageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomepageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // variables for news feature
    ListView updateListView;
    private ArrayList<News> newsList;
    private NewsArrayAdapter newsArrayAdapter;
    TextView noUpdateTextView;

    // variables for search feature
    private EditText searchEdit;

    FirebaseManager dbManager;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomepageFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomepageFrament.
     */
    // TODO: Rename and change types and number of parameters
    public static HomepageFragment newInstance(String param1, String param2) {
        HomepageFragment fragment = new HomepageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dbManager = FirebaseManager.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_homepage_fragment, container, false);

        updateListView = view.findViewById(R.id.homepage_expert_update_ListView);
        TextView noUpdateText = view.findViewById(R.id.homepage_expert_noUpdate_text);

        newsList = new ArrayList<>();
        newsArrayAdapter = new NewsArrayAdapter(requireContext(), newsList);
        updateListView.setAdapter(newsArrayAdapter);

        searchEdit = view.findViewById(R.id.homepage_search_editText);

        populateNewsList(noUpdateText);
        initializeSearchbar(searchEdit);
        return view;
    }

    private void populateNewsList(TextView noUpdateTextView)
    {
        dbManager.fetchNews(newsList, new FirebaseManager.fetchNewsListener() {
            @Override
            public void onNewsFetched(ArrayList<News> newsNewList) {
                if(newsNewList.size() == 0)
                {
                    updateListView.setVisibility(View.INVISIBLE);
                    noUpdateTextView.setVisibility(View.VISIBLE);
                }
                newsArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initializeSearchbar(EditText searchEdit)
    {
        Log.d("Homepage", "Initializing Search bar");
        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN))
                {
                    String query = searchEdit.getText().toString().trim();
                    if(!query.isEmpty())
                    {
                        openSearchResultFragment(query);
                        searchEdit.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void openSearchResultFragment(String query)
    {
        Log.d("Transaction-Homepage", "Attempting to open SearchResultFragment");
        Bundle bundle = new Bundle();
        bundle.putString("query", query);

        // start transaction for result page
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        searchResultFragment.setArguments(bundle);

        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.dynamic_container, searchResultFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}