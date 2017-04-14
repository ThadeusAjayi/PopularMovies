package com.shopspreeng.android.popularmovies1;

/**
 * Created by jayson surface on 04/04/2017.
 */

public class Movie {

    private String mMovieResource;

    private String mMovieTitle;

    private String mOverview, mRelease, mRating;


    public Movie(String movieResource, String movieTitle, String overview, String release, String rating){
        mMovieResource = movieResource;
        mMovieTitle = movieTitle;
        mOverview = overview;
        mRelease = release;
        mRating = rating;
    }

    public String getMovieResource(){
        return mMovieResource;
    }

    public String getMovieTitle(){
        return mMovieTitle;
    }

    public String getMovieOverview(){
        return mOverview;
    }

    public String getMovieRelease(){
        return mRelease;
    }

    public String getMovieRating(){
        return mRating;
    }

}
