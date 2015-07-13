package com.greylabs.yoda.schedular;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.greylabs.yoda.utils.Logger;

/**
 * Created by Jaybhay Vijay on 7/13/2015.
 */
public class DateChangeService extends Service {

    private static final String TAG="DateChangeService";

    private final IBinder iBinder = new LocalBinder();
    public class LocalBinder extends Binder {
        DateChangeService getService() {
            return DateChangeService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.log(TAG, "Date Change Service created. Now updating database.");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "DateChange on start command...", Toast.LENGTH_SHORT).show();
        return START_NOT_STICKY;
    }

}
