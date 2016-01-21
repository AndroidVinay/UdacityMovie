package example.com.udacitymovie;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import example.com.udacitymovie.model.MovieItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private String TAG = MainFragment.class.getSimpleName();
    public static final String API_KEY_VALUE = "e1dc62d7886d8f0e3741112dbef87484";
    private MovieAdapter mMovieListAdapter;
    private GridView gridView;
    ArrayList<MovieItem> posterLinkUrl = new ArrayList<MovieItem>();

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "on Create" + posterLinkUrl.size() + "");

        if (savedInstanceState == null || !savedInstanceState.containsKey("movieList")) {
            new FetchMovieList().execute("");
        } else {
            posterLinkUrl = savedInstanceState.getParcelableArrayList("movieList");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

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
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movieList", posterLinkUrl);
        super.onSaveInstanceState(outState);
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
            try {
                String baseUrl = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1";
                String apiKey = "&api_key=" + MainFragment.API_KEY_VALUE;
                URL url = new URL(baseUrl.concat(apiKey));

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
            } else {
                Toast.makeText(getContext(), "posterLinkUrl is empty", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
