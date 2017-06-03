package com.example.android.foodfinder.rest;

import com.example.android.foodfinder.model.PlaceDetailsResponse;
import com.example.android.foodfinder.model.RestaurantResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ak00481127 on 5/10/2017.
 */

public interface ApiInterface {

    @GET("nearbysearch/json")
    public Call<RestaurantResponse> restaurantList(@Query("location") String location, @Query("radius") int radius
            , @Query("type") String placeType, @Query("key") String api_key);

    @GET("details/json")
    public Call<PlaceDetailsResponse> restaurantDetails(@Query("placeid") String placeId, @Query("key") String api_key);
}
