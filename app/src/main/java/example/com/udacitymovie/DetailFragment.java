package example.com.udacitymovie;


import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

    private static final int CURSOR_LOADER_ID = 0;
    MovieItem movieItem;

    public DetailFragment() {
        // Required empty public constructor
    }


    public static DetailFragment newInstance(MovieItem movieItem) {
        DetailFragment fragment = new DetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable("movieDetail", movieItem);
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
        Button btn_make_fav = (Button) rootView.findViewById(R.id.btn_make_fav);
        final ImageView iv_poster_image = (ImageView) rootView.findViewById(R.id.iv_poster_image);
        ListView lv_trailer = (ListView) rootView.findViewById(R.id.lv_trailor);

        movieItem = (MovieItem) getArguments().get("movieDetail");

        tv_og_title.setText(movieItem.getOriginal_title().toString());
        tv_overview.setText(movieItem.getOverview());
        tv_vote_average.setText(movieItem.getVote_average().toString());
        tv_release_date.setText(movieItem.getRelease_date().toString());
        Glide.with(getContext())
                .load(url + movieItem.getPoster_path()).asBitmap()
                .into(iv_poster_image);
        btn_make_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    FileOutputStream fileOutputStream = getContext().openFileOutput(movieItem.getId() + ".jpg", getContext().MODE_PRIVATE);
                    Bitmap bitmap = covertToBitmap(iv_poster_image.getDrawable(),iv_poster_image.getDrawable().getIntrinsicWidth(),iv_poster_image.getDrawable().getIntrinsicHeight());
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
                    File file  =getContext().getFileStreamPath(movieItem.getId()+".jpg");
                    String localPath = file.getAbsolutePath();

                    movieItem.setPoster_path(localPath);

                    fileOutputStream.flush();
                    fileOutputStream.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//
//                Cursor c =
//                        getActivity().getContentResolver().query(MovieContract.Favourite.CONTENT_URI,
//                                new String[]{MovieContract.Favourite.COLUMN_SERVER_ID},
//                                null,
//                                null,
//                                null);
//                if (c.getCount() == 0) {
//


                insertData();
//                }
                // initialize loader
                getLoaderManager().initLoader(CURSOR_LOADER_ID, null, DetailFragment.this);
            }
        });

        return rootView;
    }

    public void insertData() {

        ContentValues values = new ContentValues();
        values.put(MovieContract.Favourite.COLUMN_SERVER_ID, movieItem.getId());
        values.put(MovieContract.Favourite.COLUMN_POSTER_PATH, movieItem.getPoster_path());
        values.put(MovieContract.Favourite.COLUMN_OVERVIEW, movieItem.getOverview());
        values.put(MovieContract.Favourite.COLUMN_RELEASE_DATE, movieItem.getRelease_date());
        values.put(MovieContract.Favourite.COLUMN_ORIGINAL_TITLE, movieItem.getOriginal_title());
        values.put(MovieContract.Favourite.COLUMN_ORIGINAL_LANGUAGE, movieItem.getOriginal_language());
        values.put(MovieContract.Favourite.COLUMN_TITLE, movieItem.getTitle());
        values.put(MovieContract.Favourite.COLUMN_BACKDROP_PATH, movieItem.getBackdrop_path());
        values.put(MovieContract.Favourite.COLUMN_POPULARITY, movieItem.getPopularity());
        values.put(MovieContract.Favourite.COLUMN_VOTE_COUNT, movieItem.getVote_count());
        values.put(MovieContract.Favourite.COLUMN_VIDEO, movieItem.getVideo());
        values.put(MovieContract.Favourite.COLUMN_VOTE_AVERAGE, movieItem.getVote_average());

        getActivity().getContentResolver().insert(MovieContract.Favourite.CONTENT_URI,
                values);
    }

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



