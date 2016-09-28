/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package us.dybowski.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;

/**
 * Defines table and column names for the weather database.
 */
public class MovieContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "us.dybowski.popularmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
    // For instance, content://com.example.android.sunshine.app/weather/ is a valid path for
    // looking at weather data. content://com.example.android.sunshine.app/givemeroot/ will fail,
    // as the ContentProvider hasn't been given any information on what to do with "givemeroot".
    // At least, let's hope not.  Don't be that dev, reader.  Don't be that dev.
    public static final String PATH_MOVIES = "movies"; // discover/movie
    public static final String PATH_MOVIE = "movie"; //  movie/id
    public static final String PATH_REVIEWS = "reviews"; // movie/id/reviews
    public static final String PATH_TRAILERS = "trailers"; // movie/id/viseos (type: trailer)



    /* Inner class that defines the table contents of the movie table */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";

        // The movie id string is what will be sent to movie api
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";
        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static Uri buildMovieWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesUri() {
            return CONTENT_URI;
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

        /* Inner class that defines the table contents of the movie table */
    public static final class ReviewEntry implements BaseColumns {
            public static final Uri CONTENT_URI =
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEWS).build();

            public static final String CONTENT_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEWS;

            public static final String TABLE_NAME = "review";
            public static final String COLUMN_REVIEW_ID = "id";
            public static final String COLUMN_AUTHOR = "author";
            public static final String COLUMN_CONTENT = "content";
            public static final String COLUMN_MOVIE_ID = "movie_id";

            public static Uri buildReviewWithIdUri(long id) {
                return ContentUris.withAppendedId(CONTENT_URI, id);
            }
        }

    public static final class TrailerEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILERS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILERS;

        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_TRAILER_ID = "id";
        public static final String COLUMN_SITE = "site";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_KEY = "key";
        public static final String OOLUMN_MOVIE_ID = "movie_id";

        public static Uri buildTrailerWithIdUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }    }
}