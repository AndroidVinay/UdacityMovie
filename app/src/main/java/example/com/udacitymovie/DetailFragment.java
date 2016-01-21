package example.com.udacitymovie;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import example.com.udacitymovie.model.MovieItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {


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
        ImageView iv_poster_image = (ImageView) rootView.findViewById(R.id.iv_poster_image);
        ListView lv_trailer = (ListView) rootView.findViewById(R.id.lv_trailor);


        MovieItem movieItem = (MovieItem) getArguments().get("movieDetail");

        tv_og_title.setText(movieItem.getOriginal_title().toString());
        tv_overview.setText(movieItem.getOverview());
        tv_vote_average.setText(movieItem.getVote_average().toString());
        tv_release_date.setText(movieItem.getRelease_date().toString());
        Glide.with(getContext())
                .load(url + movieItem.getPoster_path())
                .crossFade()
                .into(iv_poster_image);


        return rootView;
    }


}
