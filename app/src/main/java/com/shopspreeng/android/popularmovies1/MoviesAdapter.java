package com.shopspreeng.android.popularmovies1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.data;

/**
 * Created by jayson surface on 04/04/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    LayoutInflater inflater;


    ArrayList<Movie> movies = new ArrayList<Movie>();


    public MoviesAdapter(Context context, ArrayList<Movie> movie){
        inflater = LayoutInflater.from(context);
        movies = movie;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.movie_list,parent,false);

        MovieViewHolder holder = new MovieViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Movie currentMovie = movies.get(position);

        holder.mImageResource.setImageResource(currentMovie.getMovieResource());
        holder.mMoviewTitle.setText(currentMovie.getMovieTitle());

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageResource;

        TextView mMoviewTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);

            mImageResource = (ImageView) itemView.findViewById(R.id.movie_poster);

            mMoviewTitle = (TextView) itemView.findViewById(R.id.movie_text);
        }

    }

}
