package us.dybowski.popularmovies;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by DD8312 on 8/22/2016.
 */
public class UriImageAdapter extends ArrayAdapter<String> {

    public static final String BASE_PATH = "http://image.tmdb.org/t/p/w185/";
    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context    The current context. Used to inflate the layout file.
     * @param imagePaths A List of strings that when combined with the base path will
     *                   resolve to a uri to an image
     */
    public UriImageAdapter(Activity context, List<String> imagePaths) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.

        super(context, 0, imagePaths);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}