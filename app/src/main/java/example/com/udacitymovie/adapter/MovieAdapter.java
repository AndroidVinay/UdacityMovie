package example.com.udacitymovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

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

        MovieItem movieItem = posterLinkUrl.get(position);
        ImageView imageView;
        String url = "http://image.tmdb.org/t/p/w154";

        if (null == convertView) {
            convertView = inflater.inflate(R.layout.movie_grid_item, null);
        }
//
        imageView = (ImageView) convertView.findViewById(R.id.iv_movie);


        Glide.with(mContext)
                .load(url + movieItem.getPoster_path())
                .centerCrop()
                .fitCenter()
                .crossFade()
                .into(imageView);
        return convertView;
    }

}
