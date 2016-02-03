package example.com.udacitymovie;

import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import example.com.udacitymovie.data.MovieContract;
import example.com.udacitymovie.model.MovieItem;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Uri uri = getIntent().getExtras().getParcelable("selectedUri");
//        int deleteRow = 0;
        Fragment fragment = DetailFragment.newInstance(uri);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_body, fragment);
        fragmentTransaction.commit();

    }
}
