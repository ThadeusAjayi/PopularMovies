package com.shopspreeng.android.popularmovies1;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by jayson surface on 04/04/2017.
 */

public class Movie extends ArrayList<Parcelable> implements Parcelable {

    private String mMovieResource;

    private String mMovieTitle;

    private String mOverview, mRelease, mRating;

    private String mMovieId, mReview, mTrailer, mAuthor;



    public Movie(String movieResource, String movieTitle, String overview, String release, String rating, String
                 movieId, String review, String trailer, String author){
        mMovieResource = movieResource;
        mMovieTitle = movieTitle;
        mOverview = overview;
        mRelease = release;
        mRating = rating;
        mMovieId = movieId;
        mReview = review;
        mTrailer = trailer;
        mAuthor = author;
    }


    protected Movie(Parcel in) {
        mMovieResource = in.readString();
        mMovieTitle = in.readString();
        mOverview = in.readString();
        mRelease = in.readString();
        mRating = in.readString();
        mMovieId = in.readString();
        mReview = in.readString();
        mTrailer = in.readString();
        mAuthor = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public String getMovieId(){
        return mMovieId;
    }

    public String getReview(){
        return mReview;
    }

    public String getTrailer(){
        return mTrailer;
    }

    public String getAuthor(){
        return mAuthor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mMovieResource);
        parcel.writeString(mMovieTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mRelease);
        parcel.writeString(mRating);
        parcel.writeString(mMovieId);
        parcel.writeString(mReview);
        parcel.writeString(mTrailer);
        parcel.writeString(mAuthor);
    }
}
