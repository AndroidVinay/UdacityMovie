package example.com.udacitymovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import example.com.udacitymovie.adapter.MovieAdapter;
import example.com.udacitymovie.adapter.RecycleVieMovieAdapter;
import example.com.udacitymovie.data.MovieContract;
import example.com.udacitymovie.model.MovieItem;

/**
 * A simple {@link Fragment} subclass.
 */

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = MainFragment.class.getSimpleName();
    public static final String API_KEY_VALUE = "e1dc62d7886d8f0e3741112dbef87484";
    //    private MovieAdapter mMovieListAdapter;
    RecycleVieMovieAdapter recycleVieMovieAdapter;
    //    private GridView gridView;
    private RecyclerView recyclerView;
    ArrayList<MovieItem> posterLinkUrl = new ArrayList<MovieItem>();
    String sortBy, page;
    public static int mImageWidth;
    public static int mImageHeight;
    private static final int MOVIE_LOADER_ID = 0;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(TAG, "on Create" + posterLinkUrl.size() + "");

        if (savedInstanceState == null || !savedInstanceState.containsKey("movieList")) {
            sortBy = "popularity.desc";
            page = "1";
            new FetchMovieList().execute(sortBy, page);
        } else {
            posterLinkUrl = savedInstanceState.getParcelableArrayList("movieList");
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mImageWidth = displayMetrics.widthPixels / 2;
        mImageHeight = mImageWidth * 4 / 3;

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movie);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        Cursor cur = getActivity().getContentResolver().query(
                MovieContract.Favourite.CONTENT_URI,
                null,
                null,
                null,
                null);
        recycleVieMovieAdapter = new RecycleVieMovieAdapter(getActivity(), cur, 0);
        Log.d(TAG, "on Resume" + posterLinkUrl.size() + "");
        recyclerView.setAdapter(recycleVieMovieAdapter);
        recycleVieMovieAdapter.notifyDataSetChanged();

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activiy_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.most_popular:

                sortBy = "popularity.desc";
                page = "1";
                new FetchMovieList().execute(sortBy, page);

                break;

            case R.id.highest_rated:

                sortBy = "vote_average.desc";
                page = "1";
                posterLinkUrl.clear();

                new FetchMovieList().execute(sortBy, page);

                break;

            case R.id.favourate:
                posterLinkUrl.clear();
                Cursor c = getActivity().getContentResolver().query(MovieContract.Favourite.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);
                if (c.getCount() == 0) {

                    for (int i = 0; i < c.getCount(); i++) {
                        c.moveToFirst();
                        String id1 = c.getString(c.getColumnIndex(MovieContract.Favourite.COLUMN_SERVER_ID));
                        String poster_path = c.getString(c.getColumnIndex(MovieContract.Favourite.COLUMN_POSTER_PATH));
                        String title = c.getString(c.getColumnIndex(MovieContract.Favourite.COLUMN_TITLE));
                        String original_title = c.getString(c.getColumnIndex(MovieContract.Favourite.COLUMN_ORIGINAL_TITLE));
                        String overview = c.getString(c.getColumnIndex(MovieContract.Favourite.COLUMN_OVERVIEW));
                        String vote_average = c.getString(c.getColumnIndex(MovieContract.Favourite.COLUMN_VOTE_AVERAGE));
                        String release_date = c.getString(c.getColumnIndex(MovieContract.Favourite.COLUMN_RELEASE_DATE));
                        MovieItem mv = new MovieItem(id1, poster_path, title, original_title, overview, vote_average, release_date);
                        posterLinkUrl.add(mv);
                    }

                    if (posterLinkUrl.size() > 0) {
//                        recycleVieMovieAdapter.addAll(posterLinkUrl);
//                        mMovieListAdapter.notifyDataSetChanged();
//                        Log.d(TAG, "on PostExecute" + posterLinkUrl.size() + "");
                    } else {
                        Toast.makeText(getContext(), "posterLinkUrl is empty", Toast.LENGTH_SHORT).show();
                    }

                }

                break;

            default:

                break;

        }


        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieList", posterLinkUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.Favourite.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        recycleVieMovieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        loader.reset();
    }


    private class FetchMovieList extends AsyncTask<String, String, String> {

        private String TAG = FetchMovieList.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            posterLinkUrl.clear();
            Log.d(TAG, "onPreExecute" + posterLinkUrl.size() + "");
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonString = null;

            String sortBy = params[0];
            String page = params[1];
//            String releaseYear = params[2];
            Uri buildUri = Uri.parse(UrlString.movieBaseURL)
                    .buildUpon()
                    .appendQueryParameter(UrlString.param_sortBy, sortBy)
                    .appendQueryParameter(UrlString.param_page, page)
                    .appendQueryParameter(UrlString.param_apiKey, MainFragment.API_KEY_VALUE).build();

            try {
//                String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1";
//                String apiKey = "&api_key=" + MainFragment.API_KEY_VALUE;
                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                jsonString = buffer.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            ArrayList<ContentValues> cv;
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                cv = new ArrayList(jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jobj = jsonArray.getJSONObject(i);
                    MovieItem movieItem = new MovieItem(jobj.getString("id"),
                            jobj.getString("poster_path"),
                            jobj.getString("title"),
                            jobj.getString("original_title"),
                            jobj.getString("overview"),
                            jobj.getString("vote_average"),
                            jobj.getString("release_date"));
                    posterLinkUrl.add(movieItem);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MovieContract.Favourite.COLUMN_SERVER_ID, jobj.getString("id"));
                    contentValues.put(MovieContract.Favourite.COLUMN_POSTER_PATH, jobj.getString("poster_path"));
                    contentValues.put(MovieContract.Favourite.COLUMN_TITLE, jobj.getString("title"));
                    contentValues.put(MovieContract.Favourite.COLUMN_ORIGINAL_TITLE, jobj.getString("original_title"));
                    contentValues.put(MovieContract.Favourite.COLUMN_OVERVIEW, jobj.getString("overview"));
                    contentValues.put(MovieContract.Favourite.COLUMN_VOTE_AVERAGE, jobj.getString("vote_average"));
                    contentValues.put(MovieContract.Favourite.COLUMN_RELEASE_DATE, jobj.getString("release_date"));
                    cv.add(contentValues);

                }
                int inserted = 0;

                ContentValues[] cvArray = new ContentValues[cv.size()];
                cv.toArray(cvArray);
                inserted = getActivity().getContentResolver().bulkInsert(MovieContract.Favourite.CONTENT_URI,
                        cvArray);

                Log.d(TAG, "on Background" + posterLinkUrl.size() + "");
                Log.d(TAG, "on Background" + " data inserted   " + inserted);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonString;
        }

    }
}
