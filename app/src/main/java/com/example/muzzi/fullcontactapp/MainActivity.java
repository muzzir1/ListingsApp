package com.example.muzzi.fullcontactapp;

import android.content.res.Resources;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.os.AsyncTask;
import android.util.Log;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{

    String MovieTitleSearchText;
    //TextView responseView;
    ProgressBar progressBar;
    ListView lv;
    MovieAdapter mAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    MenuItem myActionMenuItem;

    private EndlessRecyclerViewScrollListener scrollListener;
    int Mdb_page = 1;
    Movie m;


    //static final String API_KEY = "64d75c9c2dead776";
    static final String API_URL = "https://api.themoviedb.org/3/search/movie?api_key=3d435f0b6e3f7cc633465e9cad477ca3&language=en-US";
    //ArrayList<HashMap<String, String>> contactlist;
    private List<Movie> movieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //responseView = (TextView) findViewById(R.id.responseView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //lv = (ListView) findViewById(R.id.list);

        //contactlist = new ArrayList<>();

        mAdapter = new MovieAdapter(movieList);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        Log.i("Method","OnCreate() Called");

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                recyclerView, new ClickListener()
        {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(getApplicationContext(), movie.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        scrollListener = new EndlessRecyclerViewScrollListener(lm) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);


    }

    String page ="Page Number";

    public void loadNextDataFromApi(int offset)
    {
        // Send an API request to retrieve appropriate paginated data
        Mdb_page++;
        String x = String.valueOf(Mdb_page);
        Log.d(page, x);
        new RetrieveFeedTask().execute();
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        myActionMenuItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        myActionMenuItem.expandActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                if(TextUtils.isEmpty(query))
                {
                    MovieTitleSearchText ="";
                }
                else
                {
                    movieList.clear();
                    MovieTitleSearchText = query;
                    Mdb_page = 1;
                    new RetrieveFeedTask().execute();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                return false;
            }

        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed()
    {
        if(!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
        }
        else
        {
            super.onBackPressed();
        }
    }

    public interface ClickListener
    {
        void onClick(View view, int position);

        void onLongClick(View view, int position);

    }



    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL(API_URL + "&query=" + MovieTitleSearchText + "&page=" + Mdb_page + "&include_adult=false");

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            JSONArray jsonArray_x;

            JSONObject jsonobject_x;
            String string_Title ="";
            String string_poster ="";
            String string_year="";
            String string_rating="";
            String string_plot="";


            StringBuilder sb = new StringBuilder();

            //ArrayList<6rString> movielistArrayList = new ArrayList<String>();

            if (response == null) {
                response = "THERE WAS AN ERROR";
                progressBar.setVisibility(View.GONE);

            }
            else {

                try {
                    JSONObject jobject = new JSONObject(response);

                    jsonArray_x = jobject.getJSONArray("results");

                    for (int i = 0; i <= jsonArray_x.length(); i++)
                    {
                        jsonobject_x = jsonArray_x.getJSONObject(i);
                        string_Title = jsonobject_x.getString("title");

                        if(string_Title.length() >= 17)
                        {
                            string_Title = string_Title.substring(0,16) + "..";
                        }

                        string_poster = "https://image.tmdb.org/t/p/w300/" + jsonobject_x.getString("poster_path");

                        string_rating = jsonobject_x.getString("vote_average")+ "/10";

                        string_plot = jsonobject_x.getString("overview") + "...";

                        string_year =  jsonobject_x.getString("release_date");
                        string_year = string_year.substring(0,4);
                        string_year = " (" + string_year + ")";

                        Log.d("Year", string_year);

                        m = new Movie(string_Title,string_poster,string_year,string_rating,string_plot);
                        movieList.add(m);


                    }


                } catch (Exception e)
                {

                }
                progressBar.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();




            }


        }
    }
}
