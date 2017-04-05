package com.shopspreeng.android.popularmovies1;

/**
 * Created by jayson surface on 04/04/2017.
 */

public class Movie {

    private int mMovieResource;

    private String mMovieTitle;


    public Movie(int movieResource, String movieTitle){
        mMovieResource = movieResource;
        mMovieTitle = movieTitle;
    }

    public int getMovieResource(){
        return mMovieResource;
    }

    public String getMovieTitle(){
        return mMovieTitle;
    }

}
