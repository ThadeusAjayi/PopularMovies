package com.shopspreeng.android.popularmovies1;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jayson surface on 04/04/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private static int viewHolderCount;

    private int mNumberItems;


    public MoviesAdapter(int moviesTotal){
        mNumberItems = moviesTotal;
        viewHolderCount = 0;
    }

    @Override
    public MoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list,parent,false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(view);




        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MoviesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class MoviesViewHolder extends RecyclerView.ViewHolder{

        ImageView mMoviesImageView;

        TextView mMoviesTextView;

        public MoviesViewHolder(View itemView) {
            super(itemView);

            mMoviesImageView = (ImageView) itemView.findViewById(R.id.movie_poster);

            mMoviesTextView = (TextView) itemView.findViewById(R.id.movie_text);

        }




    }

}
