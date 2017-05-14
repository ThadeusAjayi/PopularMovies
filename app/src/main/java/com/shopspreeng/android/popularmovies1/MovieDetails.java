package com.shopspreeng.android.popularmovies1;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shopspreeng.android.popularmovies1.data.MoviesContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;

public class MovieDetails extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String[]>{

    TextView titleText, overviewText, ratingText, releaseText, movieIdText, reviewText;

    ImageView poster;

    String movieUrl;

    CollapsingToolbarLayout mToolbar;

    ImageButton favouriteBtn;

    private ProgressBar progressBar;

    String movieId;

    private String title, overview, releaseDate, rating, movieDbId, imagePath, trailerUrl, review;

    Boolean selectedfav;

    GetMoviesOnline gmol = new GetMoviesOnline();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        titleText = (TextView) findViewById(R.id.title);
        overviewText = (TextView) findViewById(R.id.overview);
        ratingText = (TextView) findViewById(R.id.rating);
        releaseText = (TextView) findViewById(R.id.release);
        movieIdText = (TextView) findViewById(R.id.movie_id);
        reviewText = (TextView) findViewById(R.id.review);

        poster = (ImageView) findViewById(R.id.poster);
        favouriteBtn = (ImageButton) findViewById(R.id.fav_btn);


        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (selectedfav == false) {
                        //TODO if works, toggle image button color here
                    ContentValues contentValues = new ContentValues();

                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_IMAGE, imagePath);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, title);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, overview);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, releaseDate);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_REVIEW, review);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING, rating);
                    contentValues.put(MoviesContract.MoviesEntry.COLUMN_TRAILER, trailerUrl);

                    Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);

                    if (uri != null) {
                        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                        getContentResolver().notifyChange(uri,null);
                    }
                }else{
                    Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
                    uri = uri.buildUpon().appendPath(movieDbId).build();
                    Log.v("MovieViewId", String.valueOf(uri));
                    if(getContentResolver().delete(uri,null,null)>0)
                        Toast.makeText(getBaseContext(), uri.toString() + "Deleted", Toast.LENGTH_SHORT).show();
                        getContentResolver().notifyChange(uri,null);

                }
            }
        });

        movieIdText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("onclick",trailerUrl);
                Intent openYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl.toString()));
                startActivity(openYoutube);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        if(intent.hasExtra("toSend") && intent.hasExtra("imagePath")) {

            selectedfav = false;

            MoviesAdapter adapter = new MoviesAdapter();

            Movie extra = intent.getParcelableExtra("toSend");

            imagePath = intent.getStringExtra("imagePath");

            title = adapter.getMovie(extra).getMovieTitle();

            overview = adapter.getMovie(extra).getMovieOverview();

            releaseDate = adapter.getMovie(extra).getMovieRelease();

            rating = adapter.getMovie(extra).getMovieRating();

            movieId = adapter.getMovie(extra).getMovieId();

            loadImageFromStorage(imagePath, poster);

            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(22, null, this);

            mToolbar.setTitle(title);
            mToolbar.setExpandedTitleTypeface(Typeface.MONOSPACE);

            movieUrl = movieIdText.getText().toString();
            Log.v("MovieURL",movieUrl);
        }

        if(intent.hasExtra("toSend") && intent.hasExtra("movieDbId")) {

            selectedfav = true;

            MoviesAdapter adapter = new MoviesAdapter();

            Movie extra = intent.getParcelableExtra("toSend");

            movieDbId = intent.getStringExtra("movieDbId");

            trailerUrl = intent.getStringExtra("trailer");

            imagePath = adapter.getMovie(extra).getMovieResource();

            title = adapter.getMovie(extra).getMovieTitle();

            overview = adapter.getMovie(extra).getMovieOverview();

            releaseDate = adapter.getMovie(extra).getMovieRelease();

            rating = adapter.getMovie(extra).getMovieRating();

            //Set review text
            review = adapter.getMovie(extra).getMovieId();

            loadImageFromStorage(imagePath, poster);


            setTvContents();

            mToolbar.setTitle(title);
            mToolbar.setExpandedTitleTypeface(Typeface.MONOSPACE);

            movieUrl = movieIdText.getText().toString();
            Log.v("MovieURL",movieUrl);
        }




        }

    public void setTvContents(){
        titleText.setText(title);
        overviewText.setText(overview);
        ratingText.setText(rating);
        releaseText.setText(releaseDate);
        movieIdText.setText(trailerUrl);
        reviewText.setText(review);
    }

    public void loadImageFromStorage(String path, ImageView posterView){

        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            posterView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public Loader<String[]> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.AsyncTaskLoader<String[]>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public String[] loadInBackground() {
                try {
                    //TODO make sure to get the movie trailerUrl key to show up
                    String[] trailerNreview = {new GetMoviesOnline().buildTrailerUrl(new GetMoviesOnline().extractTrailerKey
                            (new GetMoviesOnline().run(new GetMoviesOnline().buildMoviesEndpoint(movieId).toString()))).toString(),

                            new GetMoviesOnline().run(new GetMoviesOnline().buildReviewEndpoint(movieId).toString())};
                    Log.v("reviews endpoint",new GetMoviesOnline().buildReviewEndpoint(movieId).toString());

                    return trailerNreview;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String[]> loader, String[] data) {
        progressBar.setVisibility(View.INVISIBLE);
        trailerUrl = data[0];
        String reviewHere[] = new String[2];
        try {
            reviewHere = GetMoviesOnline.extractReviewDetails(data[1]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(reviewHere[1] == null || reviewHere[0] == null){
            review = "Author:" + "\n\n" + "Review:";
        } else {
            review = reviewHere[1] + "\n\n" + reviewHere[0];
        }

        setTvContents();
    }

    @Override
    public void onLoaderReset(Loader<String[]> loader) {

    }
}
