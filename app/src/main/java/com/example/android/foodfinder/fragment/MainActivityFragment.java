package com.example.android.foodfinder.fragment;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.foodfinder.R;
import com.example.android.foodfinder.adapter.RestaurantListAdapter;
import com.example.android.foodfinder.data.FoodFinderContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, RestaurantListAdapter.ClickListener {

    public MainActivityFragment() {
    }

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLinearLayoutManager;
    RecyclerView.LayoutManager mGridLayoutManager;
    private static final int MY_LOADER_ID = 0;
    private final int COLUMN_COUNT = 2;
    Cursor mCursor;
    MainActivityFragment mainActivityFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mainActivityFragment = this;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        Uri getRestaurantListForLocation = FoodFinderContract.RestaurantEntry.CONTENT_URI;

        String sortOrder = FoodFinderContract.RestaurantEntry.COLUMN_RATING + " DESC";

        return new CursorLoader(getActivity(),
                getRestaurantListForLocation,
                null,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        RestaurantListAdapter adapter = new RestaurantListAdapter(data, getActivity(), mainActivityFragment);
        mRecyclerView.setAdapter(adapter);
        //mLinearLayoutManager= new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL , false);
        mGridLayoutManager = new GridLayoutManager(getActivity(), COLUMN_COUNT);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mRecyclerView.setAdapter(null);
    }

    @Override
    public void onItemClick(View view, int pos) {
        if (mCursor != null) {
            mCursor.moveToPosition(pos);
            String placeId = mCursor.getString(mCursor.getColumnIndex(FoodFinderContract.RestaurantEntry.COLUMN_PLACE_ID));
            ((Callback) getActivity()).onItemSelected(placeId);
        }
    }

    public interface Callback {
        public void onItemSelected(String placeId);
    }
}
