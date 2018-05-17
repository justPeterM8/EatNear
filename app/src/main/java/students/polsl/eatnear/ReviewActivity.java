package students.polsl.eatnear;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewActivity extends AppCompatActivity {
    private Button mSubmitButton;
    private RatingBar mRatingBar;
    private EditText mCustomerEditText;
    private EditText mReviewEditText;
    private TextView mRestaurantNameTextView;

    private double mRating;
    private String mReview;
    private String mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        mRatingBar = findViewById(R.id.restaurantRatingBar);
        mSubmitButton = findViewById(R.id.submitButton);
        mCustomerEditText = findViewById(R.id.loginEditText);
        mReviewEditText = findViewById(R.id.descriptionEditView);
        mRestaurantNameTextView = findViewById(R.id.restaurantNameTextView);

        Intent intent = getIntent();
        mRestaurantNameTextView.setText(intent.getStringExtra("name"));

        mSubmitButton.setOnClickListener(view -> {
            mRating = mRatingBar.getRating();

            if ( (!TextUtils.isEmpty(mReviewEditText.getText().toString())) && (!TextUtils.isEmpty(mCustomerEditText.getText().toString())) && (mRating != 0) ) {
                mReview = mReviewEditText.getText().toString();
                mCustomer = mCustomerEditText.getText().toString();
                Toast.makeText(this, "Review has been added.", Toast.LENGTH_SHORT).show();
                finish();
            } else
                Toast.makeText(this, "Wrong data. Please, fill all fields.", Toast.LENGTH_LONG).show();
        });
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
