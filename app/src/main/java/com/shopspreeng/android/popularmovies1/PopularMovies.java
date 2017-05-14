package com.shopspreeng.android.popularmovies1;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.shopspreeng.android.popularmovies1.data.MoviesContract;

import static android.R.attr.id;
import static com.shopspreeng.android.popularmovies1.R.id.popular;


public class PopularMovies extends AppCompatActivity implements  MoviesAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<ArrayList<Movie>>{


    ArrayList<Movie> extractedMovie = new ArrayList<>();

    private MoviesAdapter adapter;
    private RecyclerView recyclerMovielist;
    private TextView internetError;

    GetMoviesOnline urlTest = new GetMoviesOnline();

    URL resultPopular, resultTopRated, emptyUrl;

    private int paginatePopular = 1;
    private int paginateTopRated = 1;

    private static final int START_MOVIE_LOADER_INT= 22;

    ProgressBar progressBar;

    private static final String MOVIE_URL_STRING = "";

    Bundle queryBundle = new Bundle();

    private static final String SAVE_ROTATION = "savedMovies";

    private URL runningUrl;

    public boolean selectedFav;

    String id = "";

    String trailer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        internetError = (TextView) findViewById(R.id.no_internet);
        recyclerMovielist = (RecyclerView) findViewById(R.id.movie_list);

        recyclerMovielist.setHasFixedSize(true);
        adapter = new MoviesAdapter(this,new ArrayList<Movie>());


       detemineScreenSizeAndLayout();



        if(savedInstanceState == null) {
            onCreateLoad();
        }else {
            //TODO save some status for value to show its db files to load on start
                extractedMovie = savedInstanceState.getParcelableArrayList(SAVE_ROTATION);
                String savedRunningUrl = savedInstanceState.getString("runningUrl");
                setRunningUrl(urlTest.stringToUrl(savedRunningUrl));
                adapter.setData(extractedMovie);

        }

        recyclerMovielist.setAdapter(adapter);

        adapter.setClickListener(this);

        adapter.setLoadMoreListener(new MoviesAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                loadMore();
            }
        });


    }

    private void detemineScreenSizeAndLayout() {
        //Determine screen size
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            //Handle number of items shown on the grid on orientation change
            // 7.0 Tablet
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerMovielist.setLayoutManager(new GridLayoutManager(this, 3));
            }
            else{
                recyclerMovielist.setLayoutManager(new GridLayoutManager(this, 4));
            }
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            // 4.0 & 5.0 phone
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerMovielist.setLayoutManager(new GridLayoutManager(this, 2));
            }
            else{
                recyclerMovielist.setLayoutManager(new GridLayoutManager(this, 4));
            }
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            //Below 4.0 inches phone
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerMovielist.setLayoutManager(new GridLayoutManager(this, 1));
            }
            else{
                recyclerMovielist.setLayoutManager(new GridLayoutManager(this, 2));
            }
        }
        else {
            // Over 7 inches devices
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerMovielist.setLayoutManager(new GridLayoutManager(this, 3));
            }
            else{
                recyclerMovielist.setLayoutManager(new GridLayoutManager(this, 4));
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVE_ROTATION,extractedMovie);
        outState.putString("runningUrl",runningUrl.toString());
        outState.putBoolean("fav",selectedFav);
    }

    public void loadMore(){
//TODO fix rotation load more
        if (runningUrl.equals(resultPopular)){
            paginatePopular++;
            resultPopular = urlTest.buildUrl("popular", paginatePopular);
            Log.v("REsultPopular", resultPopular.toString());

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Movie> movieLoader = loaderManager.getLoader(START_MOVIE_LOADER_INT);

            queryBundle.putString(MOVIE_URL_STRING,resultPopular.toString());

            if(movieLoader == null){
                loaderManager.initLoader(START_MOVIE_LOADER_INT,queryBundle,this);
            }else{
                loaderManager.restartLoader(START_MOVIE_LOADER_INT,queryBundle,this);
            }

            setRunningUrl(resultPopular);
        }

        if (runningUrl.equals(resultTopRated)){
            paginateTopRated++;
            resultTopRated = urlTest.buildUrl("top_rated",paginateTopRated);
            Log.v("ResultTopRated", resultTopRated.toString());


            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Movie> movieLoader = loaderManager.getLoader(START_MOVIE_LOADER_INT);

            queryBundle.putString(MOVIE_URL_STRING,resultTopRated.toString());

            if(movieLoader == null){
                loaderManager.initLoader(START_MOVIE_LOADER_INT,queryBundle,this);
            }else{
                loaderManager.restartLoader(START_MOVIE_LOADER_INT,queryBundle,this);
            }

            setRunningUrl(resultTopRated);
        }
    }


    public void onCreateLoad(){
        boolean internetState = checkInternetErrorViewToggle();

        if(internetState == true) {
            resultPopular = urlTest.buildUrl("popular", paginatePopular);
            Log.v("firstUrl",resultPopular.toString());
            setRunningUrl(resultPopular);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Movie> movieLoader = loaderManager.getLoader(START_MOVIE_LOADER_INT);


            Bundle queryBundle = new Bundle();
            queryBundle.putString(MOVIE_URL_STRING, resultPopular.toString());

            if (movieLoader == null) {
                loaderManager.initLoader(START_MOVIE_LOADER_INT, queryBundle, this);
            } else {
                loaderManager.restartLoader(START_MOVIE_LOADER_INT, queryBundle, this);
            }
        } else {

            setErrorMsg("internetError");
        }

    }

    public boolean checkInternetErrorViewToggle(){
        return isThereInternetConnection();
    }


    protected boolean isThereInternetConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
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

        boolean internetState = checkInternetErrorViewToggle();

        int itemId = item.getItemId();
        switch (itemId){
            case popular:
                selectedFav = false;
                paginatePopular = 1;
                resultPopular = urlTest.buildUrl("popular",paginatePopular);

                if(internetState == false) {
                    setErrorMsg("popular");
                    return super.onOptionsItemSelected(item);
                }
                LoaderManager loaderManager = getSupportLoaderManager();
                Loader<Movie> movieLoader = loaderManager.getLoader(START_MOVIE_LOADER_INT);

                adapter.setData(new ArrayList<Movie>());

                queryBundle.putString(MOVIE_URL_STRING,resultPopular.toString());

                if(movieLoader == null){
                    loaderManager.initLoader(START_MOVIE_LOADER_INT,queryBundle,this);
                }else{
                    loaderManager.restartLoader(START_MOVIE_LOADER_INT,queryBundle,this);
                }
                setRunningUrl(resultPopular);
                

                return true;

            case R.id.highest_rated:
                selectedFav =false;
                paginateTopRated = 1;
                resultTopRated = urlTest.buildUrl("top_rated",paginateTopRated);

                if(internetState == false) {
                    setErrorMsg("topRated");
                    return super.onOptionsItemSelected(item);
                }

                loaderManager = getSupportLoaderManager();
                movieLoader = loaderManager.getLoader(START_MOVIE_LOADER_INT);

                adapter.setData(new ArrayList<Movie>());

                queryBundle.putString(MOVIE_URL_STRING,resultTopRated.toString());

                if(movieLoader == null){
                    loaderManager.initLoader(START_MOVIE_LOADER_INT,queryBundle,this);
                }else{
                    loaderManager.restartLoader(START_MOVIE_LOADER_INT,queryBundle,this);
                }

                setRunningUrl(resultTopRated);

                return true;

            case R.id.favourite:

                selectedFav = true;

                adapter.setData(new ArrayList<Movie>());

                QueryAsyncTask queryAsyncTask = new QueryAsyncTask();
                queryAsyncTask.execute();

                emptyUrl = urlTest.stringToUrl("http://www.shopspreeng.com");
                setRunningUrl(emptyUrl);

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void setErrorMsg(String stateToCheck){
        if(stateToCheck == "favorite")
            internetError.setText("No favorites movies");
            internetError.setVisibility(View.VISIBLE);

        if(stateToCheck == "popular" || stateToCheck == "topRated" || stateToCheck == "internetError")
            internetError.setText("No Internet connection");
            internetError.setVisibility(View.VISIBLE);

        if(stateToCheck == "loadError")
            internetError.setText("There was an error loading list");
            internetError.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, MovieDetails.class);

        if (selectedFav == false) {
            Movie toSend = adapter.getItemPosition(position);
            intent.putParcelableArrayListExtra("toSend",toSend);
            String movieId = adapter.getItemPosition(position).getMovieId();
            String imagepath = saveToInternalStorage(view, movieId);
            intent.putExtra("imagePath", imagepath);
        } else {
            Movie toSend = adapter.getItemPosition(position);
            intent.putParcelableArrayListExtra("toSend",toSend);
            intent.putExtra("movieDbId",id);
            intent.putExtra("trailer",trailer);
        }


        startActivity(intent);

    }

    public void setRunningUrl(URL newSelection){
        runningUrl = newSelection;
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie> cachedMovies = new ArrayList<>();
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(args == null){
                    return;
                }
                if(cachedMovies == null || cachedMovies.isEmpty()) {
                    internetError.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                    Log.v("LoadStarted", "LoadStarted");
                }else{
                    deliverResult(cachedMovies);
                }

            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                String url = args.getString(MOVIE_URL_STRING);
                if(url == null || url.isEmpty()){
                    return null;
                }
                try {
                    ArrayList<Movie> jsonExtractedList = GetMoviesOnline.extractMoviesFromJson(urlTest.run(url));
                    if(jsonExtractedList != null)
                    return jsonExtractedList;
                    if(jsonExtractedList == null || jsonExtractedList.isEmpty())
                        return null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(ArrayList<Movie> data) {
                cachedMovies = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        progressBar.setVisibility(View.INVISIBLE);
        extractedMovie = data;

        if(data == null){
            setErrorMsg("loadError");
        }else if(data.size() > 0){
                adapter.addData(extractedMovie);
                }

        else {

            adapter.setMoreDataAvailable(false);
            }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }


    private class QueryAsyncTask extends android.os.AsyncTask<Void,Void, Cursor>{
        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);

            if(!cursor.moveToNext()) {
                setErrorMsg("favorite");
                return ;
            }


            ArrayList<Movie> dbMovies = new ArrayList<>();

            String image = "", title = "", rating = "", overview = "", release = "", review = "";


            for(int i=0; i < cursor.getCount(); i++) {
                if (cursor.moveToNext()){

                    id = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry._ID));

                    image = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IMAGE));

                    title = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE));

                    rating = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING));

                    overview = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW));

                    release = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE));

                    review = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_REVIEW));

                    trailer  = cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TRAILER));

                    Log.v("CursorVals", review + " " + title);
                }
                dbMovies.add(new Movie(image,title,overview,release,rating,review));
            }
            cursor.close();
            adapter.setData(dbMovies);

            adapter.setMoreDataAvailable(false);

        }

        @Override
        protected Cursor doInBackground(Void... voids) {

            ContentResolver resolver = getContentResolver();
            try {
                return resolver.query(MoviesContract.MoviesEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        MoviesContract.MoviesEntry._ID);
            } catch(Exception e){
                Log.e("PopularMovies1", "Failed to ascynchronously load data.");
                e.printStackTrace();
                return null;
            }

        }
    }

    private String saveToInternalStorage(View imageView, String movieId){

        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = imageView.getDrawingCache();

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

        String fileName = movieId + ".jpg";
        // Create imageDir
        File mypath=new File(directory, fileName);

        if (mypath.exists ()) mypath.delete ();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }

    public void loadImageFromStorage(String path, ImageView posterView){

        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            posterView.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

}
