package com.design.vikas.popularmovies.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.design.vikas.popularmovies.BuildConfig;
import com.design.vikas.popularmovies.R;
import com.design.vikas.popularmovies.activity.MainActivity;
import com.design.vikas.popularmovies.activity.SettingsFragment;
import com.design.vikas.popularmovies.adapter.ResponseMovieAdapter;
import com.design.vikas.popularmovies.data.ContractMovie;
import com.design.vikas.popularmovies.interfaces.ClickListener;
import com.design.vikas.popularmovies.interfaces.MoviesInterface;
import com.design.vikas.popularmovies.model.ResponseDetailModel;
import com.design.vikas.popularmovies.model.ResponseModel;
import com.design.vikas.popularmovies.util.WebUrl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vikas kumar on 6/15/2016.
 */

/* Restrofit is an open source android networking project used for making network call better way
   hosted here - http://square.github.io/retrofit/
   API used for movies data is - http://api.themoviedb.org
 */

public class HomeFragment extends Fragment {

    ResponseMovieAdapter adapter;
    List<ResponseDetailModel> detailModelsList = new ArrayList<>();
    Retrofit retrofit;
    MoviesInterface moviesInterface;
    String stringSortValue;
    SharedPreferences preferences;
    static String KEY_PARCEL = "myMovieParcel";
    @BindView(R.id.rvMovies)
    RecyclerView rvMovies;
    String bundleValue;
    Cursor cursor;
    public static final String KEY_FAV = "isFav";
    private boolean isFav;

    public HomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.pref, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        stringSortValue = preferences.getString(SettingsFragment.SORT_ORDER, null);
        savedInstanceState = getArguments();
        bundleValue = savedInstanceState.getString(MainActivity.VIEW_TYPE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setupWithRecyclewView();
        setupListener();
        return view;
    }

    private void setupListener() {
        adapter.setOnItemClickListener(new ResponseMovieAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                if (v.getId() == R.id.ivMoviesPoster) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(KEY_FAV, isFav);
                    bundle.putParcelable(KEY_PARCEL, detailModelsList.get(position));
                    ClickListener listener = (ClickListener) getActivity();
                    listener.onMovieSelected(bundle);
                }
                }
            });
    }


    @Override
    public void onResume() {
        super.onResume();
        if (bundleValue.equals("normal")){
            stringSortValue = preferences.getString(SettingsFragment.SORT_ORDER, null);
            makeDataRequest();
        }
        else {
            fetchDataFromDB();
        }

    }

    private void fetchDataFromDB() {
      //first all clear the data which may have list earlier, onResume it will fetch fresh data
        detailModelsList.clear();
       cursor =  getActivity().getContentResolver().query(ContractMovie.CONTENT_URI, null, null, null, null);
       cursor.moveToFirst();
       ResponseDetailModel detailModel ;
       while (!cursor.isAfterLast()){
           detailModel = new ResponseDetailModel();
           detailModel.setId(cursor.getInt(cursor.getColumnIndex(ContractMovie.MovieEntry.COLUMN_MOVIE_ID)));
           detailModel.setOriginalTitle(cursor.getString(cursor.getColumnIndex(ContractMovie.MovieEntry.COLUMN_NAME)));
           detailModel.setReleaseDate(cursor.getString(cursor.getColumnIndex(ContractMovie.MovieEntry.COLUMN_DATE)));
           detailModel.setPosterPath(cursor.getString(cursor.getColumnIndex(ContractMovie.MovieEntry.COLUMN_IMAGE)));
           detailModel.setOverview(cursor.getString(cursor.getColumnIndex(ContractMovie.MovieEntry.COLUMN_DESCRIPTION)));
           detailModel.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(ContractMovie.MovieEntry.COLUMN_RATING)));
           detailModelsList.add(detailModel);
           cursor.moveToNext();
       }
        adapter.notifyDataSetChanged();
        //this will notify the detail view not to activate the fab button
        isFav = true;
    }

    private void makeDataRequest() {
        //network call to movies api goes here
        retrofit = new Retrofit.Builder().
                baseUrl(WebUrl.URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        moviesInterface = retrofit.create(MoviesInterface.class);

        Call<ResponseModel> modelCall = moviesInterface.responseModelCall(stringSortValue, BuildConfig.APIKEY);
        modelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                detailModelsList = response.body().getResults();
                adapter = new ResponseMovieAdapter(detailModelsList, getActivity());
                adapter.notifyDataSetChanged();
                rvMovies.swapAdapter(adapter, false);
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

                Toast.makeText(getActivity(), R.string.toast_msg_data_load_failure, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setupWithRecyclewView() {
        //setting here the recyclew view
        adapter = new ResponseMovieAdapter(detailModelsList, getActivity());
        rvMovies.setHasFixedSize(true);
        rvMovies.setItemAnimator(new DefaultItemAnimator());
         if (MainActivity.TWO_PANE) {
            rvMovies.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvMovies.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        }
            else {
            rvMovies.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        rvMovies.setAdapter(adapter);
    }

}