package com.shopspreeng.android.popularmovies1;

import android.content.ContentValues;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import com.shopspreeng.android.popularmovies1.data.MoviesContract;

/**
 * Created by jayson surface on 05/05/2017.
 */

public class TestUtils {

    public static void insertFakeData(SQLiteDatabase db){
        if(db == null){
            return;
        }
        //create a list of fake guests
        List<ContentValues> list = new ArrayList<ContentValues>();

        ContentValues cv = new ContentValues();
        cv.put(MoviesContract.MoviesEntry.COLUMN_IMAGE, "BadlandImage");
        cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, "Into the Badlands");
        cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING, "6.5");
        cv.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, "This movie is very bad, since the title is badland");
        cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, "12-12-2017");
        cv.put(MoviesContract.MoviesEntry.COLUMN_TRAILER, "BadlandImage");
        cv.put(MoviesContract.MoviesEntry.COLUMN_REVIEW, "this is the review");
        list.add(cv);

        cv = new ContentValues();
        cv.put(MoviesContract.MoviesEntry.COLUMN_IMAGE, "BadlandImage");
        cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, "Into the Badlands");
        cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING, "6.5");
        cv.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, "This movie is very bad, since the title is badland");
        cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, "12-12-2017");
        cv.put(MoviesContract.MoviesEntry.COLUMN_TRAILER, "BadlandImage");
        cv.put(MoviesContract.MoviesEntry.COLUMN_REVIEW, "this is the review");
        list.add(cv);

        cv = new ContentValues();
        cv.put(MoviesContract.MoviesEntry.COLUMN_IMAGE, "BadlandImage");
        cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_TITLE, "Into the Badlands");
        cv.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_RATING, "6.5");
        cv.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, "This movie is very bad, since the title is badland");
        cv.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, "12-12-2017");
        cv.put(MoviesContract.MoviesEntry.COLUMN_TRAILER, "BadlandImage");
        cv.put(MoviesContract.MoviesEntry.COLUMN_REVIEW, "this is the review");
        list.add(cv);

        //insert all guests in one transaction
        try
        {
            db.beginTransaction();
            //clear the table first
            db.delete (MoviesContract.MoviesEntry.TABLE_NAME,null,null);
            //go through the list and add one by one
            for(ContentValues c:list){
                db.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, c);
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e) {
            //too bad :(
        }
        finally
        {
            db.endTransaction();
        }

    }
}
