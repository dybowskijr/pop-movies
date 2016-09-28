package us.dybowski.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchMovieTask extends AsyncTask<String, Void, Movie> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private DetailFragment mDetailFragment;

    public FetchMovieTask(DetailFragment df) {
        mDetailFragment = df;
    }

    private Movie getMovieFromJson(String forecastJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RUNTIME = "runtime";
        final String MDB_RELEASE_DATE = "release_date";
        final String MDB_OVERVIEW = "overview";
        final String MDB_VOTE_AVERAGE = "vote_average";
        final String MDB_TITLE = "title";
        final String MDB_ID = "id";
        final String MDB_POSTER_PATH = "poster_path";

        JSONObject movieJson = new JSONObject(forecastJsonStr);

        return new Movie(
                movieJson.getString(MDB_ID),
                movieJson.getString(MDB_VOTE_AVERAGE),
                movieJson.getString(MDB_OVERVIEW),
                movieJson.getString(MDB_POSTER_PATH),
                movieJson.getString(MDB_RELEASE_DATE),
                movieJson.getString(MDB_RUNTIME),
                movieJson.getString(MDB_TITLE)
        );
    }

    /**
     *
     * @param params first parameter will be the sort_by value (vote_average.desc, popularity.desc)
     * @return
     */
    @Override
    protected Movie doInBackground(String... params) {

        final String API_KEY_VALUE = BuildConfig.THE_MOVIE_DB_API_KEY;
        final String API_KEY_KEY = "api_key";

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

            final String MDB_BASE_URL = "http://api.themoviedb.org/3/movie";
            final String MDB_KEY_VALUE = BuildConfig.THE_MOVIE_DB_API_KEY;
            final String MDB_API_KEY = "api_key";

            Uri builtUri = Uri.parse(MDB_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(MDB_API_KEY, MDB_KEY_VALUE)
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
            return getMovieFromJson(moviesJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    protected void onPostExecute(Movie movie) {
        if (movie != null) {
            Log.i("ONPOSTEXECUTE", movie.getTitle());

        }
    }
}
