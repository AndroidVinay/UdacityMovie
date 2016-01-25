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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import example.com.udacitymovie.adapter.MovieAdapter;
import example.com.udacitymovie.data.MovieContract;
import example.com.udacitymovie.model.MovieItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private String TAG = MainFragment.class.getSimpleName();
    public static final String API_KEY_VALUE = "e1dc62d7886d8f0e3741112dbef87484";
    private MovieAdapter mMovieListAdapter;
    private GridView gridView;
    ArrayList<MovieItem> posterLinkUrl = new ArrayList<MovieItem>();
    String sortBy, page;
    public static int mImageWidth;
    public static int mImageHeight;
    private static final int CURSOR_LOADER_ID = 0;

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
        gridView = (GridView) rootView.findViewById(R.id.gv_movie);
        mMovieListAdapter = new MovieAdapter(getActivity(), R.layout.movie_grid_item, posterLinkUrl);
        Log.d(TAG, "on Resume" + posterLinkUrl.size() + "");
        gridView.setAdapter(mMovieListAdapter);
        mMovieListAdapter.notifyDataSetChanged();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                MovieItem movieItem = posterLinkUrl.get(position);
                intent.putExtra("movieDetails", movieItem);
                startActivity(intent);
            }
        });

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
        data.close();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        loader.reset();
    }


    public void insertData() {
        ContentValues[] flavorValuesArr = new ContentValues[posterLinkUrl.size()];
        // Loop through static array of Flavors, add each to an instance of ContentValues
        // in the array of ContentValues
        for (int i = 0; i < posterLinkUrl.size(); i++) {
            flavorValuesArr[i] = new ContentValues();
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_SERVER_ID, posterLinkUrl.get(i).getId());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_POSTER_PATH,
                    posterLinkUrl.get(i).getPoster_path());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_OVERVIEW, posterLinkUrl.get(i).getOverview());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_RELEASE_DATE, posterLinkUrl.get(i).getRelease_date());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_ORIGINAL_TITLE, posterLinkUrl.get(i).getOriginal_title());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_ORIGINAL_LANGUAGE, posterLinkUrl.get(i).getOriginal_language());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_TITLE, posterLinkUrl.get(i).getTitle());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_BACKDROP_PATH, posterLinkUrl.get(i).getBackdrop_path());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_POPULARITY, posterLinkUrl.get(i).getPopularity());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_VOTE_COUNT, posterLinkUrl.get(i).getVote_count());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_VIDEO, posterLinkUrl.get(i).getVideo());
            flavorValuesArr[i].put(MovieContract.Favourite.COLUMN_VOTE_AVERAGE, posterLinkUrl.get(i).getVote_average());
        }

        // bulkInsert our ContentValues array
        getActivity().getContentResolver().bulkInsert(MovieContract.Favourite.CONTENT_URI,
                flavorValuesArr);
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


            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray jsonArray = jsonObject.getJSONArray("results");
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
                }
                Log.d(TAG, "on Background" + posterLinkUrl.size() + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
            if (posterLinkUrl.size() > 0) {
                mMovieListAdapter.addAll(posterLinkUrl);
                mMovieListAdapter.notifyDataSetChanged();
                Log.d(TAG, "on PostExecute" + posterLinkUrl.size() + "");

                Cursor c =
                        getActivity().getContentResolver().query(MovieContract.Favourite.CONTENT_URI,
                                new String[]{MovieContract.Favourite._ID},
                                null,
                                null,
                                null);
                if (c.getCount() == 0) {
                    insertData();
                }
                // initialize loader
                getLoaderManager().initLoader(CURSOR_LOADER_ID, null, MainFragment.this);
            } else {
                Toast.makeText(getContext(), "posterLinkUrl is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
