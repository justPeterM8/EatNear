package students.polsl.eatnear.activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import students.polsl.eatnear.R;
import students.polsl.eatnear.model.Review;
import students.polsl.eatnear.retrofit.EatNearClient;
import students.polsl.eatnear.utilities.RetrofitUtils;
import students.polsl.eatnear.utilities.DateUtils;

public class AddReviewActivity extends AppCompatActivity {
    private Button mSubmitButton;
    private RatingBar mRatingBar;
    private EditText mCustomerEditText;
    private EditText mReviewEditText;
    private TextView mRestaurantNameTextView;
    private EatNearClient eatNearClient;
    private double mRating;
    private String mReview;
    private String mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mRatingBar = findViewById(R.id.restaurantRatingBar);
        mCustomerEditText = findViewById(R.id.loginEditText);
        mReviewEditText = findViewById(R.id.descriptionEditView);

        mSubmitButton = findViewById(R.id.reviewSubmitButton);
        mRestaurantNameTextView = findViewById(R.id.restaurantNameTextView);

        //retrofit
        eatNearClient = RetrofitUtils.createClient("http://e72fd782.ngrok.io", EatNearClient.class);

        Intent intent = getIntent();
        mRestaurantNameTextView.setText(intent.getStringExtra("name"));

    }

    public void onClick(View view){
        if (R.id.reviewSubmitButton == view.getId()){
            if ((!TextUtils.isEmpty(mReviewEditText.getText().toString())) && (!TextUtils.isEmpty(mCustomerEditText.getText().toString())) && (mRatingBar.getRating() != 0)) {
                mReview = mReviewEditText.getText().toString();
                mCustomer = mCustomerEditText.getText().toString();
                mRating = mRatingBar.getRating();

                Review reviewToSave = new Review(mReview, mCustomer, mRating, DateUtils.convertDateToString(DateUtils.createTodaysDate()));
                Call<Void> callEatNear = eatNearClient.createReview(mRestaurantNameTextView.getText().toString(), reviewToSave);
                new ReviewCreationTask().execute(callEatNear);
                finish();
            } else
                Toast.makeText(this, "Wrong data. Please, fill all fields.", Toast.LENGTH_LONG).show();
        }
    }

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

    public class ReviewCreationTask extends AsyncTask<Call<Void>, Void, Response<Void>> {
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
                Toast.makeText(AddReviewActivity.this, "Review has been added.", Toast.LENGTH_SHORT).show();
            } else//no such restaurant (what would be weird, because we add review comming from restaurant's activity)
                Toast.makeText(AddReviewActivity.this, "Data conflict", Toast.LENGTH_LONG).show();
        }
    }
}
