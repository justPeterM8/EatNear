package students.polsl.eatnear.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Response;
import students.polsl.eatnear.R;
import students.polsl.eatnear.model.Restaurant;
import students.polsl.eatnear.retrofit.EatNearClient;
import students.polsl.eatnear.utilities.RetrofitUtils;

import static students.polsl.eatnear.utilities.Consts.BACKEND_URL;

public class AddRestaurantActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText mRestaurantNameEditText;
    private EditText mRestaurantAddressEditText;
    private CheckBox mCurrentAddressCheckbox;
    private Button mSubmitButton;
    private EatNearClient eatNearClient;
    private String mRestaurantName;
    private String mRestaurantAddress;
    private String mRestaurantCategory;
    private double mAddressLatitude;
    private double mAddressLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant);
        mRestaurantNameEditText = findViewById(R.id.restaurantNameEditText);
        mRestaurantAddressEditText = findViewById(R.id.restaurantAddressEditText);
        mCurrentAddressCheckbox = findViewById(R.id.addressCheckBox);
        mSubmitButton = findViewById(R.id.reviewSubmitButton);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        //retrofit
        eatNearClient = RetrofitUtils.createClient(BACKEND_URL, EatNearClient.class);

        ArrayAdapter<CharSequence> dataAdapter = ArrayAdapter.createFromResource(this,
                R.array.food_category, R.layout.spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new AddRestaurantActivity.MyLocationListener();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, locationListener);
        } else {
            Toast.makeText(this, "No GPS permission", Toast.LENGTH_SHORT).show();
        }

        mCurrentAddressCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked){
                mRestaurantAddressEditText.setFocusable(false);
                mRestaurantAddressEditText.setText("Waiting for GPS signal...");
            } else{
                mRestaurantAddressEditText.setFocusableInTouchMode(true);
                mRestaurantAddressEditText.getText().clear();
                mRestaurantAddressEditText.setHint("Address");
            }
        });
    }

    public void onClickRestaurant(View view){
        if ( (!TextUtils.isEmpty(mRestaurantNameEditText.getText().toString())) && (!TextUtils.isEmpty(mRestaurantAddressEditText.getText().toString())) ) {
            if (!mCurrentAddressCheckbox.isChecked()) {
                if (!getAddressCoordinates()) {
                    Toast.makeText(this, "Given address is wrong.", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else{
                if (mRestaurantAddressEditText.getText().toString().equals("Waiting for GPS signal...")){
                    Toast.makeText(this, "Waiting for GPS signal...", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            mRestaurantName = mRestaurantNameEditText.getText().toString();
            mRestaurantAddress = mRestaurantAddressEditText.getText().toString();

            Restaurant restaurantToSave = new Restaurant(mRestaurantName, mAddressLatitude, mAddressLongitude, mRestaurantAddress, mRestaurantCategory);
            Call<Void> callEatNear = eatNearClient.createRestaurant(restaurantToSave);
            new AddRestaurantActivity.RestaurantCreationTask().execute(callEatNear);
            finish();
        } else
            Toast.makeText(this, "Wrong data. Please, fill all fields.", Toast.LENGTH_LONG).show();
    }

    private boolean getAddressCoordinates(){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(mRestaurantAddressEditText.getText().toString(), 1);
            if (addresses.size() > 0) {
                mAddressLatitude = addresses.get(0).getLatitude();
                mAddressLongitude = addresses.get(0).getLongitude();
                //Log.i("AddRestaurantActivity", "" + mAddressLatitude + " , " + mAddressLongitude);
                return true;
            } else
                return false;
        } catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mRestaurantCategory = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(final Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            List<Address> addresses = null;

            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
                addresses = gcd.getFromLocation(latitude, longitude, 1);
            } catch (IOException e){
                e.printStackTrace();
            }

            if (mCurrentAddressCheckbox.isChecked() && addresses != null) {
                mRestaurantAddressEditText.setText(addresses.get(0).getThoroughfare() + " " + addresses.get(0).getSubThoroughfare() + ", " + addresses.get(0).getLocality());
                mAddressLongitude = longitude;
                mAddressLatitude = latitude;
            }
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

    @Override
    protected void onResume() {
        super.onResume();

        if (!isNetworkConnected()){
            Toast.makeText(this, "This app requires Internet connection", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WelcomeActivity.class));
        }

        if (!isGpsActive()){
            Toast.makeText(this, "This app requires active GPS", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, WelcomeActivity.class));
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private boolean isGpsActive(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public class RestaurantCreationTask extends AsyncTask<Call<Void>, Void, Response<Void>> {
        @Override
        protected Response<Void> doInBackground(Call<Void>[] calls) {
            Response<Void> response = null;
            try {
                response = calls[0].execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response<Void> postResponse) {//all user data available
            postResponse.body();
            if (postResponse.isSuccessful()) {
                Toast.makeText(AddRestaurantActivity.this, "Restaurant added successfully", Toast.LENGTH_SHORT).show();
            } else//no such restaurant (what would be weird, because we add review comming from restaurant's activity)
                Toast.makeText(AddRestaurantActivity.this, "Restaurant creation failed, try again ", Toast.LENGTH_LONG).show();
        }
    }
}
