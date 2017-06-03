package com.example.android.foodfinder.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ak00481127 on 5/11/2017.
 */

public class FoodFinderProvider extends ContentProvider {

    private static final int RESTAURANT = 100;
    private static final int PHOTO = 200;
    private static final int PHOTO_WITH_PLACEID = 201;
    private static final int REVIEW = 300;
    private static final int REVIEW_WITH_PLACEID = 301;
    private static final int PLACE_DETAILS = 400;
    private static final int PLACE_DETAILS_WITH_PLACE_ID = 401;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    /* private static final String sPhotoSelection =
             FoodFinderContract.PhotoEntry.TABLE_NAME +
                     "." + FoodFinderContract.PhotoEntry.COLUMN_PLACE_ID + " = ?";*/
    private static final String sDetailsSelection =
            FoodFinderContract.DetailsEntry.TABLE_NAME +
                    "." + FoodFinderContract.DetailsEntry.COLUMN_PLACE_ID + " = ?";
    private static final String sReviewsSelection =
            FoodFinderContract.ReviewEntry.TABLE_NAME +
                    "." + FoodFinderContract.ReviewEntry.COLUMN_PLACE_ID + " = ?";
    private FoodFinderDbHelper mDbHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FoodFinderContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FoodFinderContract.PATH_RESTAURANT, RESTAURANT);
        //matcher.addURI(authority, FoodFinderContract.PATH_PLACE_PHOTOS, PHOTO);
        matcher.addURI(authority, FoodFinderContract.PATH_PLACE_REVIEW, REVIEW);
        matcher.addURI(authority, FoodFinderContract.PATH_PLACE_DETAILS, PLACE_DETAILS);
        matcher.addURI(authority, FoodFinderContract.PATH_PLACE_DETAILS + "/*", PLACE_DETAILS_WITH_PLACE_ID);
        //matcher.addURI(authority, FoodFinderContract.PATH_PLACE_PHOTOS + "/*", PHOTO_WITH_PLACEID);
        matcher.addURI(authority, FoodFinderContract.PATH_PLACE_REVIEW + "/*", REVIEW_WITH_PLACEID);

        return matcher;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RESTAURANT:
                return FoodFinderContract.RestaurantEntry.CONTENT_TYPE;
            /*case PHOTO_WITH_PLACEID:
                return FoodFinderContract.PhotoEntry.CONTENT_ITEM_TYPE;*/
            case REVIEW_WITH_PLACEID:
                return FoodFinderContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case PLACE_DETAILS_WITH_PLACE_ID:
                return FoodFinderContract.DetailsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown Uri:" + uri);
        }
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new FoodFinderDbHelper(getContext());
        return true;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsUpdated = 0;

        switch (sUriMatcher.match(uri)) {
            case RESTAURANT: {
                rowsUpdated = db.update(FoodFinderContract.RestaurantEntry.TABLE_NAME, values
                        , selection, selectionArgs);
                break;
            }
            case REVIEW: {
                rowsUpdated = db.update(FoodFinderContract.ReviewEntry.TABLE_NAME, values
                        , selection, selectionArgs);
                break;
            }
            /*case PHOTO: {
                rowsUpdated = db.update(FoodFinderContract.PhotoEntry.TABLE_NAME, values
                        , selection, selectionArgs);
                break;
            }*/
            case PLACE_DETAILS: {
                rowsUpdated = db.update(FoodFinderContract.DetailsEntry.TABLE_NAME, values
                        , selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            // "restaurant"
            case RESTAURANT:
                retCursor = mDbHelper.getReadableDatabase().query(
                        FoodFinderContract.RestaurantEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            // "photo/*"
           /* case PHOTO_WITH_PLACEID:
                retCursor = getPhotoDetailsForPlaceId(uri, projection);
                break;*/

            // "review/*"
            case REVIEW_WITH_PLACEID:
                retCursor = getReviewsForPlaceId(uri, projection);
                break;

            // "details/*"
            case PLACE_DETAILS_WITH_PLACE_ID:
                retCursor = getPlaceDetailsForPlaceId(uri, projection);
                break;

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);


        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);


        return retCursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted = 0;


        switch (sUriMatcher.match(uri)) {
            case RESTAURANT: {
                rowsDeleted = db.delete(FoodFinderContract.RestaurantEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            /*case PHOTO: {
                rowsDeleted = db.delete(FoodFinderContract.PhotoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }*/

            case REVIEW: {
                rowsDeleted = db.delete(FoodFinderContract.ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }

            case PLACE_DETAILS: {
                rowsDeleted = db.delete(FoodFinderContract.DetailsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri = null;

        switch (sUriMatcher.match(uri)) {
            case RESTAURANT: {
                long _id = db.insert(FoodFinderContract.RestaurantEntry.TABLE_NAME, null, values);

                if (_id > 0) {
                    returnUri = FoodFinderContract.RestaurantEntry.buildRestaurantUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            /*case PHOTO: {
                long _id = db.insert(FoodFinderContract.PhotoEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = FoodFinderContract.PhotoEntry.buildPhotoUri(_id);

                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }*/
            case REVIEW: {
                long _id = db.insert(FoodFinderContract.ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = FoodFinderContract.ReviewEntry.buildReviewUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case PLACE_DETAILS: {
                long _id = db.insert(FoodFinderContract.DetailsEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = FoodFinderContract.DetailsEntry.buildDetailUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Uri returnUri = null;
        int returnCount = 0;

        switch (sUriMatcher.match(uri)) {
            case RESTAURANT: {
                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FoodFinderContract.RestaurantEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                break;
            }
            /*case PHOTO: {

                db.beginTransaction();

                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FoodFinderContract.PhotoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                break;
            }*/
            case REVIEW: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FoodFinderContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                break;
            }
            case PLACE_DETAILS: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(FoodFinderContract.DetailsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown URI: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnCount;
    }


    /*public Cursor getPhotoDetailsForPlaceId(Uri uri, String[] projection) {
        String placeId = FoodFinderContract.getPlaceIdFromUri(uri);
        Cursor cursor;

        cursor = mDbHelper.getReadableDatabase().query(
                FoodFinderContract.PhotoEntry.TABLE_NAME,
                projection,
                sPhotoSelection,
                new String[]{placeId},
                null,
                null,
                null
        );

        return cursor;
    }*/

    public Cursor getReviewsForPlaceId(Uri uri, String[] projection) {
        String placeId = FoodFinderContract.getPlaceIdFromUri(uri);
        Cursor cursor;
        cursor = mDbHelper.getReadableDatabase().query(
                FoodFinderContract.ReviewEntry.TABLE_NAME,
                projection,
                sReviewsSelection,
                new String[]{placeId},
                null,
                null,
                null
        );
        return cursor;
    }

    public Cursor getPlaceDetailsForPlaceId(Uri uri, String[] projection) {
        String placeId = FoodFinderContract.getPlaceIdFromUri(uri);
        Cursor cursor;

        cursor = mDbHelper.getReadableDatabase().query(
                FoodFinderContract.DetailsEntry.TABLE_NAME,
                projection,
                sDetailsSelection,
                new String[]{placeId},
                null,
                null,
                null
        );
        return cursor;
    }
}
