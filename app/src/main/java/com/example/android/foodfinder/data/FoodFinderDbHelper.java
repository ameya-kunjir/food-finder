package com.example.android.foodfinder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ak00481127 on 5/11/2017.
 */

public class FoodFinderDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "restaurant.db";

    public FoodFinderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createTableRestaurantScript());

        // db.execSQL(createTablePhotoScript());

        db.execSQL(createTableDetailScript());

        db.execSQL(createTableReviewScript());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FoodFinderContract.RestaurantEntry.TABLE_NAME);
        // db.execSQL("DROP TABLE IF EXISTS " + FoodFinderContract.PhotoEntry.TABLE_NAME );
        db.execSQL("DROP TABLE IF EXISTS " + FoodFinderContract.DetailsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FoodFinderContract.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }

    public String createTableRestaurantScript() {
        /*final String SQL_CREATE_RESTAURANT_TABLE = "CREATE TABLE " + FoodFinderContract.RestaurantEntry.TABLE_NAME
                + " ( " + FoodFinderContract.RestaurantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodFinderContract.RestaurantEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, "+
                FoodFinderContract.RestaurantEntry.COLUMN_RESTAURANT_NAME + " TEXT NOT NULL, " +
                FoodFinderContract.RestaurantEntry.COLUMN_RATING + " REAL NOT NULL, " +
                FoodFinderContract.RestaurantEntry.COLUMN_REFERENCE + " TEXT NOT NULL );";

        return SQL_CREATE_RESTAURANT_TABLE;*/


        final String SQL_CREATE_RESTAURANT_TABLE = "CREATE TABLE " + FoodFinderContract.RestaurantEntry.TABLE_NAME
                + " ( " + FoodFinderContract.RestaurantEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodFinderContract.RestaurantEntry.COLUMN_RESTAURANT_NAME + " TEXT NOT NULL, " +
                FoodFinderContract.RestaurantEntry.COLUMN_RATING + " REAL NOT NULL, " +
                FoodFinderContract.RestaurantEntry.COLUMN_REFERENCE + " TEXT NOT NULL, " +
                FoodFinderContract.RestaurantEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                FoodFinderContract.RestaurantEntry.COLUMN_PHOTO_REFERENCE + " TEXT NOT NULL, " +
                FoodFinderContract.RestaurantEntry.COLUMN_WIDTH + " INTEGER NOT NULL, " +
                FoodFinderContract.RestaurantEntry.COLUMN_HEIGHT + " INTEGER NOT NULL );";

        return SQL_CREATE_RESTAURANT_TABLE;
    }

    /*public String createTablePhotoScript()
    {
        final String SQL_CREATE_PHOTO_TABLE = "CREATE TABLE " + FoodFinderContract.PhotoEntry.TABLE_NAME +
                " ( " + FoodFinderContract.PhotoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodFinderContract.PhotoEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                FoodFinderContract.PhotoEntry.COLUMN_HEIGHT + " INTEGER NOT NULL, " +
                FoodFinderContract.PhotoEntry.COLUMN_WIDTH + " INTEGER NOT NULL, " +
                FoodFinderContract.PhotoEntry.COLUMN_REFERENCE + " TEXT NOT NULL);";

        return SQL_CREATE_PHOTO_TABLE;
    }*/

    public String createTableDetailScript() {
        final String SQL_CREATE_DETAIL_TABLE = "CREATE TABLE " + FoodFinderContract.DetailsEntry.TABLE_NAME +
                " ( " + FoodFinderContract.DetailsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodFinderContract.DetailsEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                FoodFinderContract.DetailsEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                FoodFinderContract.DetailsEntry.COLUMN_CONTACT_NO + " TEXT NOT NULL, " +
                FoodFinderContract.DetailsEntry.COLUMN_TIMINGS + " TEXT NOT NULL, " +
                FoodFinderContract.DetailsEntry.COLUMN_WEBSITE + " TEXT NOT NULL );";

        return SQL_CREATE_DETAIL_TABLE;
    }

    public String createTableReviewScript() {
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + FoodFinderContract.ReviewEntry.TABLE_NAME +
                " ( " + FoodFinderContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodFinderContract.ReviewEntry.COLUMN_PLACE_ID + " TEXT NOT NULL, " +
                FoodFinderContract.ReviewEntry.COLUMN_AUTHOR_NAME + " TEXT NOT NULL, " +
                FoodFinderContract.ReviewEntry.COLUMN_PROFILE_PHOTO_URL + " TEXT NOT NULL, " +
                FoodFinderContract.ReviewEntry.COLUMN_RATING + " REAL NOT NULL, " +
                FoodFinderContract.ReviewEntry.COLUMN_RELATIVE_TIME_DESCRIPTION + " TEXT NOT NULL, " +
                FoodFinderContract.ReviewEntry.COLUMN_TEXT + " TEXT NOT NULL );";
        return SQL_CREATE_REVIEW_TABLE;
    }
}
