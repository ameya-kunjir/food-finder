package com.example.android.foodfinder.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.android.foodfinder.R;
import com.example.android.foodfinder.model.Review;

import java.util.List;

/**
 * Created by ameya on 5/21/2017.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    Context mContext;
    List<Review> mReviews;

    public ReviewsAdapter(List<Review> reviews, Context context) {
        mContext = context;
        mReviews = reviews;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_review, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviews.get(position);
        holder.nameTxtView.setText(review.getAuthor_name());

        holder.review.setText(review.getText());

        Glide.with(holder.itemView.getContext())
                .load(review.getProfile_photo_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.profilePic);

    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic;
        TextView nameTxtView;
        TextView review;

        public ViewHolder(View view) {
            super(view);
            profilePic = (ImageView) view.findViewById(R.id.profile_pic);
            nameTxtView = (TextView) view.findViewById(R.id.name);
            review = (TextView) view.findViewById(R.id.review_text);
        }
    }
}
