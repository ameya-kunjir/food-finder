package com.example.android.foodfinder.sync;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.foodfinder.R;
import com.example.android.foodfinder.data.FoodFinderContract;
import com.example.android.foodfinder.model.PlaceDetails;
import com.example.android.foodfinder.model.PlacePhoto;
import com.example.android.foodfinder.model.Restaurant;
import com.example.android.foodfinder.model.RestaurantResponse;
import com.example.android.foodfinder.rest.ApiClient;
import com.example.android.foodfinder.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by ameya on 5/13/2017.
 */
public final class PlacesPullJob {

    private static final int RADIUS = 2000;
    private static final String TYPE = "restaurant";
    private static final String LOG_TAG = "PLACESPULLJOB";
    private static Call<RestaurantResponse> call = null;
    private static final String ACTION_DATA_UPDATED = "com.example.android.foodfinder.ACTION_DATA_UPDATED";
    //private static Call<PlaceDetailsResponse> callDetails = null;
    private static Context mContext;
    private static List<Restaurant> mRestaurantList;
    private static List<PlaceDetails> mPlaceDtailsList;

    public static String getActionDataUpdated() {
        return ACTION_DATA_UPDATED;
    }

    public PlacesPullJob() {
    }

    public static void getNearByPlaces(Context context, String location) {
        mContext = context;

        dataCleanUp();

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        call = apiService.restaurantList(location, RADIUS, TYPE, context.getString(R.string.api_key));

        if (call != null) {
            call.enqueue(new retrofit2.Callback<RestaurantResponse>() {
                @Override
                public void onResponse(Call<RestaurantResponse> call, Response<RestaurantResponse> response) {
                    if (response != null) {
                        List<Restaurant> restaurantList = response.body().getResults();

                        Log.i(LOG_TAG, String.valueOf(restaurantList.size()));
                        if (restaurantList == null || restaurantList.size() == 0) {
                            Log.i(LOG_TAG, "Failed to retrieve required data");
                        } else {
                            //bulk insert in restaurants table
                            insertRestaurantData(restaurantList);
                        }

                    } else {
                        Log.e(LOG_TAG, "Result object is null");
                    }
                }

                @Override
                public void onFailure(Call<RestaurantResponse> call, Throwable throwable) {
                    Log.e(LOG_TAG, throwable.toString());
                }
            });

        }
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED);
        mContext.sendBroadcast(dataUpdatedIntent);


    }

    public static void insertRestaurantData(List<Restaurant> restaurantList) {
        if (restaurantList != null && restaurantList.size() > 0) {
            Iterator<Restaurant> it = restaurantList.iterator();
            ArrayList<ContentValues> restaurantCVs = new ArrayList<>();

            while (it.hasNext()) {
                Restaurant restaurant = it.next();
                ContentValues restaurantCV = new ContentValues();
                restaurantCV.put(FoodFinderContract.RestaurantEntry.COLUMN_RESTAURANT_NAME, restaurant.getName());
                restaurantCV.put(FoodFinderContract.RestaurantEntry.COLUMN_RATING, restaurant.getRating());
                restaurantCV.put(FoodFinderContract.RestaurantEntry.COLUMN_PLACE_ID, restaurant.getPlace_id());
                restaurantCV.put(FoodFinderContract.RestaurantEntry.COLUMN_REFERENCE, restaurant.getReference());


                List<PlacePhoto> placePhotos = restaurant.getPhotos();
                if (placePhotos != null) {
                    restaurantCV.put(FoodFinderContract.RestaurantEntry.COLUMN_WIDTH, placePhotos.get(0).getWidth());
                    restaurantCV.put(FoodFinderContract.RestaurantEntry.COLUMN_HEIGHT, placePhotos.get(0).getHeight());
                    restaurantCV.put(FoodFinderContract.RestaurantEntry.COLUMN_PHOTO_REFERENCE,
                            placePhotos.get(0).getPhoto_reference());

                } else {
                    continue;
                }
                restaurantCVs.add(restaurantCV);
            }

            int rowsInserted = mContext.getContentResolver().bulkInsert(FoodFinderContract.RestaurantEntry.CONTENT_URI,
                    restaurantCVs.toArray(new ContentValues[restaurantCVs.size()]));

            Log.i(LOG_TAG, "Data insertion successful for rows :" + rowsInserted);


        } else {
            //handle error case when Restaurant object is null
        }

    }


    /*public static void retrievePlaceDetailsData() {
        if (mRestaurantList != null) {
            Iterator<Restaurant> it = mRestaurantList.iterator();
            Restaurant restaurant = null;
            mPlaceDtailsList = new ArrayList<PlaceDetails>();

            ApiInterface apiService =
                    ApiClient.getClient().create(ApiInterface.class);

            while (it.hasNext()) {
                restaurant = it.next();
                if (restaurant != null) {
                    String placeId = restaurant.getPlace_id();

                    callDetails = apiService.restaurantDetails(placeId, mContext.getString(R.string.api_key));

                    if (callDetails != null) {
                        callDetails.enqueue(new retrofit2.Callback<PlaceDetailsResponse>() {
                            @Override
                            public void onResponse(Call<PlaceDetailsResponse> call, Response<PlaceDetailsResponse> response) {
                                PlaceDetails placeDetails = response.body().getResult();
                                if (placeDetails != null) {
                                    mPlaceDtailsList.add(placeDetails);
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
        } else {
            Log.e(LOG_TAG, "Failed to retrieve place details");
        }

    }

    public static void insertPlaceDetails() {
        if (mPlaceDtailsList != null) {
            Iterator<PlaceDetails> iterator = mPlaceDtailsList.iterator();

            PlaceDetails place = null;

            while (iterator.hasNext()) {
                place = iterator.next();

                Log.i(LOG_TAG, place.getFormatted_address());
                Log.i(LOG_TAG, place.getFormatted_phone_number());
                Log.i(LOG_TAG, place.getPlace_id());
                Log.i(LOG_TAG, place.getWebsite());

                RestaurantTimings restaurantTimings = place.getOpening_hours();
                Log.i(LOG_TAG, String.valueOf(restaurantTimings.isOpen_now()));
                Log.i(LOG_TAG, restaurantTimings.getWeekday_text().toString());

            }
        }
    }*/

    public static void insertReviews() {

    }

    public static void dataCleanUp() {
        int rowsDeleted = mContext.getContentResolver().delete(FoodFinderContract.RestaurantEntry.CONTENT_URI, null, null);
        Log.i(LOG_TAG, "Data deletion successful for rows :" + rowsDeleted);
    }

}
