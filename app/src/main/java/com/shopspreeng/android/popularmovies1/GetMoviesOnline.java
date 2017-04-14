package com.shopspreeng.android.popularmovies1;

import android.net.Uri;
import android.util.Log;

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

    final static String API_KEY = "[API GOES HERE]";

    final static String PAGE = "page";




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


                Movie movieList = new Movie(moviePoster,movieTitle,movieOverview,movieReleaseDate,movieRating);
                movieArrayList.add(movieList);

            }
        } catch (JSONException e) {
            Log.e("JSON extraction", "Problem parsing the earthquake JSON results", e);
        }

        return movieArrayList;
    }



}
