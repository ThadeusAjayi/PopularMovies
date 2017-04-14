package com.shopspreeng.android.popularmovies1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.shopspreeng.android.popularmovies1.R.id.popular;


public class PopularMovies extends AppCompatActivity implements  MoviesAdapter.ItemClickListener{


    URL resultPopular, resultTopRated;

    GetMoviesOnline urlTest = new GetMoviesOnline();

    ArrayList<Movie> extractedMovie = new ArrayList<Movie>();

    URL clickedUrl;

    private static final int NUM_OF_COLUMNS = 2;
    private MoviesAdapter adapter;
    private RecyclerView recyclerMovielist;
    private TextView internetError;
    GridLayoutManager layoutManager;
    private int paginatePopular = 1;
    private int paginateTopRated = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

       internetError = (TextView) findViewById(R.id.no_internet);
        recyclerMovielist = (RecyclerView) findViewById(R.id.movie_list);


        layoutManager = new GridLayoutManager(this,NUM_OF_COLUMNS);
        recyclerMovielist.setLayoutManager(layoutManager);
        adapter = new MoviesAdapter(this,extractedMovie);
        recyclerMovielist.setAdapter(adapter);
        adapter.setClickListener(this);





        if(!isThereInternetConnection()) {
            internetError.setVisibility(View.VISIBLE);
        }else{
            resultPopular = urlTest.buildUrl("popular", paginatePopular);
            ImdbQueryTask Task = new ImdbQueryTask();
            internetError.setVisibility(View.INVISIBLE);
            Task.execute(resultPopular);
        }



    }


    protected boolean isThereInternetConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(menu != null){
            getMenuInflater().inflate(R.menu.movie_list_options, menu);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int itemId = item.getItemId();
        switch (itemId){
            case popular:
                resultPopular = urlTest.buildUrl("popular",paginatePopular);
                ImdbQueryTask popularTask = new ImdbQueryTask();
                if(!isThereInternetConnection()) {
                    popularTask.cancel(true);
                    return super.onOptionsItemSelected(item);
                }
                if(popularTask.getStatus() == AsyncTask.Status.RUNNING){
                    //Don't load another task
                }
                    popularTask.execute(resultPopular);
                return true;

            case R.id.highest_rated:
                resultTopRated = urlTest.buildUrl("top_rated",paginateTopRated);
                ImdbQueryTask topRatedTask = new ImdbQueryTask();
                if(!isThereInternetConnection()) {
                    topRatedTask.cancel(true);
                    return super.onOptionsItemSelected(item);
                }
                if(topRatedTask.getStatus() == AsyncTask.Status.RUNNING){
                    //Don't load another task
                }
                    topRatedTask.execute(resultTopRated);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {

        String imageResource = adapter.getItemPosition(position).getMovieResource().toString();
        String movieTitle = adapter.getItemPosition(position).getMovieTitle().toString();
        String movieOverview = adapter.getItemPosition(position).getMovieOverview().toString();
        String movieRelease = adapter.getItemPosition(position).getMovieRelease().toString();
        String movieRating = adapter.getItemPosition(position).getMovieRating().toString();


        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("ImageExtra",imageResource);
        intent.putExtra("TitleExtra",movieTitle);
        intent.putExtra("OverViewExtra",movieOverview);
        intent.putExtra("ReleaseExtra",movieRelease);
        intent.putExtra("RatingExtra",movieRating);
        startActivity(intent);


    }



    public class ImdbQueryTask extends AsyncTask<URL, Void, ArrayList<Movie>> {


        ArrayList<Movie> movieList = new ArrayList<Movie>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Movie> doInBackground(URL... urls) {
            clickedUrl = urls[0];
            String JSONString = null;
            try {
                JSONString = urlTest.run(clickedUrl.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            movieList = GetMoviesOnline.extractMoviesFromJson(JSONString);

            return movieList;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {

                if(movies.isEmpty()){
                    internetError.setText("There was a problem loading list");
                    internetError.setVisibility(View.VISIBLE);
                }

                adapter.setData(movies);

        }
    }



}
