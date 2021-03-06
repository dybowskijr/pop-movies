package us.dybowski.popularmovies;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by DD8312 on 8/22/2016.
 */
public class UriImageAdapter extends ArrayAdapter<Movie> {

    private static String LOG_TAG = UriImageAdapter.class.getSimpleName();

    private String mBasePath;
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context    The current context. Used to inflate the layout file.
     * @param imagePaths A List of strings that when combined with the base path will
     *                   resolve to a uri to an image
     */
    public UriImageAdapter(Activity context, List<Movie> imagePaths, String basePath) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.

        super(context, 0, imagePaths);
        mBasePath = basePath;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = new ImageView(getContext());
        }
        Movie m = getItem(position);
       // ((ImageView)convertView).setImageResource(R.drawable.ic_action_name);
        Uri imageUri = Uri.parse(mBasePath).buildUpon().appendEncodedPath(m.getPosterPath()).build();
        Log.d(LOG_TAG, imageUri.toString());
        Picasso.with(getContext()).load(imageUri).into((ImageView)convertView);

        return convertView;
    }
}