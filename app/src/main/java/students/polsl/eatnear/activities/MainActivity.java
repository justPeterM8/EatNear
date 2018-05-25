package students.polsl.eatnear.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import students.polsl.eatnear.R;
import students.polsl.eatnear.fragments.AllRestaurantsFragment;
import students.polsl.eatnear.fragments.NearRestaurantsFragment;
import students.polsl.eatnear.utilities.NotificationReceiver;

import static students.polsl.eatnear.utilities.Consts.*;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

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

        checkPreferences();
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

    private void scheduleNotification(Context context){

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    RESTAURANT_NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, RESTAURANT_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorAccent))
                .setSmallIcon(R.drawable.ic_restaurant_icon)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("It's time for dinner!")
                .setContentText("Check out our restaurants.")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(showRestaurantList(context))
                .addAction(showRestaurantMap(context))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        Notification notification = notificationBuilder.build();

        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.setAction("" + PENDINT_INTENT_REQUEST_CODE);
        notificationIntent.putExtra(NOTIFICATION_KEY, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, PENDINT_INTENT_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Date date = new Date();
        int hour = date.getHours();
        int minute = date.getMinutes() + 1;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute + 2);
        calendar.set(Calendar.SECOND, 00);
        long startUpTime = calendar.getTimeInMillis();
        if (System.currentTimeMillis() > startUpTime) {
            startUpTime = startUpTime + AlarmManager.INTERVAL_DAY;
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startUpTime, 60*1000, pendingIntent);
    }

    private static PendingIntent contentIntent(Context context) {
        Intent startMainActivity = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                PENDINT_INTENT_REQUEST_CODE,
                startMainActivity,
                PendingIntent.FLAG_ONE_SHOT);
    }

    private static NotificationCompat.Action showRestaurantList(Context context) {
        Intent showRestaurants = new Intent(context, MainActivity.class);
        PendingIntent showRestaurantPendingIntent = PendingIntent.getActivity(
                context,
                ACTION_LIST_REQUEST_CODE,
                showRestaurants,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Action showRestaurantsAction = new NotificationCompat.Action(R.drawable.ic_list,
                "Show list",
                showRestaurantPendingIntent);

        return showRestaurantsAction;
    }

    private static NotificationCompat.Action showRestaurantMap(Context context) {
        Intent showRestaurants = new Intent(context, MapActivity.class);
        PendingIntent showRestaurantPendingIntent = PendingIntent.getActivity(
                context,
                ACTION_MAP_REQUEST_CODE,
                showRestaurants,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Action showRestaurantsAction = new NotificationCompat.Action(R.drawable.ic_map,
                "Show map",
                showRestaurantPendingIntent);

        return showRestaurantsAction;
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();

        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.ic_notification_small_icon);
        return largeIcon;
    }

    private static void cancelNotificationAlarm(Context context){
        Intent notificationIntent = new Intent(context, NotificationReceiver.class);
        notificationIntent.setAction("" + PENDINT_INTENT_REQUEST_CODE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, PENDINT_INTENT_REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.notifications_activation_key))) {
            if (sharedPreferences.getBoolean(key, true)) {
                scheduleNotification(this);
            } else {
                cancelNotificationAlarm(this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void checkPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (sharedPreferences.getBoolean(getString(R.string.notifications_activation_key), true)) {
            scheduleNotification(this);
        }
        else {
            cancelNotificationAlarm(this);
        }
    }
}
