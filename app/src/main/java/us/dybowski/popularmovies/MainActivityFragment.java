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

import us.dybowski.popularmovies.data.DataUtilities;
import us.dybowski.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    UriImageAdapter mUriImageAdapter;
    GridView movieGridView;
    String mSortOrder;
    private int mPosition = ListView.INVALID_POSITION;

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

        mUriImageAdapter = new UriImageAdapter(getActivity(), new ArrayList<Movie>(), "http://image.tmdb.org/t/p/w342/");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        movieGridView = (GridView) rootView.findViewById(R.id.gridview);
        movieGridView.setAdapter(mUriImageAdapter);

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                if (true) {
                    Movie m = (Movie) adapterView.getItemAtPosition(position);
                    Log.i("POSITION", Integer.toString(position));
                    Log.i("MOVIEID", m.getMovieId());
                    ((Callback) getActivity())
                            .onItemSelected(Long.parseLong(m.getMovieId()));
                }
                mPosition = position;
            }
        });
        mSortOrder = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default_value));

        refresh(mSortOrder);
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mSortOrder = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default_value));
    }

    @Override
    public void onStart() {
        super.onStart();
        String sortOrder = PreferenceManager.getDefaultSharedPreferences(
                getActivity()).getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default_value));
        if (!sortOrder.equals(mSortOrder)) {
            mSortOrder = sortOrder;
            refresh(mSortOrder);
        }
    }

    private void refresh(String sortOrder) {
        Log.i(LOG_TAG, "sortOrder: " + sortOrder);
        if(sortOrder.equals("favorites")) {
            mUriImageAdapter.clear();
            mUriImageAdapter.addAll(DataUtilities.getFavorites(getActivity()));
        }
        else {
            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this);
            fetchMoviesTask.execute(sortOrder);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String sortOrder = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default_value));
        outState.putString("ORDER", sortOrder);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        void onItemSelected(long l);
    }
}

