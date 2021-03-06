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


public class FetchTrailersTask extends AsyncTask<Long, Void, ArrayList<Trailer>> {

    private final String LOG_TAG = FetchTrailersTask.class.getSimpleName();
    private DetailFragment mDetailFragment;


    public FetchTrailersTask(DetailFragment fragment) {
       mDetailFragment = fragment;
    }


    private ArrayList<Trailer> getReviewsFromJson(String forecastJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String MDB_RESULTS = "results";
        final String MDB_KEY = "key";
        final String MDB_SITE = "site";
        final String MDB_SIZE = "size";
        final String MDB_NAME = "name";

        ArrayList<Trailer> trailerList = new ArrayList<>();

        JSONObject trailersJson = new JSONObject(forecastJsonStr);
        JSONArray trailerJsonArray = trailersJson.getJSONArray(MDB_RESULTS);

        for (int i = 0; i < trailerJsonArray.length(); i++) {

            JSONObject trailerJson = trailerJsonArray.getJSONObject(i);
            Log.i(LOG_TAG, trailerJson.getString(MDB_NAME));
            Log.i(LOG_TAG, trailerJson.getString(MDB_KEY));
            trailerList.add(
                    new Trailer(
                            trailerJson.getString(MDB_KEY),
                            trailerJson.getString(MDB_SITE),
                            trailerJson.getString(MDB_NAME)
                    )
            );
        }
        return trailerList;
    }

    /**
     *
     * @param params first parameter will be the sort_by value (vote_average.desc, popularity.desc)
     * @return
     */
    @Override
    protected ArrayList<Trailer> doInBackground(Long... params) {
        final String THE_MOVIE_DB_TRAILERS_BASE_PATH = "https://api.themoviedb.org/3/movie";

        final String API_KEY_VALUE = BuildConfig.THE_MOVIE_DB_API_KEY;
        final String API_KEY_KEY = "api_key";
        final String API_TRAILERS_PATH = "videos";

        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String reviewsJsonString = null;

        String format = "json";

        try {
            Uri builtUri = Uri.parse(THE_MOVIE_DB_TRAILERS_BASE_PATH).buildUpon()
                    .appendPath(Long.toString(params[0]))
                    .appendPath(API_TRAILERS_PATH)
                    .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
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
            reviewsJsonString = buffer.toString();
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
            return getReviewsFromJson(reviewsJsonString);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    protected void onPostExecute(ArrayList<Trailer> result) {
        mDetailFragment.mTrailerAdapter.clear();
        if (result != null) {
            mDetailFragment.mTrailerAdapter.addAll(result);
        }
    }
}
