package students.polsl.eatnear.utilities;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String NOTIFICATION_KEY = "notification";
    private static final int PENDINT_INTENT_REQUEST_CODE = 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = intent.getParcelableExtra(NOTIFICATION_KEY);
        notificationManager.notify(PENDINT_INTENT_REQUEST_CODE, notification);
    }
}
