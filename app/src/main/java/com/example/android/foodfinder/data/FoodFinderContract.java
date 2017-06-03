package com.example.android.foodfinder.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ak00481127 on 5/10/2017.
 */

public class FoodFinderContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.foodfinder.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RESTAURANT = "restaurants";

    //public static final String PATH_PLACE_PHOTOS = "photo";

    public static final String PATH_PLACE_DETAILS = "details";

    public static final String PATH_PLACE_REVIEW = "review";

    public static final class RestaurantEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RESTAURANT).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANT;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RESTAURANT;

        public static final String TABLE_NAME = "restaurants";

        public static final String COLUMN_PLACE_ID = "place_id";

        public static final String COLUMN_RESTAURANT_NAME = "rest_name";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_REFERENCE = "reference";

        public static final String COLUMN_PHOTO_REFERENCE = "photo_reference";

        public static final String COLUMN_WIDTH = "width";

        public static final String COLUMN_HEIGHT = "height";

        public static Uri buildRestaurantUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    /*public static final class PhotoEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACE_PHOTOS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE_PHOTOS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE_PHOTOS;

        public static final String TABLE_NAME = "photo";

        public static final String COLUMN_PLACE_ID = "place_id";

        public static final String COLUMN_WIDTH = "width";

        public static final String COLUMN_HEIGHT = "height";

        public static final String COLUMN_REFERENCE = "reference";

        *//* /photo/{place_id}  *//*
        public static Uri buildPhotoUri(String placeId)
        {
            return CONTENT_URI.buildUpon().appendPath(placeId).build();
        }

        public static Uri buildPhotoUri(long id)
        {
            return ContentUris.withAppendedId(CONTENT_URI , id);
        }

    }
*/
    public static final class DetailsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACE_DETAILS).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE_DETAILS;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE_DETAILS;

        public static final String TABLE_NAME = "details";

        public static final String COLUMN_PLACE_ID = "place_id";

        public static final String COLUMN_ADDRESS = "address";

        public static final String COLUMN_CONTACT_NO = "contact";

        public static final String COLUMN_TIMINGS = "timings";

        public static final String COLUMN_WEBSITE = "website";

        /* /place/{place_id}  */
        public static Uri buildDetailUri(String placeId) {
            return CONTENT_URI.buildUpon().appendPath(placeId).build();
        }

        public static Uri buildDetailUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACE_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE_REVIEW;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PLACE_REVIEW;

        public static final String TABLE_NAME = "review";

        public static final String COLUMN_PLACE_ID = "place_id";

        public static final String COLUMN_AUTHOR_NAME = "author_name";

        public static final String COLUMN_PROFILE_PHOTO_URL = "profile_photo";

        public static final String COLUMN_RATING = "rating";

        public static final String COLUMN_RELATIVE_TIME_DESCRIPTION = "time_of_review";

        public static final String COLUMN_TEXT = "text";


        /* /review/{place_id}  */
        public static Uri buildReviewUri(String placeId) {
            return CONTENT_URI.buildUpon().appendPath(placeId).build();
        }

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

    public static String getPlaceIdFromUri(Uri uri) {
        return (uri.getPathSegments().get(1));
    }
}
