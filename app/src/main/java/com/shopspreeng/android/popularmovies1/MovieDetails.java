package com.shopspreeng.android.popularmovies1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.w3c.dom.Text;

public class MovieDetails extends AppCompatActivity {

    TextView titleText, overviewText, ratingText, releaseText;

    ImageView poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        titleText = (TextView) findViewById(R.id.title);
        overviewText = (TextView) findViewById(R.id.overview);
        ratingText = (TextView) findViewById(R.id.rating);
        releaseText = (TextView) findViewById(R.id.release);

        poster = (ImageView) findViewById(R.id.poster);
        GetMoviesOnline buildImage = new GetMoviesOnline();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent receivedIntent = getIntent();
        if(receivedIntent.hasExtra("ImageExtra") && receivedIntent.hasExtra("TitleExtra") &&
                receivedIntent.hasExtra("OverViewExtra") && receivedIntent.hasExtra("ReleaseExtra")
                && receivedIntent.hasExtra("RatingExtra")){

            String imageUrl = receivedIntent.getStringExtra("ImageExtra");

            String title = receivedIntent.getStringExtra("TitleExtra");

            String overview = receivedIntent.getStringExtra("OverViewExtra");

            String releaseDate = receivedIntent.getStringExtra("ReleaseExtra");

            String rating = receivedIntent.getStringExtra("RatingExtra");

            Picasso.with(this).load(String.valueOf(buildImage.buildImageUrl(imageUrl))).fit().into(poster);

            titleText.append("\t" + title);
            overviewText.append("\n \t" + overview);
            ratingText.append("\t" + rating);
            releaseText.append("\t" + releaseDate);



        }



    }
}
