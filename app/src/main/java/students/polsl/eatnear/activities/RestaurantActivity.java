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
import java.util.List;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Response;
import students.polsl.eatnear.R;
import students.polsl.eatnear.adapters.ReviewsMainAdapter;
import students.polsl.eatnear.model.Review;
import students.polsl.eatnear.retrofit.EatNearClient;
import students.polsl.eatnear.utilities.RetrofitUtils;

public class RestaurantActivity extends AppCompatActivity implements OnMapReadyCallback {
    private RecyclerView mRecyclerView;
    private ReviewsMainAdapter mReviewsMainAdapter;
    private FloatingActionButton mActionButton;
    private TextView mRestaurantNameTextView;
    private TextView mRestaurantLocationTextView;
    private EatNearClient eatNearClient;
    private double locLatitude;
    private double locLongitude;
    private Logger logger = Logger.getLogger(this.getClass().getSimpleName());


    @Override
    public void onStart() {
        super.onStart();
            Call<List<Review>> callEatNear = eatNearClient.getReviewsForSpecificRestaurant(mRestaurantNameTextView.getText().toString());
            new RestaurantActivity.GetAllReviewsTask().execute(callEatNear);
    }

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
        locLatitude = intent.getDoubleExtra("latitude", 0);
        locLongitude = intent.getDoubleExtra("longitude", 0);

        eatNearClient = RetrofitUtils.createClient("http://19e604c7.ngrok.io", EatNearClient.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mRecyclerView = findViewById(R.id.review_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReviewsMainAdapter = new ReviewsMainAdapter(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mReviewsMainAdapter);

        mActionButton.setOnClickListener(view -> {
            Intent startReviewActivity = new Intent(this, AddReviewActivity.class);
            startReviewActivity.putExtra("name", intent.getStringExtra("name"));
            startActivity(startReviewActivity);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(locLatitude, locLongitude);
        googleMap.addMarker(new MarkerOptions().position(location)
                .title(mRestaurantNameTextView.getText().toString()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
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

    public class GetAllReviewsTask extends AsyncTask<Call<List<Review>>, Void, Response<List<Review>>> {
        @Override
        protected Response<List<Review>> doInBackground(Call<List<Review>>[] calls) {
            Response<List<Review>> response = null;
            try{
                response = calls[0].execute();
            }catch (IOException e){
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response<List<Review>> postResponse) {//all user data available
            List<Review> responseReview = postResponse.body();
            if(responseReview != null){//no data available
                mReviewsMainAdapter.swapData(responseReview);
                mRecyclerView.setVisibility(View.VISIBLE);
            }else
                mRecyclerView.setVisibility(View.INVISIBLE);
        }
    }
}
