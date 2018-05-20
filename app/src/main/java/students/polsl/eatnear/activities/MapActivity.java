package students.polsl.eatnear.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import students.polsl.eatnear.R;
import students.polsl.eatnear.model.Restaurant;
import students.polsl.eatnear.retrofit.EatNearClient;
import students.polsl.eatnear.utilities.RetrofitUtils;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    //private GoogleMap mGoogleMap;
    private ProgressBar mProgressBar;
    private EatNearClient eatNearClient;
    private List<Restaurant> restaurants;
    private SupportMapFragment mMapFragment;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mProgressBar = findViewById(R.id.progressBar);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mProgressBar.setVisibility(View.VISIBLE);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //LocationListener locationListener = new MapActivity.MyLocationListener();
        eatNearClient = RetrofitUtils.createClient("http://19e604c7.ngrok.io", EatNearClient.class);


        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 10, locationListener);
            mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            Toast.makeText(this, "No GPS permission", Toast.LENGTH_SHORT).show();
        }

        Call<List<Restaurant>> call = eatNearClient.getAllRestaurantsInfo(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        new GetAllRestaurantsInfo().execute(call);
    }

    private void getMapAsync(){
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng currentLocation = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
            for (Restaurant restaurant : restaurants) {
                LatLng location = new LatLng(restaurant.getLocalizationLatitude(), restaurant.getLocalizationLongitude());
                googleMap.addMarker(new MarkerOptions().position(location)
                        .title(restaurant.getName()));
            }
        //mGoogleMap = googleMap;
        mProgressBar.setVisibility(View.INVISIBLE);
    }

//    private class MyLocationListener implements LocationListener {
//
//        @Override
//        public void onLocationChanged(final Location location) {
////            mProgressBar.setVisibility(View.INVISIBLE);
////            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
////            if (mGoogleMap != null) {
////                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 13));
////            }
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isNetworkConnected()) {
            Toast.makeText(this, "This app requires Internet connection", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        if (!isGpsActive()) {
            Toast.makeText(this, "This app requires active GPS", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private boolean isGpsActive() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public class GetAllRestaurantsInfo extends AsyncTask<Call<List<Restaurant>>, Void, Response<List<Restaurant>>> {
        @Override
        protected Response<List<Restaurant>> doInBackground(Call<List<Restaurant>>[] calls) {
            Response<List<Restaurant>> response = null;
            try {
                response = calls[0].execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response<List<Restaurant>> postResponse) {
            List<Restaurant> responseRestaurant = postResponse.body();
            if (responseRestaurant != null) {
                restaurants = responseRestaurant;
                getMapAsync();
            } else{
                finish();
            }
        }
    }
}
