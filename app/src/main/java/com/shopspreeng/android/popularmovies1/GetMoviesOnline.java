package com.shopspreeng.android.popularmovies1;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by jayson surface on 05/04/2017.
 */

public class GetMoviesOnline {

    final static String IMDB_BASE_URL = "http://api.themoviedb.org/3/movie/";

    final static String PARAM_API = "api_key";

    final static String VIDEO_KEY = "v";

    //TODO as recommended, keep api key variable in gradle
    final static String API_KEY = "[API KEY GOES HERE]";

    final static String PAGE = "page";

    final static String BASE_YOUTUBE_URL = "https://www.youtube.com/watch";


    public URL buildUrl(String sortOption, int pageNumber){

        Uri buildUri = Uri.parse(IMDB_BASE_URL).buildUpon()
                .appendPath(sortOption)
                .appendQueryParameter(PARAM_API,API_KEY)
                .appendQueryParameter(PAGE, String.valueOf(pageNumber))
                .build();

        URL url = null;

        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        return url;
    }

    public URL buildImageUrl(String imagePath){

        Uri buildUri = Uri.parse("http://image.tmdb.org/t/p/w185/").buildUpon()
                .appendEncodedPath(imagePath)
                .build();

        URL url = null;

        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public URL buildMoviesEndpoint (String movieId){

        Uri buildUri = Uri.parse(IMDB_BASE_URL).buildUpon()
                .appendEncodedPath(movieId+"/videos")
                .appendQueryParameter(PARAM_API,API_KEY)
                .build();

        URL moviesEndpoint = null;
        try {
            moviesEndpoint = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return moviesEndpoint;
    }

    public URL buildReviewEndpoint(String movieId){

        Uri buildUri = Uri.parse(IMDB_BASE_URL).buildUpon()
                .appendEncodedPath(movieId+"/reviews")
                .appendQueryParameter(PARAM_API,API_KEY)
                .build();

        URL reviewEndpoint = null;
        try {
            reviewEndpoint = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.print(reviewEndpoint.toString());
        return reviewEndpoint;
    }


    OkHttpClient connect = new OkHttpClient();

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = connect.newCall(request).execute();

        String result = response.body().string();

        return result;
    }

    public static ArrayList<Movie> extractMoviesFromJson(String movieJson) {

        ArrayList<Movie> movieArrayList = new ArrayList<>();

        try {

            JSONObject JSONroot = new JSONObject(movieJson);

            JSONArray movieResult = JSONroot.getJSONArray("results");

            for (int i = 0; i < movieResult.length(); i++) {

                JSONObject movieObject = movieResult.getJSONObject(i);

                String movieTitle = movieObject.getString("original_title");

                String moviePoster = movieObject.getString("poster_path");

                String movieOverview = movieObject.getString("overview");

                String movieReleaseDate = movieObject.getString("release_date");

                String movieRating = movieObject.getString("vote_average");

                String movieId = movieObject.getString("id");


                Movie movieList = new Movie(moviePoster,movieTitle,movieOverview,movieReleaseDate,movieRating,movieId);
                movieArrayList.add(movieList);

            }
        } catch (JSONException e) {
            Log.e("JSON extraction", "Problem parsing the earthquake JSON results", e);
        }

        return movieArrayList;
    }

    public static String extractTrailerKey(String movieJson){
        String trailerKey = "";

        try {
            JSONObject jsonObject = new JSONObject(movieJson);
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            JSONObject keyObject = jsonArray.getJSONObject(0);
            trailerKey = keyObject.getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailerKey;
    }

    public static String[] extractReviewDetails(String reviewJson) throws JSONException {
        String [] reviewDetails = new String[2];

        JSONObject rootJson = new JSONObject(reviewJson);
        JSONArray resultArray = rootJson.getJSONArray("results");

            JSONObject resultObject = resultArray.getJSONObject(1);
            String author = resultObject.getString("author");
            String content = resultObject.getString("content");

        reviewDetails[0] = author;
        reviewDetails[1] = content;
        Log.v("author","author");
        Log.v("content","content");

        return reviewDetails;

    }

    public static URL buildTrailerUrl(String trailerKey){
        Uri buildUri = Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                .appendQueryParameter(VIDEO_KEY,trailerKey)
                .build();

        URL trailerEndpoint = null;
        try {
            trailerEndpoint = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.print(trailerEndpoint.toString());
        return trailerEndpoint;
    }

    public URL stringToUrl(String stringURl){
        URL url = null;

        try {
            url = new URL(stringURl);
        } catch (MalformedURLException e) {
            e.printStackTrace();

        }
        return url;
    }




}
