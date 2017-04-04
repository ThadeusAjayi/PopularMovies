package com.shopspreeng.android.popularmovies1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class PopularMovies extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(menu != null){
            getMenuInflater().inflate(R.menu.movie_list_options, menu);
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.popular:
                Toast.makeText(this,"Popular Clicked",Toast.LENGTH_SHORT)
                        .show();
                return true;
            case R.id.highest_rated:
                Toast.makeText(this,"Highest Rated Clicked",Toast.LENGTH_SHORT)
                        .show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
