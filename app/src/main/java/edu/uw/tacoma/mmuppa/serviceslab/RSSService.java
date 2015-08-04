package edu.uw.tacoma.mmuppa.serviceslab;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 **/
public class RSSService extends IntentService {

    private static final String TAG = "RSSService";
    private static final String CNN_URL = "http://rss.cnn.com/rss/edition.rss";
    private static int POLL_INTERVAL = 60000;


    public RSSService() {
        super("RSSService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "start service");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            Log.d(TAG, "Performing the service");
            try {
                URL url = new URL(CNN_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                Log.d(TAG, "The response is: " + response);
                InputStream content = conn.getInputStream();

                BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                String s = "";
                String result = "";
                while ((s = buffer.readLine()) != null) {
                    result += s;
                }
                Log.i(TAG, result);

            } catch (Exception e) {
                e.printStackTrace();
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle("CNN News Update")
                    .setSmallIcon(android.R.drawable.bottom_bar)
                    .setContentText("Some news has been received");

            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Creates the PendingIntent
            PendingIntent notifyPendingIntent =
                    PendingIntent.getActivity(
                            this,
                            0,
                            i,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            // Puts the PendingIntent into the notification builder
            builder.setContentIntent(notifyPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // mId allows you to update the notification later on.
            mNotificationManager.notify(1, builder.build());
        }
    }


    public static void setServiceAlarm(Context c, boolean isOn) {
        Intent intent = new Intent(c, RSSService.class);
        PendingIntent pendingIntent = PendingIntent.getService(
                c, 0, intent, 0);
        AlarmManager alarmManager =
                (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP
            , 10, POLL_INTERVAL, pendingIntent);
        } else {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
