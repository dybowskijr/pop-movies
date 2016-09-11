package us.dybowski.popularmovies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    UriImageAdapter mUriImageAdapter;

    private static List<Movie> dummyList = new ArrayList<Movie>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mUriImageAdapter = new UriImageAdapter(getActivity(), dummyList, "http://image.tmdb.org/t/p/w342/");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        GridView movieGridView = (GridView)rootView.findViewById(R.id.gridview);
        movieGridView.setAdapter(mUriImageAdapter);


        //get movie list using preference (highest_rated or most_popular
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_default_value));
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(sortOrder);
        return rootView;
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie> > {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private ArrayList<Movie> getMoviesFromJson(String forecastJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String MDB_RESULTS = "results";
            final String MDB_POSTER_PATH = "poster_path";
            final String MDB_ID = "id";

            ArrayList<Movie> movieList = new ArrayList<>();

            JSONObject moviesJson = new JSONObject(forecastJsonStr);
            JSONArray movieArray = moviesJson.getJSONArray(MDB_RESULTS);

            for(int i = 0; i < movieArray.length(); i++) {

                JSONObject movieJson = movieArray.getJSONObject(i);
                Log.i(LOG_TAG, movieJson.getString("title") );
                movieList.add(new Movie(movieJson.getString(MDB_ID), movieJson.getString(MDB_POSTER_PATH)));
            }
            return movieList;
        }

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonString = null;

            String format = "json";


            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MDB_BASE_URL = "https://api.themoviedb.org/3/movie/popular?api_key=6d7b0feed96048f22022fc5a08023e75";
                      //  "https://api.themoviedb.org/3/movie/";

                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MDB_BASE_URL).buildUpon()
                       // .appendPath(params[0])
                      //  .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.i(LOG_TAG, url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMoviesFromJson(moviesJsonString);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        protected void onPostExecute(ArrayList<Movie> result) {
            if (result != null) {
                mUriImageAdapter.clear();

                    mUriImageAdapter.addAll(result);

            }
        }
    }
}

