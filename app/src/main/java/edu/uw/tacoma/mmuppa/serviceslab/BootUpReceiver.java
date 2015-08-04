package edu.uw.tacoma.mmuppa.serviceslab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BootUpReceiver extends BroadcastReceiver {
    private static final String TAG = "BootUpReceiver";
    public BootUpReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    context.getString(R.string.SHARED_PREFS), Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean(context.getString(R.string.ON), false)) {
                RSSService.setServiceAlarm(context, true);
            }
        }
    }
}
