package students.polsl.eatnear.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import students.polsl.eatnear.activities.AddRestaurantActivity;
import students.polsl.eatnear.activities.MapActivity;
import students.polsl.eatnear.R;
import students.polsl.eatnear.activities.RestaurantActivity;
import students.polsl.eatnear.adapters.RestaurantsMainAdapter;
import students.polsl.eatnear.model.Restaurant;
import students.polsl.eatnear.retrofit.EatNearClient;
import students.polsl.eatnear.utilities.RetrofitUtils;

import static students.polsl.eatnear.utilities.Consts.BACKEND_URL;

public class NearRestaurantsFragment extends Fragment implements RestaurantsMainAdapter.RestaurantTileListener {
    private Context appContext;
    private RecyclerView mRecyclerView;
    private RestaurantsMainAdapter mRestaurantsMainAdapter;
    private Location mCurrentLocation;
    private FloatingActionButton mMenuActionButton;
    private FloatingActionButton mMapActionButton;
    private FloatingActionButton mAddActionButton;
    private TextView mEmptyList;
    private EatNearClient eatNearClient;

    private boolean mIsFabOpen;

    public NearRestaurantsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(appContext);
        Call<List<Restaurant>> call = eatNearClient.getNearRestaurantsInfo(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), Integer.valueOf(sharedPreferences.getString(appContext.getString(R.string.near_area_key), "1000")));
        new NearRestaurantsFragment.GetAllRestaurantsInfo().execute(call);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
        appContext = getActivity().getApplicationContext();

        mMenuActionButton = rootView.findViewById(R.id.fabMenu);
        mAddActionButton = rootView.findViewById(R.id.fabAdd);
        mMapActionButton = rootView.findViewById(R.id.fabMap);
        mEmptyList = rootView.findViewById(R.id.emptyListTextView);

        eatNearClient = RetrofitUtils.createClient(BACKEND_URL, EatNearClient.class);

        LocationManager locationManager = (LocationManager) appContext.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new NearRestaurantsFragment.MyLocationListener();

        if (ContextCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(appContext, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 10, locationListener);
            mCurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            Toast.makeText(appContext, "No GPS permission", Toast.LENGTH_SHORT).show();
        }

        mRecyclerView = rootView.findViewById(R.id.restaurant_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(appContext));
        mRestaurantsMainAdapter = new RestaurantsMainAdapter(this, appContext);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRestaurantsMainAdapter);
        mMenuActionButton.setOnClickListener(view -> {
            if (!mIsFabOpen) {
                showFabMenu();
            } else {
                closeFabMenu();
            }
        });

        mAddActionButton.setOnClickListener(view -> {
            Intent addRestaurant = new Intent(appContext, AddRestaurantActivity.class);
            startActivity(addRestaurant);
        });

        mMapActionButton.setOnClickListener(view -> {
            Intent startMapActivity = new Intent(appContext, MapActivity.class);
            List<Restaurant> restaurants = null;
            startActivity(startMapActivity);
        });

        return rootView;
    }

    private void showFabMenu() {
        mIsFabOpen = true;
        mAddActionButton.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        mMapActionButton.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFabMenu() {
        mIsFabOpen = false;
        mAddActionButton.animate().translationY(0);
        mMapActionButton.animate().translationY(0);
    }

    @Override
    public void onClickAction(View view, double[] latLong) {
        //Toast.makeText(appContext, "Click registered on tile: " + view.getTag(), Toast.LENGTH_SHORT).show();
        Intent startRestaurantActivity = new Intent(appContext, RestaurantActivity.class);
        startRestaurantActivity.putExtra("location", "" + view.getTag(R.id.addressTextView));
        startRestaurantActivity.putExtra("name", "" + view.getTag(R.id.restaurantNameTextView));
        startRestaurantActivity.putExtra("latitude", latLong[0]);
        startRestaurantActivity.putExtra("longitude", latLong[1]);
        startActivity(startRestaurantActivity);
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(final Location location) {
            mCurrentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
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
        protected void onPostExecute(Response<List<Restaurant>> postResponse) {//all user data available
            List<Restaurant> responseRestaurant = postResponse.body();
            if (responseRestaurant != null) {//there is no response (no user found)
                mRestaurantsMainAdapter.swapData(responseRestaurant);
                if (responseRestaurant.size() == 0) {
                    mEmptyList.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                }
                else {
                    mEmptyList.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                mRecyclerView.setVisibility(View.INVISIBLE);
                mEmptyList.setVisibility(View.VISIBLE);
            }
        }
    }
}
