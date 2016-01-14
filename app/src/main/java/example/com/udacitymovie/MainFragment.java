package example.com.udacitymovie;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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
import example.com.udacitymovie.model.MovieItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    public static final String API_KEY_VALUE = "e1dc62d7886d8f0e3741112dbef87484";

    private MovieAdapter mMovieListAdapter;
    public static int mImageWidth;
    public static int mImageHeight;
    private GridView gridView;
    ArrayList<MovieItem> posterLinkUrl = new ArrayList<MovieItem>();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchMovieList().execute("");
        setHasOptionsMenu(true);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        mImageWidth = displayMetrics.widthPixels / 2;
        mImageHeight = mImageWidth * 4 / 3;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.gv_movie);
        mMovieListAdapter = new MovieAdapter(getActivity(), R.layout.movie_grid_item, posterLinkUrl);
        gridView.setAdapter(mMovieListAdapter);

        return rootView;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_main, menu);


    }


    private class FetchMovieList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;

            BufferedReader reader = null;

            String jsonString = null;

            try {

                String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1";
                String apiKey = "&api_key=" + MainFragment.API_KEY_VALUE;
                URL url = new URL(baseUrl.concat(apiKey));


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
                jsonString = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
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
                        Log.e("PlaceholderFragment", "Error closing stream", e);
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


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
            mMovieListAdapter = new MovieAdapter(getActivity(), R.layout.movie_grid_item, posterLinkUrl);
            gridView.setAdapter(mMovieListAdapter);

        }
    }
}
