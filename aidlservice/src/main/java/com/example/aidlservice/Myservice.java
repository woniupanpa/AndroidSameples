package com.example.aidlservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author yanjim
 * @Date 2022/1/13 10:04
 */
public class Myservice extends Service {

    private static final String TAG = Myservice.class.getSimpleName();

    private SubAppBinder subAppBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind--->");
        return subAppBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind--->");
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate--->");
        super.onCreate();
        subAppBinder = new SubAppBinder(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand--->");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy--->");
        super.onDestroy();
    }
}
