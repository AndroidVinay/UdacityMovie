package example.com.udacitymovie;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import example.com.udacitymovie.data.MovieContract;
import example.com.udacitymovie.model.MovieItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = DetailFragment.class.getSimpleName();
    private static final int CURSOR_LOADER_ID = 0;
    String movieServerId;
    String localPath;
    Cursor cur;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(Uri uri) {
        DetailFragment fragment = new DetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("selectedUri", uri);
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String url = "http://image.tmdb.org/t/p/w154";
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        TextView tv_og_title = (TextView) rootView.findViewById(R.id.tv_og_title);
        TextView tv_overview = (TextView) rootView.findViewById(R.id.tv_overview);
        TextView tv_vote_average = (TextView) rootView.findViewById(R.id.tv_vote_average);
        TextView tv_release_date = (TextView) rootView.findViewById(R.id.tv_release_date);
        ToggleButton btn_make_fav = (ToggleButton) rootView.findViewById(R.id.btn_make_fav);
        final ImageView iv_poster_image = (ImageView) rootView.findViewById(R.id.iv_poster_image);
        ListView lv_trailer = (ListView) rootView.findViewById(R.id.lv_trailor);

        Uri uri = getArguments().getParcelable("selectedUri");

        cur = getActivity().getContentResolver().query(uri,
                null, null, null, null);
        if (cur.getCount() > 0)
            cur.moveToNext();

        Log.d(TAG, "count of cur " + cur.getCount());
        movieServerId = cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_SERVER_ID));
        Log.d(TAG, "count of Server id " + cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_SERVER_ID)));
        tv_og_title.setText(cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_ORIGINAL_TITLE)));
        Log.d(TAG, "count of COLUMN_ORIGINAL_TITLE " + cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_ORIGINAL_TITLE)));
        tv_overview.setText(cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_OVERVIEW)));
        Log.d(TAG, "count of COLUMN_OVERVIEW " + cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_OVERVIEW)));
        tv_vote_average.setText(cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_VOTE_AVERAGE)));
        Log.d(TAG, "count of COLUMN_VOTE_AVERAGE " + cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_VOTE_AVERAGE)));
        tv_release_date.setText(cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_RELEASE_DATE)));
        Log.d(TAG, "count of COLUMN_RELEASE_DATE " + cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_RELEASE_DATE)));


        Glide.with(getContext())
                .load(url + cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_POSTER_PATH))).asBitmap()
                .into(iv_poster_image);
        Log.d(TAG, "count of COLUMN_POSTER_PATH " + cur.getString(cur.getColumnIndex(MovieContract.Favourite.COLUMN_POSTER_PATH)));

        Cursor c = getActivity().getContentResolver().query(uri,
                new String[]{MovieContract.Favourite.COLUMN_SERVER_ID},
                null,
                null,
                null);
        if (c.getCount() == 0) {

            btn_make_fav.setChecked(false);

            Log.d(TAG, "it not liked yet");

        } else {
            btn_make_fav.setChecked(true);

            Log.d(TAG, "it is already liked");

        }

        btn_make_fav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(getActivity(), "like", Toast.LENGTH_SHORT).show();

                    try {

                        FileOutputStream fileOutputStream = getContext().openFileOutput(movieServerId + ".jpg", getContext().MODE_PRIVATE);
                        Bitmap bitmap = covertToBitmap(iv_poster_image.getDrawable(), iv_poster_image.getDrawable().getIntrinsicWidth(), iv_poster_image.getDrawable().getIntrinsicHeight());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        File file = getContext().getFileStreamPath(movieServerId + ".jpg");
                        localPath = file.getAbsolutePath();

//                        movieItem.setPoster_path(localPath);

                        fileOutputStream.flush();
                        fileOutputStream.close();

                        //insert data in table
                        Cursor c = getActivity().getContentResolver().query(MovieContract.Favourite.CONTENT_URI,
                                new String[]{MovieContract.Favourite.COLUMN_SERVER_ID},
                                null,
                                null,
                                null);
                        if (c.getCount() == 0) {
                            updatedData();
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // initialize loader
                    getLoaderManager().initLoader(CURSOR_LOADER_ID, null, DetailFragment.this);

                } else {
                    Toast.makeText(getActivity(), "dislike", Toast.LENGTH_SHORT).show();

                    Cursor c = getActivity().getContentResolver().query(MovieContract.Favourite.CONTENT_URI,
                            new String[]{MovieContract.Favourite.COLUMN_SERVER_ID},
                            null,
                            null,
                            null);
                    if (c.getCount() > 0) {

                        int deleteRow = 0;
                        Uri uri = MovieContract.Favourite.BuildFavouriteUri(Long.valueOf(movieServerId));
                        deleteRow = getActivity().getContentResolver().delete(MovieContract.Favourite.CONTENT_URI,
                                MovieContract.Favourite.COLUMN_SERVER_ID + " = ?",
                                new String[]{movieServerId});

                        Log.d(TAG, "Number of row data deleted" + deleteRow);
                    }
                }
            }
        });


        return rootView;
    }

    public void updatedData() {

        ContentValues values = new ContentValues();
        values.put(MovieContract.Favourite.COLUMN_POSTER_PATH, localPath);
        int updateResult = 0;
        updateResult = getActivity().getContentResolver().update(MovieContract.Favourite.CONTENT_URI,
                values, MovieContract.Favourite.COLUMN_SERVER_ID + " = ?", new String[]{movieServerId});
        Log.d(TAG, "favourite data is updated : " + updateResult);

    }

//    public void insertData() {
//
//        ContentValues values = new ContentValues();
//        values.put(MovieContract.Favourite.COLUMN_SERVER_ID, movieItem.getId());
//        values.put(MovieContract.Favourite.COLUMN_POSTER_PATH, movieItem.getPoster_path());
//        values.put(MovieContract.Favourite.COLUMN_OVERVIEW, movieItem.getOverview());
//        values.put(MovieContract.Favourite.COLUMN_RELEASE_DATE, movieItem.getRelease_date());
//        values.put(MovieContract.Favourite.COLUMN_ORIGINAL_TITLE, movieItem.getOriginal_title());
//        values.put(MovieContract.Favourite.COLUMN_ORIGINAL_LANGUAGE, movieItem.getOriginal_language());
//        values.put(MovieContract.Favourite.COLUMN_TITLE, movieItem.getTitle());
//        values.put(MovieContract.Favourite.COLUMN_BACKDROP_PATH, movieItem.getBackdrop_path());
//        values.put(MovieContract.Favourite.COLUMN_POPULARITY, movieItem.getPopularity());
//        values.put(MovieContract.Favourite.COLUMN_VOTE_COUNT, movieItem.getVote_count());
//        values.put(MovieContract.Favourite.COLUMN_VIDEO, movieItem.getVideo());
//        values.put(MovieContract.Favourite.COLUMN_VOTE_AVERAGE, movieItem.getVote_average());
//
//        getActivity().getContentResolver().insert(MovieContract.Favourite.CONTENT_URI,
//                values);
//    }

    public Bitmap covertToBitmap(Drawable drawable, int width, int hight) {

        Bitmap bitmap = Bitmap.createBitmap(width, hight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, hight);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}



