package com.shopspreeng.android.popularmovies1;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shopspreeng.android.popularmovies1.data.MoviesContract;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.string.no;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

/**
 * Created by jayson surface on 04/04/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>{

    LayoutInflater inflater;

    Context context;

    ArrayList<Movie> movies;

    ItemClickListener mClickListener;

    OnLoadMoreListener loadMoreListener;

    public MoviesAdapter(){}

    boolean isLoading = false, isMoreDataAvailable = true;


    public MoviesAdapter(Context context, ArrayList<Movie> movie){
        inflater = LayoutInflater.from(context);
        movies = movie;
    }

    public void setData(ArrayList<Movie> movie){
        movies = movie;
        notifyDataSetChanged();
        isLoading = false;
    }

    public void addData(ArrayList<Movie> movie){
        movies.addAll(movie);
        notifyDataSetChanged();
        isLoading = false;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.movie_list,parent,false);

        MovieViewHolder holder = new MovieViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }
        Movie currentMovie = movies.get(position);
        GetMoviesOnline getMovies = new GetMoviesOnline();

        if(!currentMovie.getMovieResource().contains("/com.shopspreeng.android.popularmovies1/")) {
            Picasso.with(context).load(String.valueOf(getMovies.buildImageUrl(currentMovie.getMovieResource()))).fit().into(holder.mImageResource);
        } else {
            Picasso.with(context).load(new File(currentMovie.getMovieResource())).fit().into(holder.mImageResource);
            //loadImageFromStorage(currentMovie.getMovieResource(),holder.mImageResource);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
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

    public Movie getMovie(Movie movie){
        return movie;
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

    interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }


}
