package us.dybowski.popularmovies;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import us.dybowski.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    UriImageAdapter mUriImageAdapter;
    GridView movieGridView;
    private int mPosition = ListView.INVALID_POSITION;

    private static List<Movie> dummyList = new ArrayList<>();

    public MainActivityFragment() {
    }

    private static final String[] MOVIEGRID_COLUMNS = {

            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_RUNTIME,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE
    };

    static final int COL_ID = 0;
    static final int COL_MOVIE_ID = 0;
    static final int COL_OVERVIEW = 0;
    static final int COL_TITLE = 0;
    static final int COL_RELEASE_DATE = 0;
    static final int COL_RUNTIME = 0;
    static final int COL_VOTE_AVERAGE = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mUriImageAdapter = new UriImageAdapter(getActivity(), dummyList, "http://image.tmdb.org/t/p/w342/");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieGridView = (GridView)rootView.findViewById(R.id.gridview);
        movieGridView.setAdapter(mUriImageAdapter);

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (true) {
                    Movie m = (Movie)adapterView.getItemAtPosition(position);
                    Log.i("POSITION", Integer.toString(position));
                    Log.i("MOVIEID", m.getMovieId());
                    ((Callback) getActivity())
                            .onItemSelected(Long.parseLong(m.getMovieId()));
                }
                mPosition = position;
            }
        });

        //get movie list using preference (highest_rated or most_popular
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default_value));
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this);
        fetchMoviesTask.execute(sortOrder);
        return rootView;
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        // This is called when a new Loader needs to be created.  This
//        // fragment only uses one loader, so we don't care about checking the id.
//
//
//        // Sort order:  //// TODO: 9/18/2016 get user prefs
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        String sortOrder = prefs.getString(
//                getActivity().getString(R.string.pref_sort_order_key),
//                getActivity().getString(R.string.pref_sort_order_default_value)) + " ASC";
//
//        Uri movieUri = MovieContract.MovieEntry.buildMoviesUri();
//
//        return new CursorLoader(getActivity(),
//                movieUri,
//                MOVIEGRID_COLUMNS,
//                null,
//                null,
//                sortOrder);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }

    /**
     * A callback interface that all activities containing this fragment must
     * implement. This mechanism allows activities to be notified of item
     * selections.
     */
    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(long l);
    }
}

