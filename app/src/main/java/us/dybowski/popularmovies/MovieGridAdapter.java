package us.dybowski.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * {@link MovieGridAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class MovieGridAdapter extends CursorAdapter {


    public MovieGridAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return new ImageView(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

       //Get the poster path
        String posterPath = cursor.getString(MainActivityFragment.COL_POSTER_PATH);
        Uri imageUri = Uri.parse(BuildConfig.THE_MOVIE_DB_IMAGE_BASE_PATH).buildUpon().appendEncodedPath(posterPath).build();
        Picasso.with(context).load(imageUri).into((ImageView)view);

    }

}