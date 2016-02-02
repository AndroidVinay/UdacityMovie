package example.com.udacitymovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import example.com.udacitymovie.DetailActivity;
import example.com.udacitymovie.R;
import example.com.udacitymovie.data.MovieContract;

/**
 * Created by VPPL-10132 on 2/1/2016.
 */
public class RecycleVieMovieAdapter extends CursorRecyclerViewAdapter {
    private String TAG = RecycleVieMovieAdapter.class.getSimpleName();
    Context context;
    Cursor cursor;

    public RecycleVieMovieAdapter(Context context, Cursor cursor, int flag) {
        super(context, cursor);
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        MovieHolder movieHolder = new MovieHolder(view);
        return movieHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final Cursor cursor) {
        MovieHolder movieHolder = (MovieHolder) viewHolder;
        String url = "http://image.tmdb.org/t/p/w154";

        Log.d(TAG, "" + cursor.getString(cursor.getColumnIndex(MovieContract.Favourite.COLUMN_POSTER_PATH)));
        Glide.with(context)
                .load(url + cursor.getString(cursor.getColumnIndex(MovieContract.Favourite.COLUMN_POSTER_PATH))).asBitmap()
                .centerCrop()
                .fitCenter()
                .into(movieHolder.imageView);

        movieHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d(TAG, "" + cursor.getString(cursor.getColumnIndex(MovieContract.Favourite.COLUMN_SERVER_ID)));

                Intent i = new Intent(context, DetailActivity.class);
                i.putExtra("movieServerId", cursor.getString(cursor.getColumnIndex(MovieContract.Favourite.COLUMN_SERVER_ID)));
                context.startActivity(i);

            }
        });

    }


    class MovieHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
//        TextView tvMovieName;

        public MovieHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.iv_movie);
//            tvMovieName = (TextView) itemView.findViewById(R.id.tv_movie_name);

            // What would be best way for Recycle Click Listener

        }
    }
}
