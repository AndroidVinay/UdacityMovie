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
public class MovieAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<String> posterLinkUrl = new ArrayList<>();
    View rootView;
    Fragment fragment;
    private static int mImageWidth;
    private static int mImageHeight;
    LayoutInflater inflater;

    public MovieAdapter(Context context, int resource, ArrayList<String> objects, Fragment fragment) {
        super(context, resource);
        mContext = context;
        posterLinkUrl = objects;
        this.fragment = fragment;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        fragment.getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mImageWidth = displayMetrics.widthPixels / 2;
        mImageHeight = mImageWidth * 4 / 3;

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

        imageView = (ImageView) rootView.findViewById(R.id.iv_movie);

        if (posterLinkUrl != null) {
            Glide.with(fragment)
                    .load("http://image.tmdb.org/t/p/w154/5MUyULSD4syaMQFcfPABqopAO4e.jpg")
                    .override(mImageWidth, mImageHeight)
                    .into(imageView);
        } else {
            Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show();
        }

        return rootView;
//        final ImageView myImageView;
//        if (convertView == null) {
//            myImageView = (ImageView) inflater.inflate(R.layout.movie_grid_item, parent, false);
//        } else {
//            myImageView = (ImageView) convertView;
//        }
//
////       ? String url = myUrls.get(position);
//
//        Glide.with(mContext)
//                .load("http://image.tmdb.org/t/p/w154/5MUyULSD4syaMQFcfPABqopAO4e.jpg")
//                .centerCrop()
//                .crossFade()
//                .into(myImageView);
//
//        return myImageView;

    }

}
