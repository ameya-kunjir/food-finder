package com.example.android.foodfinder.model;

/**
 * Created by ak00481127 on 5/9/2017.
 */

public class PlaceDetailsResponse {

    PlaceDetails result;
    String status;

    public PlaceDetailsResponse() {

    }

    public PlaceDetails getResult() {
        return result;
    }

    public void setResult(PlaceDetails result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
