package com.design.vikas.popularmovies.fragment;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.design.vikas.popularmovies.BuildConfig;
import com.design.vikas.popularmovies.R;
import com.design.vikas.popularmovies.adapter.ReviewAdapter;
import com.design.vikas.popularmovies.data.ContractMovie;
import com.design.vikas.popularmovies.data.MovieContentProvider;
import com.design.vikas.popularmovies.interfaces.MoviesInterface;
import com.design.vikas.popularmovies.model.ResponseDetailModel;
import com.design.vikas.popularmovies.model.ResponseReview;
import com.design.vikas.popularmovies.model.ResponseReviewDatum;
import com.design.vikas.popularmovies.model.ResponseTrailer;
import com.design.vikas.popularmovies.model.ResponseTrailersDatum;
import com.design.vikas.popularmovies.util.WebUrl;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vikas kumar on 6/21/2016.
 */
public class DetailsFragment extends Fragment {

    String title, desc, release, rating, image;
    int id;
    static final String INTENT_CHOOSER = "Choose player";
    static final String TAG_PARCEL = "movie_data";

    static final String KEY_ID = "movie_id";
    static final String KEY_NAME = "movie_name";
    static final String KEY_DESC = "movie_description";
    static final String KEY_RATING = "movie_rating";
    static final String KEY_IMAGE = "movie_image";
    static final String KEY_DATE = "movie_date";

    ReviewAdapter reviewAdapter;
    List<ResponseTrailersDatum> trailersDatums;
    List<ResponseReviewDatum> reviewDatums = new ArrayList<>();

    Retrofit retrofit;
    MoviesInterface moviesInterface;

    ContentValues contentValues;

    @BindView(R.id.ivDetailsMovieImage)
    ImageView ivDetailsMovieImage;
    @BindView(R.id.tvDetailsTitle)
    TextView tvDetailsTitle;
    @BindView(R.id.tvDetailsDate)
    TextView tvDetailsDate;
    @BindView(R.id.tvDetailsDesc)
    TextView tvDetailsDesc;
    @BindView(R.id.tvDetailsRating)
    TextView tvDetailsRating;
    @BindView(R.id.tvDetailsTrailer720)
    TextView tvDetailsTrailer720;
    @BindView(R.id.llDetailsTrailer1)
    LinearLayout llDetailsTrailer1;
    @BindView(R.id.tvDetailsTrailer1080)
    TextView tvDetailsTrailer1080;
    @BindView(R.id.llDetailsTrailer2)
    LinearLayout llDetailsTrailer2;
    @BindView(R.id.btDetailsShowReviews)
    AppCompatButton btDetailsShowReviews;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.rvDetails)
    RecyclerView rvDetails;
    @BindView(R.id.flReviewContent)
    FrameLayout flReviewContent;
    @BindView(R.id.fabFav)
    FloatingActionButton fabFav;

    boolean isFavMarked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData(savedInstanceState);
        initializeRetrofit();
        contentValues  = new ContentValues();
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void setRecyclerview() {
        //set the recyclerview here
        reviewAdapter = new ReviewAdapter(reviewDatums);
        rvDetails.setHasFixedSize(true);
        rvDetails.setLayoutManager(new LinearLayoutManager(getActivity()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        rvDetails.setAdapter(reviewAdapter);
        final int position = reviewAdapter.getItemCount()-1;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setData();
    }

    @Override
    public void onStart() {
        super.onStart();
        //call for the trailers data
        fetchTrailer();
    }

    private void fetchTrailer() {
        //call here
        Call<ResponseTrailer> trailerCall = moviesInterface.getTrailerResponse(id, BuildConfig.APIKEY);
        trailerCall.enqueue(new Callback<ResponseTrailer>() {
            @Override
            public void onResponse(Call<ResponseTrailer> call, Response<ResponseTrailer> response) {
                //on successful communication
                if (response.code() == 200) {

                    trailersDatums = response.body().getResults();
                    int size = trailersDatums.size();
                    if (size > 1) {
                        tvDetailsTrailer720.setText(trailersDatums.get(0).getSite() + " " + trailersDatums.get(0).getSize());
                        tvDetailsTrailer1080.setText(trailersDatums.get(1).getSite() + " " + trailersDatums.get(1).getSize());
                    } else if (size == 1) {
                        tvDetailsTrailer720.setText(trailersDatums.get(0).getSite() + " " + trailersDatums.get(0).getSize());
                        llDetailsTrailer2.setVisibility(View.GONE);
                    }


                } else {
                    //for unknown reasons if any connection failure
                    connectionFailureTrailer();
                }
            }

            @Override
            public void onFailure(Call<ResponseTrailer> call, Throwable t) {
                //for unknown reasons if any connection failure
                connectionFailureTrailer();
            }
        });

    }

    public void initializeRetrofit(){
        //initialise the retrofit here
        retrofit = new Retrofit.Builder().
                baseUrl(WebUrl.URL).
                addConverterFactory(GsonConverterFactory.create()).
                build();

        moviesInterface = retrofit.create(MoviesInterface.class);
    }

    public void connectionFailureTrailer(){
        Toast.makeText(getActivity(),R.string.toast_connection_failure,Toast.LENGTH_SHORT).show();
        llDetailsTrailer1.setClickable(false);
        llDetailsTrailer2.setClickable(false);
    }

    private void setData() {
        //set the received data here
        tvDetailsRating.setText(rating);
        tvDetailsTitle.setText(title);
        tvDetailsDate.setText(release);
        tvDetailsDesc.setText(desc);
        Picasso.with(getActivity()).
                load(WebUrl.URL_IMAGE + image).
                placeholder(R.drawable.placeholder).into(ivDetailsMovieImage);
    }

    private void getData(Bundle bundle) {
        //get bundle data
        bundle = getArguments();
        if (bundle.isEmpty()){

            //empty bundle set nothing
            return;

        }else {
            isFavMarked = bundle.getBoolean(HomeFragment.KEY_FAV);
            ResponseDetailModel detailModel = bundle.getParcelable(HomeFragment.KEY_PARCEL);
            title = detailModel.getOriginalTitle();
            desc = detailModel.getOverview();
            release = detailModel.getReleaseDate();
            rating = String.valueOf(detailModel.getVoteAverage());
            image = detailModel.getPosterPath();
            id = detailModel.getId();
            Log.d(TAG_PARCEL, detailModel.getTitle() + "");
        }

    }

    @OnClick({R.id.llDetailsTrailer1, R.id.llDetailsTrailer2, R.id.btDetailsShowReviews, R.id.fabFav})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.llDetailsTrailer1:
                //construct a uri  or id first to open the youtube or browser, 720 p
                final String id_720 = trailersDatums.get(0).getKey();
                openVideo(id_720);
                break;

                //1080p
            case R.id.llDetailsTrailer2:
                final String id_1080 = trailersDatums.get(0).getKey();
                openVideo(id_1080);
                break;

            //show reviews here
            case R.id.btDetailsShowReviews:
                    fetchReview();
                    progressBar.setVisibility(View.VISIBLE);
                break;

            case R.id.fabFav:
                //insert the data in another thread
                if (isFavMarked){
                    Toast.makeText(getActivity(),R.string.toast_fab,Toast.LENGTH_SHORT).show();
                }else {
                    fabFav.post(new Runnable() {
                        @Override
                        public void run() {
                            //initialise the values to be put
                            contentValues.put(KEY_ID, id);
                            contentValues.put(KEY_NAME, title);
                            contentValues.put(KEY_DATE, release);
                            contentValues.put(KEY_RATING, rating);
                            contentValues.put(KEY_IMAGE, image);
                            contentValues.put(KEY_DESC, desc);

                            //insert into database now
                            getActivity().getContentResolver().insert(ContractMovie.CONTENT_URI, contentValues);

                            if (MovieContentProvider.isSuccess) {
                                fabFav.setImageResource(R.drawable.ic_action_done);
                                Toast.makeText(getActivity(), R.string.toast_fab_success, Toast.LENGTH_SHORT).show();
                                isFavMarked = true;
                            } else {
                                Toast.makeText(getActivity(), R.string.toast_fab_failure, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


                break;
        }
    }

    private void fetchReview() {
        // fetch review here
        Call<ResponseReview> reviewCall = moviesInterface.getReviewResponse(id, BuildConfig.APIKEY);
        reviewCall.enqueue(new Callback<ResponseReview>() {
            @Override
            public void onResponse(Call<ResponseReview> call, Response<ResponseReview> response) {
                //check for response <code></code>
                if (response.code()==200){
                    //communicate with the server was successful
                    reviewDatums = response.body().getResults();
                    progressBar.setVisibility(View.GONE);
                    if (reviewDatums.size()==0){
                        Toast.makeText(getActivity(),R.string.toast_review,Toast.LENGTH_SHORT).show();
                    }else {
                        setRecyclerview();
                    }
                }
                else {
                    //some error may occur
                    Toast.makeText(getActivity(),R.string.toast_msg_data_load_failure,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseReview> call, Throwable t) {
                //some error may occur
                Toast.makeText(getActivity(),R.string.toast_msg_data_load_failure,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openVideo(String id){
        final Uri uri = Uri.parse(WebUrl.URI_YOUTUBE_MOBILE+id);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
        else {
            final Uri uri1 = Uri.parse(WebUrl.URI_YOUTUBE+id);
            startActivity(intent.createChooser(new Intent(Intent.ACTION_VIEW, uri1), INTENT_CHOOSER));
        }
    }
}
