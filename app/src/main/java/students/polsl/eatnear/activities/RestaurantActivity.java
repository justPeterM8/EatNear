package students.polsl.eatnear.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Response;
import students.polsl.eatnear.R;
import students.polsl.eatnear.adapters.RestaurantsMainAdapter;
import students.polsl.eatnear.adapters.ReviewsMainAdapter;
import students.polsl.eatnear.fake_data.FakeReviewDataCreator;
import students.polsl.eatnear.model.Restaurant;
import students.polsl.eatnear.retrofit.EatNearClient;
import students.polsl.eatnear.retrofit.RetrofitUtils;

public class RestaurantActivity extends AppCompatActivity implements OnMapReadyCallback, RestaurantsMainAdapter.RestaurantTileListener {
    private RecyclerView mRecyclerView;
    private ReviewsMainAdapter mRestaurantsMainAdapter;
    private FloatingActionButton mActionButton;
    private TextView mRestaurantNameTextView;
    private TextView mRestaurantLocationTextView;
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);
        mActionButton = findViewById(R.id.restaurant_floating_button);
        mRestaurantLocationTextView = findViewById(R.id.addressTextView);
        mRestaurantNameTextView = findViewById(R.id.restaurantNameTextView);

        Intent intent = getIntent();
        logger.info(intent.getStringExtra("location"));
        mRestaurantLocationTextView.setText(intent.getStringExtra("location"));
        mRestaurantNameTextView.setText(intent.getStringExtra("name"));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRecyclerView = findViewById(R.id.review_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRestaurantsMainAdapter = new ReviewsMainAdapter(this, FakeReviewDataCreator.createReviewFakeDataList(10));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRestaurantsMainAdapter);

        mActionButton.setOnClickListener(view -> {
            Intent startReviewActivity = new Intent(this, ReviewActivity.class);
            startReviewActivity.putExtra("name", intent.getStringExtra("name"));
            startActivity(startReviewActivity);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(50.39850089999999, 18.619241099999954);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title("Restaurant name"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
    }

    @Override
    public void onClickAction(View view) {
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
}
