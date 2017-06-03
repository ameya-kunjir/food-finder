package com.example.android.foodfinder.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.android.foodfinder.R;
import com.example.android.foodfinder.model.PlacePhoto;

import java.util.List;

/**
 * Created by ameya on 5/21/2017.
 */
public class RestaurantImageSlider extends PagerAdapter {

    List<PlacePhoto> mPhotos;
    Context context;
    private static final String PHOTO_BASE_URL = "https://maps.googleapis.com/maps/api/place/photo";
    private final String LOG_TAG = "RESTAURANT_IMAGE_SLIDER";
    private static int desiredHeight;


    public RestaurantImageSlider(Context con, List<PlacePhoto> photoList) {
        mPhotos = photoList;
        context = con;
        desiredHeight = Math.round(dipToPixels(con, 200));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View rootView = LayoutInflater.from(container.getContext())
                .inflate(R.layout.pager_item, container, false);

        ImageView imgView = (ImageView) rootView.findViewById(R.id.restImg);

        PlacePhoto placePhoto = mPhotos.get(position);

        StringBuilder urlBuilder = new StringBuilder(PHOTO_BASE_URL);
        urlBuilder.append("?");
        //urlBuilder.append("maxwidth="+desiredHeight+"&");
        urlBuilder.append("maxheight=" + desiredHeight + "&");
        urlBuilder.append("photoreference=" + placePhoto.getPhoto_reference() + "&");
        urlBuilder.append("key=" + context.getString(R.string.api_key));

        Glide.with(context).load(urlBuilder.toString()).into(imgView);

        container.addView(rootView);

        return rootView;
    }

    @Override
    public int getCount() {
        return mPhotos.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (LinearLayout) object;
    }

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }
}
