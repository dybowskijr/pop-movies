package us.dybowski.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import us.dybowski.popularmovies.Movie;

public class DataUtilities {
    public static long insertFavorite(Movie m, Context c) {
        MovieDbHelper movieDbHelper = new MovieDbHelper(c);
        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, m.getMovieId());
        cv.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, true);
        return movieDbHelper.getWritableDatabase().insert(MovieContract.MovieEntry.TABLE_NAME, "", cv);

    }

    public static int deleteFavorite(String movie, Context c) {
        String[] args = new String[1];
        args[0] = movie;
        MovieDbHelper movieDbHelper = new MovieDbHelper(c);
        return movieDbHelper.getWritableDatabase().delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "= ?", args);
    }
    public static boolean isFavorite(String movieId, Context c) {
        boolean retval = false;
        String[] columns = new String[1];
        String[] selectionArgs = new String[1];
        columns[0] = MovieContract.MovieEntry.COLUMN_MOVIE_ID;
        selectionArgs[0] = movieId;
        MovieDbHelper movieDbHelper = new MovieDbHelper(c);
        Cursor cursor = movieDbHelper.getReadableDatabase()
                .query(MovieContract.MovieEntry.TABLE_NAME, columns, MovieContract.MovieEntry.COLUMN_MOVIE_ID + "= ?", selectionArgs, null, null, null);
        retval = cursor.moveToFirst();
        cursor.close();
        return retval;
    }
}
