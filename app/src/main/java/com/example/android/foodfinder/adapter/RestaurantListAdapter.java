package com.example.android.foodfinder.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.android.foodfinder.R;
import com.example.android.foodfinder.data.FoodFinderContract;

/**
 * Created by ameya on 5/8/2017.
 */
public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    Activity mActivity;
    Cursor mCursor;
    ClickListener clickListener;
    private static final String PHOTO_BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    private static final String LOG_TAG = "REST_LIST_ADAPTER";
    private static final int desiredThumbnailImageHeight = 150;
    private int desiredHeight;


    public RestaurantListAdapter(Cursor cursor, Activity activity, ClickListener click) {
        mActivity = activity;
        mCursor = cursor;
        this.clickListener = click;
        desiredHeight = Math.round(dipToPixels(mActivity, desiredThumbnailImageHeight));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        mCursor.moveToPosition(position);
        //holder.restaurantImage.setImageResource(mImageList.get(position));
        /*Log.i(LOG_TAG , mCursor.getString(
                mCursor.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_RESTAURANT_NAME)));*/
        holder.txtRestaurantName.setText(mCursor.getString(
                mCursor.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_RESTAURANT_NAME)));

        /*Log.i(LOG_TAG , String.valueOf(mCursor.getFloat(mCursor.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_RATING))));*/

        float rating = mCursor.getFloat(mCursor.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_RATING));

        String photoReference = mCursor.getString(
                mCursor.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_PHOTO_REFERENCE));

        /*int maxWidth = mCursor.getInt(
                mCursor.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_HEIGHT));

        int maxHeight = mCursor.getInt(
                mCursor.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_WIDTH));*/

        //Log.i(LOG_TAG , "height = " + desiredHeight + "Original height" + maxHeight);

        StringBuilder urlBuilder = new StringBuilder(PHOTO_BASE_URL);
        urlBuilder.append("?");
        //urlBuilder.append("maxwidth="+maxWidth+"&");
        urlBuilder.append("maxheight=" + desiredHeight + "&");
        urlBuilder.append("photoreference=" + photoReference + "&");
        urlBuilder.append("key=" + mActivity.getString(R.string.api_key));

        //Log.i(LOG_TAG , urlBuilder.toString());

        holder.ratingBar.setRating(rating);
        //Picasso.with(mActivity).load(urlBuilder.toString()).into(holder.restaurantImage);

        Glide.clear(holder.restaurantImage);

        Glide.with(holder.itemView.getContext())
                .load(urlBuilder.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .listener(new RequestListener<String, GlideDrawable>() {
                              @Override
                              public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(GlideDrawable resource, String model,
                                                             Target<GlideDrawable> target,
                                                             boolean isFromMemoryCache, boolean isFirstResource) {
                                  Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                                  Palette palette = Palette.generate(bitmap);
                                  int defaultColor = 0xFF333333;
                                  int color = palette.getDarkMutedColor(defaultColor);
                                  holder.cardView.setBackgroundColor(color);
                                  return false;
                              }
                          }
                ).into(holder.restaurantImage);


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_view, parent, false);
        final ViewHolder viewHolder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
                                 @Override
                                 public void onClick(View view) {

                                     if (clickListener != null) {
                                         clickListener.onItemClick(view, viewHolder.getAdapterPosition());
                                     }
                                     /*Intent intent = new Intent(mActivity , DetailsActivity.class);
                                     mActivity.startActivity(intent);*/
                                 }
                             }
        );

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView restaurantImage;
        TextView txtRestaurantName;
        RatingBar ratingBar;
        CardView cardView;

        public ViewHolder(View view) {
            super(view);
            restaurantImage = (ImageView) view.findViewById(R.id.thumbnail);
            txtRestaurantName = (TextView) view.findViewById(R.id.rest_name);
            ratingBar = (RatingBar) view.findViewById(R.id.ratings);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.onItemClick(v, getAdapterPosition());
            }

        }
    }


    public interface ClickListener {
        void onItemClick(View view, int pos);
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

}




