package com.example.android.foodfinder.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * Created by ameya on 5/13/2017.
 */
public class TestProvider extends AndroidTestCase {

    public void testRestaurantInsert() {
        ContentValues testValues = new ContentValues();
        testValues.put(FoodFinderContract.RestaurantEntry.COLUMN_PLACE_ID, "g7wg73wkufj");
        testValues.put(FoodFinderContract.RestaurantEntry.COLUMN_RATING, 4.2f);
        testValues.put(FoodFinderContract.RestaurantEntry.COLUMN_REFERENCE, "iewvurienioa");
        testValues.put(FoodFinderContract.RestaurantEntry.COLUMN_RESTAURANT_NAME, "sanman");
        testValues.put(FoodFinderContract.RestaurantEntry.COLUMN_HEIGHT, 690);
        testValues.put(FoodFinderContract.RestaurantEntry.COLUMN_WIDTH, 1290);
        testValues.put(FoodFinderContract.RestaurantEntry.COLUMN_PHOTO_REFERENCE, "http://image.url");

        SQLiteDatabase db = new FoodFinderDbHelper(this.mContext).getWritableDatabase();


        long restaurantRowId = db.insertOrThrow(FoodFinderContract.RestaurantEntry.TABLE_NAME, null, testValues);

        assertTrue("Unable to Insert RestaurantEntry into the Database", restaurantRowId != -1);

        String moviesWhereClause = FoodFinderContract.RestaurantEntry._ID + " = ? ";

        Cursor restCursor = mContext.getContentResolver().query(FoodFinderContract.RestaurantEntry.CONTENT_URI
                , null
                , moviesWhereClause
                , new String[]{Long.toString(restaurantRowId)}
                , null);

        TestUtilities.validateCursor("testBasicQuery", restCursor, testValues);
        db.close();
    }

    /*public void testPhotoInsert()
    {
        ContentValues testValues = new ContentValues();
        testValues.put(FoodFinderContract.PhotoEntry.COLUMN_PLACE_ID, "g7wg73wkufj");
        testValues.put(FoodFinderContract.PhotoEntry.COLUMN_HEIGHT, "1240");
        testValues.put(FoodFinderContract.PhotoEntry.COLUMN_REFERENCE, "iewvurienioa");
        testValues.put(FoodFinderContract.PhotoEntry.COLUMN_WIDTH, "1200");

        SQLiteDatabase db = new FoodFinderDbHelper(this.mContext).getWritableDatabase();


        long photoRowId = db.insertOrThrow(FoodFinderContract.PhotoEntry.TABLE_NAME, null, testValues);

        assertTrue("Unable to Insert RestaurantEntry into the Database", photoRowId != -1);

        String moviesWhereClause = FoodFinderContract.PhotoEntry.COLUMN_PLACE_ID + " = ? ";

        Cursor restCursor = mContext.getContentResolver().query(FoodFinderContract.PhotoEntry.buildPhotoUri("g7wg73wkufj")
                , null
                , moviesWhereClause
                , new String[]{"g7wg73wkufj"}
                , null);

        TestUtilities.validateCursor("testBasicQuery", restCursor, testValues);
        db.close();

    }*/


    public void testReviewInsert() {
        ContentValues testValues = new ContentValues();
        testValues.put(FoodFinderContract.ReviewEntry.COLUMN_PLACE_ID, "g7wg73wkufj");
        testValues.put(FoodFinderContract.ReviewEntry.COLUMN_AUTHOR_NAME, "ameya");
        testValues.put(FoodFinderContract.ReviewEntry.COLUMN_PROFILE_PHOTO_URL, "http://asnuirej.jpg");
        testValues.put(FoodFinderContract.ReviewEntry.COLUMN_RATING, "4.2");
        testValues.put(FoodFinderContract.ReviewEntry.COLUMN_RELATIVE_TIME_DESCRIPTION, "2 weeks before");
        testValues.put(FoodFinderContract.ReviewEntry.COLUMN_TEXT, "good restaurant");

        SQLiteDatabase db = new FoodFinderDbHelper(this.mContext).getWritableDatabase();


        long reviewRowId = db.insertOrThrow(FoodFinderContract.ReviewEntry.TABLE_NAME, null, testValues);

        assertTrue("Unable to Insert RestaurantEntry into the Database", reviewRowId != -1);

        String moviesWhereClause = FoodFinderContract.ReviewEntry.COLUMN_PLACE_ID + " = ? ";

        Cursor restCursor = mContext.getContentResolver().query(FoodFinderContract.ReviewEntry.buildReviewUri("g7wg73wkufj")
                , null
                , moviesWhereClause
                , new String[]{"g7wg73wkufj"}
                , null);

        TestUtilities.validateCursor("testBasicQuery", restCursor, testValues);
        db.close();
    }

    public void testDetailsInsert() {
        ContentValues testValues = new ContentValues();
        testValues.put(FoodFinderContract.DetailsEntry.COLUMN_PLACE_ID, "g7wg73wkufj");
        testValues.put(FoodFinderContract.DetailsEntry.COLUMN_CONTACT_NO, "9969664226");
        testValues.put(FoodFinderContract.DetailsEntry.COLUMN_ADDRESS, "Shailesh CHS nerul east");
        testValues.put(FoodFinderContract.DetailsEntry.COLUMN_TIMINGS, "10.00PM to 2.00AM");


        SQLiteDatabase db = new FoodFinderDbHelper(this.mContext).getWritableDatabase();


        long detailRowId = db.insertOrThrow(FoodFinderContract.DetailsEntry.TABLE_NAME, null, testValues);

        assertTrue("Unable to Insert RestaurantEntry into the Database", detailRowId != -1);

        String moviesWhereClause = FoodFinderContract.DetailsEntry.COLUMN_PLACE_ID + " = ? ";

        Cursor restCursor = mContext.getContentResolver().query(FoodFinderContract.DetailsEntry.buildDetailUri("g7wg73wkufj")
                , null
                , moviesWhereClause
                , new String[]{"g7wg73wkufj"}
                , null);

        TestUtilities.validateCursor("testBasicQuery", restCursor, testValues);
        db.close();
    }
}
