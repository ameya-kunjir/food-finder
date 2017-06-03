package com.example.android.foodfinder.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ak00481127 on 5/10/2017.
 */

public class PlaceDetails {

    String formatted_address;
    String formatted_phone_number;
    RestaurantTimings opening_hours;
    String place_id;
    List<Review> reviews;
    String website;
    List<PlacePhoto> photos;
    String name;
    float rating;

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlaceDetails() {
        reviews = new ArrayList<Review>();
        photos = new ArrayList<PlacePhoto>();
    }

    public List<PlacePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PlacePhoto> photos) {
        this.photos = photos;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public RestaurantTimings getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(RestaurantTimings opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }


    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }


    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
