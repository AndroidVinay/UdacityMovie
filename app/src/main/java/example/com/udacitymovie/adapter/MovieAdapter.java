package example.com.udacitymovie.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
import java.util.List;
import java.util.zip.Inflater;

import example.com.udacitymovie.BuildConfig;
import example.com.udacitymovie.MainFragment;
import example.com.udacitymovie.R;
import example.com.udacitymovie.model.MovieItem;

/**
 * Created by vppl-10132 on 1/4/2016.
 */
public class MovieAdapter extends ArrayAdapter<MovieItem> {

    private Context mContext;
    private ArrayList<MovieItem> posterLinkUrl = new ArrayList<>();


    LayoutInflater inflater;

    public MovieAdapter(Context context, int resource, ArrayList<MovieItem> objects) {
        super(context, resource);
        mContext = context;
        posterLinkUrl = objects;


        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return posterLinkUrl.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//
        View rootView = inflater.inflate(R.layout.movie_grid_item, null);
        ImageView imageView;

        String url = "http://image.tmdb.org/t/p/w185";
        imageView = (ImageView) rootView.findViewById(R.id.iv_movie);

        if (posterLinkUrl != null) {
            Glide.with(mContext)
                    .load(url+posterLinkUrl.get(position).getPoster_path())
                    .centerCrop()
                    .into(imageView);
        } else {
            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
        }
        return rootView;

    }

}
