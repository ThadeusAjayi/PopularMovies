package com.shopspreeng.android.popularmovies1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.R.attr.data;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by jayson surface on 04/04/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    LayoutInflater inflater;

    Context context;

    ArrayList<Movie> movies;

    ItemClickListener mClickListener;


    public MoviesAdapter(Context context, ArrayList<Movie> movie){
        inflater = LayoutInflater.from(context);
        movies = movie;
    }

    public void setData(ArrayList<Movie> movie){
        movies = movie;
        notifyDataSetChanged();
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
        GetMoviesOnline getMovies = new GetMoviesOnline();

        Picasso.with(context).load(String.valueOf(getMovies.buildImageUrl(currentMovie.getMovieResource()))).fit().into(holder.mImageResource);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageResource;


        public MovieViewHolder(View itemView) {
            super(itemView);

            mImageResource = (ImageView) itemView.findViewById(R.id.movie_poster);
            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public Movie getItemPosition(int position){
        return  movies.get(position);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
