package students.polsl.eatnear.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import students.polsl.eatnear.R;
import students.polsl.eatnear.fragments.AllRestaurantsFragment;
import students.polsl.eatnear.fragments.NearRestaurantsFragment;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSIONS_CODE = 100;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(students.polsl.eatnear.R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        mViewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);

        setSupportActionBar(toolbar);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return;
            } else {
                finish();
            }
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    NearRestaurantsFragment nearRestaurantsFragment = new NearRestaurantsFragment();
                    return nearRestaurantsFragment;

                case 1:
                    AllRestaurantsFragment allRestaurantsFragment = new AllRestaurantsFragment();
                    return allRestaurantsFragment;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isNetworkConnected()){
            Toast.makeText(this, "This app requires Internet connection", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!isGpsActive()){
            Toast.makeText(this, "This app requires active GPS", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
