package com.design.vikas.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.design.vikas.popularmovies.R;
import com.design.vikas.popularmovies.model.ResponseReview;
import com.design.vikas.popularmovies.model.ResponseReviewDatum;

import java.util.List;

/**
 * Created by vikas kumar on 7/30/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{

    List<ResponseReviewDatum> responseReviews;

    public ReviewAdapter(List<ResponseReviewDatum> responseReviews) {
        this.responseReviews = responseReviews;
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_row_review,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        ResponseReviewDatum responseReview = responseReviews.get(position);
        holder.tvReview.setText(responseReview.getContent());
        holder.tvName.setText(responseReview.getAuthor());
    }

    @Override
    public int getItemCount() {
        return responseReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvReview;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView)itemView.findViewById(R.id.tvReviewUserName);
            tvReview = (TextView)itemView.findViewById(R.id.tvReviewDetails);
        }
    }
}
