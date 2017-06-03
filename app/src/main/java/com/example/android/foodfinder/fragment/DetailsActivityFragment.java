package com.example.android.foodfinder.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.foodfinder.R;
import com.example.android.foodfinder.adapter.RestaurantImageSlider;
import com.example.android.foodfinder.adapter.ReviewsAdapter;
import com.example.android.foodfinder.model.PlaceDetails;
import com.example.android.foodfinder.model.PlaceDetailsResponse;
import com.example.android.foodfinder.model.PlacePhoto;
import com.example.android.foodfinder.model.RestaurantTimings;
import com.example.android.foodfinder.model.Review;
import com.example.android.foodfinder.rest.ApiClient;
import com.example.android.foodfinder.rest.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    private static final String LOG_TAG = "DetailsActivityFragment";

    private static Context mContext;

    public DetailsActivityFragment() {
    }

    /*@BindView(R.id.restaurant_img)
    ImageView restaurantImgView;*/

    @BindView(R.id.restaurant_name)
    TextView restaurantTxtView;

    @BindView(R.id.rest_address)
    TextView restAddTxtView;

    @BindView(R.id.rest_phone)
    TextView restPhoneTxtView;

    @BindView(R.id.hours)
    TextView restHoursTxtView;

    @BindView(R.id.rating)
    TextView ratingTxtView;

    @BindView(R.id.ratingbar)
    RatingBar ratingBarView;

    @BindView(R.id.isOpen)
    TextView status;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolBarLayout;

    ViewPager viewPager;
    RestaurantImageSlider restaurantImageSlider;
    private static Call<PlaceDetailsResponse> callDetails = null;
    ReviewsAdapter reviewAdapter;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLinearLayoutManager;
    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        ButterKnife.bind(this, rootView);

        Intent intent = getActivity().getIntent();

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_container);


        mCollapsingToolBarLayout.setTitle(getString(R.string.collapsing_toolbar_title));

        //check if any data received via intent
        if (intent != null && intent.hasExtra("placeId")) {
            String placeId = intent.getStringExtra("placeId");
            if (isOnline()) {
                setUpDetailsUI(placeId);
            } else {
                //no network connectivity
                Toast.makeText(getActivity(), getActivity().getString(R.string.error_no_network), Toast.LENGTH_LONG).show();
            }

        } else {
           // Log.e(LOG_TAG, "Error ! Place Id not received in intent");
            Bundle arguments = getArguments();
            if (arguments != null) {
                String placeId = arguments.getString("placeId");
                if (isOnline()) {
                    setUpDetailsUI(placeId);
                } else {
                    //no network connectivity
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_no_network), Toast.LENGTH_LONG).show();
                }

            }
        }



        viewPager = (ViewPager) rootView.findViewById(R.id.pager);

       /* mToolbar = (Toolbar) rootView.findViewById(R.id.detail_view_toolbar);

        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });
        }*/

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Some sample text")
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        return rootView;
    }

    /**
     * Method to check internet connectivity
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public void setUpDetailsUI(String placeId) {
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        callDetails = apiService.restaurantDetails(placeId, getString(R.string.api_key));


        if (callDetails != null) {
            callDetails.enqueue(new retrofit2.Callback<PlaceDetailsResponse>() {
                @Override
                public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {

                    PlaceDetails placeDetails = response.body().getResult();
                    if (placeDetails != null) {
                        restaurantTxtView.setText(placeDetails.getName());
                        ratingTxtView.setText(String.valueOf(placeDetails.getRating()));
                        ratingBarView.setRating(placeDetails.getRating());

                        //String address = "Address: " + placeDetails.getFormatted_address();
                        restAddTxtView.setText(placeDetails.getFormatted_address());
                        restPhoneTxtView.setText(placeDetails.getFormatted_phone_number());

                        List<PlacePhoto> placePhotos = placeDetails.getPhotos();

                        if (placePhotos != null) {

                            restaurantImageSlider = new RestaurantImageSlider(getActivity(), placePhotos);
                            if (viewPager != null) {
                                viewPager.setAdapter(restaurantImageSlider);
                                viewPager.setOffscreenPageLimit(5);
                            } else {
                                Log.e(LOG_TAG, "viewPager is null");
                            }
                        } else {
                            Log.e(LOG_TAG, "placePhotos is null");
                        }

                        RestaurantTimings timings = placeDetails.getOpening_hours();

                        if (timings != null) {
                            if (timings.isOpen_now()) {
                                status.setText(getActivity().getString(R.string.status_open));
                                status.setTextColor(Color.GREEN);
                            } else {
                                status.setText(getActivity().getString(R.string.status_close));
                                status.setTextColor(Color.RED);
                            }

                            List<String> dayTime = timings.getWeekday_text();
                            Log.i(LOG_TAG, "fetched list" + dayTime.toString());
                            if (dayTime != null && dayTime.size() > 0) {
                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                Date d = new Date();
                                String dayOfTheWeek = sdf.format(d);
                                //Log.i(LOG_TAG , "today date " + dayOfTheWeek);
                                Iterator<String> it = dayTime.iterator();
                                while (it.hasNext()) {
                                    String day = it.next();
                                    //Log.i(LOG_TAG , "element" + day);
                                    String[] splitString = day.split(":");
                                    Log.i(LOG_TAG, splitString.toString());
                                    if (dayOfTheWeek.equalsIgnoreCase(splitString[0])) {
                                        restHoursTxtView.setText(day);
                                        break;
                                    }
                                }

                            }
                        }

                        List<Review> reviews = placeDetails.getReviews();

                        if (reviews != null) {
                            ReviewsAdapter reviewAdapter = new ReviewsAdapter(reviews, getActivity());
                            mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            mRecyclerView.setLayoutManager(mLinearLayoutManager);
                            mRecyclerView.setAdapter(reviewAdapter);

                        }
                    }
                }


                @Override
                public void onFailure(Call<PlaceDetailsResponse> call, Throwable t) {
                    Log.e(LOG_TAG, t.toString());
                }
            });
        }

    }

}


