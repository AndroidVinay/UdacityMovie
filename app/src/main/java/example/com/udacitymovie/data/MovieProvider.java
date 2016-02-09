package example.com.udacitymovie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.security.Provider;

/**
 * Created by VPPL-10132 on 1/25/2016.
 */
public class MovieProvider extends ContentProvider {

    private static final String TAG = MovieProvider.class.getSimpleName();

    public static final int FAVOURITE = 1;
    public static final int FAVOURITE_BY_ID = 2;
    public static final int FAVOURITE_BY_SERVER_ID = 3;
    public static final int FAVOURITE_IMAGE = 4;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    DBhelper DBhelper;

    private static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.Favourite.TABLE_FAVOURITE, FAVOURITE);
        matcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.Favourite.TABLE_FAVOURITE + "/*", FAVOURITE_BY_ID);


        return matcher;
    }


    @Override
    public boolean onCreate() {
        DBhelper = new DBhelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVOURITE: {
                return MovieContract.Favourite.CONTENT_DIR_TYPE;
            }
            case FAVOURITE_BY_ID: {
                return MovieContract.Favourite.CONTENT_ITEM_TYPE;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            case FAVOURITE: {

                retCursor = DBhelper.getReadableDatabase().query(
                        MovieContract.Favourite.TABLE_FAVOURITE,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            case FAVOURITE_BY_SERVER_ID: {

                retCursor = DBhelper.getReadableDatabase().query(
                        MovieContract.Favourite.TABLE_FAVOURITE,
                        projection,
                        MovieContract.Favourite.COLUMN_SERVER_ID+ " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }

            case FAVOURITE_BY_ID: {

                retCursor = DBhelper.getReadableDatabase().query(
                        MovieContract.Favourite.TABLE_FAVOURITE,
                        projection,
                        MovieContract.Favourite.COLUMN_SERVER_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                return retCursor;
            }
            default: {
                // By default, we assume a bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

        }

//        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = DBhelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITE: {
                long _id = db.insert(MovieContract.Favourite.TABLE_FAVOURITE, null, values);
                if (_id > 0) {
                    returnUri = MovieContract.Favourite.BuildFavouriteUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown Uri" + uri);

            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = DBhelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;
        switch (match) {
            case FAVOURITE:
                numDeleted = db.delete(
                        MovieContract.Favourite.TABLE_FAVOURITE, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.Favourite.TABLE_FAVOURITE + "'");
                break;
            case FAVOURITE_BY_ID:
                numDeleted = db.delete(MovieContract.Favourite.TABLE_FAVOURITE,
                        MovieContract.Favourite._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.Favourite.TABLE_FAVOURITE + "'");

                break;

            case FAVOURITE_BY_SERVER_ID:
                numDeleted = db.delete(MovieContract.Favourite.TABLE_FAVOURITE, MovieContract.Favourite.COLUMN_SERVER_ID + " = ?", new String[]{String.valueOf(ContentUris.parseId(uri))});
                //reset SERVER_ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.Favourite.TABLE_FAVOURITE + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = DBhelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null) {
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch (sUriMatcher.match(uri)) {
            case FAVOURITE: {
                numUpdated = db.update(MovieContract.Favourite.TABLE_FAVOURITE,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVOURITE_BY_ID: {
                numUpdated = db.update(MovieContract.Favourite.TABLE_FAVOURITE,
                        contentValues,
                        MovieContract.Favourite._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = DBhelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITE:
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                int numInserted = 0;
                try {
                    for (ContentValues value : values) {
                        if (value == null) {
                            throw new IllegalArgumentException("Cannot have null content values");
                        }
                        long _id = -1;
                        try {
                            _id = db.insertOrThrow(MovieContract.Favourite.TABLE_FAVOURITE,
                                    null, value);
                        } catch (SQLiteConstraintException e) {
                            Log.w(TAG, "Attempting to insert " +
                                    value.getAsString(
                                            MovieContract.Favourite.COLUMN_ORIGINAL_TITLE)
                                    + " but value is already in database.");
                        }
                        if (_id != -1) {
                            numInserted++;
                        }
                    }
                    if (numInserted > 0) {
                        // If no errors, declare a successful transaction.
                        // database will not populate if this is not called
                        db.setTransactionSuccessful();
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                if (numInserted > 0) {
                    // if there was successful insertion, notify the content resolver that there
                    // was a change
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return numInserted;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
