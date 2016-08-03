package com.design.vikas.popularmovies.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.design.vikas.popularmovies.R;
import com.design.vikas.popularmovies.fragment.DetailsFragment;
import com.design.vikas.popularmovies.fragment.HomeFragment;
import com.design.vikas.popularmovies.interfaces.ClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements ClickListener{

    @BindView(R.id.toolbarMain)
    Toolbar toolbarMain;
    @BindView(R.id.flContainer)
    FrameLayout flContainer;
    public final static String VIEW_TYPE = "viewType";
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    static public boolean TWO_PANE;
    final static String TAG_SETTINGS = SettingsFragment.class.getName();
    final static String TAG_HOME = HomeFragment.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbarMain);

        if (findViewById(R.id.fl_movie_detail_container)!=null){
            // in two-pane mode.
            // In two-pane mode, show the detail view in this activity
            TWO_PANE = true;
        }

        if (findViewById(R.id.flContainer) != null) {

            if (savedInstanceState !=null) {
                return;
            }

            //start the new fragment on startup
            final HomeFragment homeFragment = new HomeFragment();
            final Bundle bundle = new Bundle();
            bundle.putString(VIEW_TYPE, "normal");
            homeFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.flContainer, homeFragment).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            //open the settings fragment
            getFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, new SettingsFragment()).
                    addToBackStack(TAG_SETTINGS).
                    commit();
        }
        if (id == R.id.favoriteMovie) {
            //open the settings fragment
            final HomeFragment homeFragment = new HomeFragment();
            final Bundle bundle = new Bundle();
            bundle.putString(VIEW_TYPE,"favorite");
            homeFragment.setArguments(bundle);
            getFragmentManager().
                    beginTransaction().
                    replace(R.id.flContainer, homeFragment).
                    addToBackStack(TAG_HOME).
                    commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //clear the fragment back stack
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else super.onBackPressed();
    }

    @Override
    public void onMovieSelected(Bundle bundle) {
        //movie item has been selected
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);
        if (TWO_PANE){
            getFragmentManager().
                    beginTransaction().replace(R.id.fl_movie_detail_container, detailsFragment).commit();
        }
        else {
            getFragmentManager().
                    beginTransaction().replace(R.id.flContainer, detailsFragment).
                    addToBackStack(DETAILFRAGMENT_TAG).commit();
        }
    }

}
