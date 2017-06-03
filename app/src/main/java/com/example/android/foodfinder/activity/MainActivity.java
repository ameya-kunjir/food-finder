package com.example.android.foodfinder.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.foodfinder.R;
import com.example.android.foodfinder.fragment.DetailsActivityFragment;
import com.example.android.foodfinder.fragment.MainActivityFragment;
import com.example.android.foodfinder.sync.PlacesPullIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        MainActivityFragment.Callback {


    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private static final String LOG_TAG = "MAIN_ACTIVITY";
    private static Location mLocation;
    private GoogleApiClient mGoogleApiClient;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Intent mServiceIntent;
    private boolean mTwoPane = false;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    @BindView(R.id.error)
    TextView error;

    private static final int MY_FINE_LOCATION_PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            if (isOnline()) {
                Log.i(LOG_TAG, "Network is available");
                if (CheckGooglePlayServices()) {
                    if (mGoogleApiClient == null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(LocationServices.API)
                                .build();
                    }

                } else {
                    //handle error case for Google Play Services not available
                    error.setText(getString(R.string.error_no_network));
                    error.setVisibility(View.VISIBLE);
                }
            } else {
                //handle error case when internet is not available
                error.setText(getString(R.string.error_no_network));
                error.setVisibility(View.VISIBLE);
            }
        }


        if (findViewById(R.id.rest_details_container) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rest_details_container, new DetailsActivityFragment(), DETAILFRAGMENT_TAG).commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    @Override
    protected void onStart() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    /**
     * Method to check internet connectivity
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(LOG_TAG, "Google Play Services Connection Successfull");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


            if (mLocation != null) {
               latitude = mLocation.getLatitude();
               longitude = mLocation.getLongitude();
            }

            if (latitude != 0.0 && longitude != 0.0) {
                String location = String.valueOf(latitude) + "," + String.valueOf(longitude);

                mServiceIntent = new Intent(this, PlacesPullIntentService.class);
                mServiceIntent.putExtra("current_location", location);
                Log.i(LOG_TAG, "Current location" + location);
                this.startService(mServiceIntent);
            } else {
                Log.e(LOG_TAG, "Error occured ! Cannot retrieve current location");
            }

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_FINE_LOCATION_PERMISSION_REQUEST_CODE);

        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Google Play Services Connection Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "Connection failed " + connectionResult.getErrorMessage());
    }


    // check if Google Play Services are available or not
    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_FINE_LOCATION_PERMISSION_REQUEST_CODE:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


                        if (mLocation != null) {
                            latitude = mLocation.getLatitude();
                            longitude = mLocation.getLongitude();

                        }

                        if (latitude != 0.0 && longitude != 0.0) {
                            String location = String.valueOf(latitude) + "," + String.valueOf(longitude);
                            mServiceIntent = new Intent(this, PlacesPullIntentService.class);
                            mServiceIntent.putExtra("current_location", location);
                            this.startService(mServiceIntent);
                        } else {
                            Log.e(LOG_TAG, "Error occured ! Cannot retrieve current location");
                        }


                    }
                } else {
                    // handle error case when location permission is denied
                    error.setText(getString(R.string.error_no_network));
                    error.setVisibility(View.VISIBLE);
                }
        }
    }

    @Override
    public void onItemSelected(String placeId) {
        if (mTwoPane) {
            //two pane layout
            //set all the data for details

            Bundle args = new Bundle();
            args.putString("placeId", placeId);

            DetailsActivityFragment fragment = new DetailsActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.rest_details_container, fragment, DETAILFRAGMENT_TAG).commit();
        } else {
            //one pane layout
            //launch intent for displaying restaurant details on phone
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("placeId", placeId);
            startActivity(intent);
        }
    }
}
