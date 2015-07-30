package edu.uw.tacoma.mmuppa.serviceslab;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 **/
public class RSSService extends IntentService {

    private static final String TAG = "RSSService";

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
            Log.d(TAG, "Handling Service");
        }
    }
}
