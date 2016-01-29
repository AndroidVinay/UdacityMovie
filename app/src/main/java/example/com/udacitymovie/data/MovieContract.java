package example.com.udacitymovie.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by VPPL-10132 on 1/25/2016.
 */
public class MovieContract {

    public static String TAG = MovieContract.class.getSimpleName();

    public static final String CONTENT_AUTHORITY = "example.com.udacitymovie";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class Favourite implements BaseColumns {

        public static final String TABLE_FAVOURITE = "favourites";
        public static final String _ID = "_id";
        public static final String COLUMN_SERVER_ID = "server_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE = "original_language";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String SQL_CREATE = T.CREATE_TABLE + TABLE_FAVOURITE + T.OPEN_BRACE
                + _ID + T.TYPE_INTEGER + T.PRIMARY_KEY + T.AUTO_INCREMENT + T.SEP_COMMA
                + COLUMN_SERVER_ID + T.TYPE_INTEGER + T.SEP_COMMA
                + COLUMN_POSTER_PATH + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_OVERVIEW + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_RELEASE_DATE + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_ORIGINAL_TITLE + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_ORIGINAL_LANGUAGE + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_TITLE + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_BACKDROP_PATH + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_POPULARITY + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_VOTE_COUNT + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_VIDEO + T.TYPE_TEXT + T.SEP_COMMA
                + COLUMN_VOTE_AVERAGE + T.TYPE_TEXT + T.SEP_COMMA
                + T.UNIQUE + T.OPEN_BRACE + COLUMN_SERVER_ID + T.CLOSE_BRACE + T.ON_CONFLICT_REPLACE
                + T.CLOSE_BRACE + T.SEMICOLON;

        public static final String SQL_DROP = T.DROP_TABLE + TABLE_FAVOURITE;

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(Favourite.TABLE_FAVOURITE).build();

        public static final String CONTENT_DIR_TYPE = Uri.parse(ContentResolver.CURSOR_DIR_BASE_TYPE).buildUpon().appendPath(CONTENT_AUTHORITY)
                .appendPath(Favourite.TABLE_FAVOURITE).toString();

        public static final String CONTENT_ITEM_TYPE = Uri.parse(ContentResolver.CURSOR_ITEM_BASE_TYPE).buildUpon().appendPath(CONTENT_AUTHORITY)
                .appendPath(Favourite.TABLE_FAVOURITE).toString();

        public static Uri BuildFavouriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }


}
