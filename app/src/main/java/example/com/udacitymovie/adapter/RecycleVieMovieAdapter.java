package example.com.udacitymovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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

    public RecycleVieMovieAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        this.context = context;
        this.cursor = cursor;
    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(this.cursor);
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        return super.swapCursor(this.cursor);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_item, parent, false);
        MovieHolder movieHolder = new MovieHolder(view);
        return movieHolder;

    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }

    @Override
    public void onBindViewHolder(MovieHolder viewHolder, Cursor cursor) {
        Log.d(TAG, " on Bind View Holder" + cursor.getString(cursor.getColumnIndex(MovieContract.Favourite.COLUMN_POSTER_PATH)));

        this.cursor = cursor;
        viewHolder.serverId = getCursor().getString(getCursor().getColumnIndex(MovieContract.Favourite.COLUMN_SERVER_ID));
        Glide.with(context)
                .load(getCursor().getString(getCursor().getColumnIndex(MovieContract.Favourite.COLUMN_POSTER_PATH))).asBitmap()
                .centerCrop()
                .fitCenter()
                .into(viewHolder.imageView);

//        }


    }

    @Override
    public void onBindViewHolder(MovieHolder viewHolder, int position) {
        super.onBindViewHolder(viewHolder, position);


    }


    class MovieHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        String serverId;


        public MovieHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.iv_movie);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "" + serverId, Toast.LENGTH_SHORT).show();
                    Uri uri = MovieContract.Favourite.BuildFavouriteUri(Long.parseLong(serverId));
                    Intent i = new Intent(context, DetailActivity.class);
                    i.putExtra("selectedUri", uri);
                    context.startActivity(i);


                }
            });

        }
    }
}
