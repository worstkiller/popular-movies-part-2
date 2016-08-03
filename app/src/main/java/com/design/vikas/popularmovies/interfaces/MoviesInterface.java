package com.design.vikas.popularmovies.interfaces;

import com.design.vikas.popularmovies.model.ResponseModel;
import com.design.vikas.popularmovies.model.ResponseReview;
import com.design.vikas.popularmovies.model.ResponseTrailer;
import com.design.vikas.popularmovies.model.ResponseTrailersDatum;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by vikas kumar on 6/16/2016.
 */
public interface MoviesInterface {

    //movie details
    @GET("/3/movie/{sort}?")
    Call<ResponseModel> responseModelCall(@Path("sort") String sort_by, @Query("api_key") String api_key);

    //movie trailer
    @GET("/3/movie/{id}/videos?")
    Call<ResponseTrailer> getTrailerResponse(@Path("id") int id,@Query("api_key") String api_key);

    //movie review
    @GET("/3/movie/{id}/reviews?")
    Call<ResponseReview> getReviewResponse(@Path("id") int id,@Query("api_key") String api_key);
}
