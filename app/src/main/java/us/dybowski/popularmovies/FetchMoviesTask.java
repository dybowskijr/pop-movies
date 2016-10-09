package us.dybowski.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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


public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private MainActivityFragment mMainActivityFragment;

    public FetchMoviesTask(MainActivityFragment mainActivityFragment) {
       mMainActivityFragment = mainActivityFragment;
    }


    private ArrayList<Movie> getMoviesFromJson(String forecastJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULTS = "results";
        final String MDB_POSTER_PATH = "poster_path";
        final String MDB_MOVIE_ID = "id";


        ArrayList<Movie> movieList = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(forecastJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray(MDB_RESULTS);

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject movieJson = movieArray.getJSONObject(i);
            Log.i(LOG_TAG, movieJson.getString("title"));
            movieList.add(new Movie(movieJson.getString(MDB_MOVIE_ID), movieJson.getString(MDB_POSTER_PATH)));
        }
        return movieList;
    }

    /**
     *
     * @param params first parameter will be the sort_by value (vote_average.desc, popularity.desc)
     * @return
     */
    @Override
    protected ArrayList<Movie> doInBackground(String... params) {

        final String API_KEY_VALUE = BuildConfig.THE_MOVIE_DB_API_KEY;
        final String API_KEY_KEY = "api_key";
        final String SORT_BY_KEY = "sort_by";
        // just to get movies people have heard of
        final String VOTE_COUNT_KEY = "vote_count.gte";
        final String VOTE_COUNT_VALUE = "3000";

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

            final String MDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";

            Uri builtUri = Uri.parse(MDB_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
                    .appendQueryParameter(SORT_BY_KEY, params[0])
                    .appendQueryParameter(VOTE_COUNT_KEY, VOTE_COUNT_VALUE)
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
            mMainActivityFragment.mUriImageAdapter.clear();

            mMainActivityFragment.mUriImageAdapter.addAll(result);

        }
    }
}
