package com.example.android.foodfinder.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.foodfinder.R;
import com.example.android.foodfinder.data.FoodFinderContract;

/**
 * Created by ameya on 5/22/2017.
 */
public class RestaurantListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private Cursor data = null;
    private Context con = null;
    private int mAppWidgetId;

    private static String[] RestaurantColumns = {
            FoodFinderContract.RestaurantEntry._ID,
            FoodFinderContract.RestaurantEntry.COLUMN_RESTAURANT_NAME,
            FoodFinderContract.RestaurantEntry.COLUMN_RATING,
            FoodFinderContract.RestaurantEntry.COLUMN_PLACE_ID
    };

    RestaurantListRemoteViewFactory(Context context, Intent intent) {
        con = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

        //initialising cursor data
        data = con.getContentResolver().query(FoodFinderContract.RestaurantEntry.CONTENT_URI,
                RestaurantColumns,
                null,
                null,
                null);

    }


    @Override
    public RemoteViews getViewAt(int position) {

        if (position == AdapterView.INVALID_POSITION ||
                data == null || !data.moveToPosition(position)) {
            return null;
        }

        RemoteViews views = new RemoteViews(con.getPackageName(), R.layout.widget_item);

        if (data != null) {
            String restName = data.getString(data.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_RESTAURANT_NAME));

            float rating = data.getFloat(data.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_RATING));

            Resources res = con.getResources();
            String text = res.getString(R.string.widget_rating_txt, restName, String.valueOf(rating));

            views.setTextViewText(R.id.near_rest_name , text );
           // views.setTextViewText(R.id.near_rest_name, restName + " Rating : " + String.valueOf(rating));


        }


        return views;
    }


    @Override
    public int getCount() {
        return data == null ? 0 : data.getCount();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        int item = 0;
        if (data.moveToPosition(position)) {
            item = data.getInt(data.getColumnIndex(FoodFinderContract.RestaurantEntry._ID));
        }
        return item;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
        if (data != null) {
            data.close();
            data = null;
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
