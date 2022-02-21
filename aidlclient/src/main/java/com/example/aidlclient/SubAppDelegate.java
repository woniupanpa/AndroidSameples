package com.example.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.example.aidlservice.IMyAidlInterface;
import com.example.aidlservice.ISubAppAidlListener;

/**
 * @author yanjim
 * @Date 2022/1/13 11:23
 */
public class SubAppDelegate {

    private static final String TAG = SubAppDelegate.class.getSimpleName();

    private Context context;

    private static IMyAidlInterface subAppInterface = null;

    public SubAppDelegate(Context context) {
        this.context = context;
    }

    public void callAdd() {
        Long startTime = System.currentTimeMillis();
        String packageName = "com.example.aidlservice";
        String serviceAction = "android.intent.action.test";
        callSubApp(packageName, serviceAction, new ISubAppAidlListener.Stub() {

            @Override
            public void onNext(String jsonNextData) throws RemoteException {

            }

            @Override
            public void onMethodFinished(String jsonResponseData) throws RemoteException {
                Log.d(TAG, "endTime--->" + (System.currentTimeMillis() - startTime));
                Log.d(TAG, "responseData--->" + jsonResponseData);
                //disconnect(jsonResponseData);
            }
        });
    }

    public void callSubApp(String packageName, String serviceAction, ISubAppAidlListener listener) {
        if (null != subAppInterface) {
            Log.d(TAG, "SubAppInterface not null!!");
            try {
                subAppInterface.add(1, 2, listener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return;
        }

        new Thread(() -> {
            synchronized (SubAppDelegate.class) {
                try {
                    bindServiceUntilConnect(packageName, serviceAction);
                } catch (Exception e) {
                    return;
                }

                try {
                    subAppInterface.add(1, 2, listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void bindServiceUntilConnect(String packageName, String serviceAction) throws Exception {
        Intent intent = new Intent();
        intent.setPackage(packageName);
        intent.setAction(serviceAction);
        if (!context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)) {
            throw new Exception("Please confirm sub-app installed!!");
        }

        while (subAppInterface == null) {
            Thread.sleep(300);
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            subAppInterface = null;
            Log.d(TAG, "onServiceDisconnected");
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            Log.d(TAG, "onServiceConnected");
            subAppInterface = IMyAidlInterface.Stub.asInterface(arg1);
        }
    };

    public void disconnect(String responseData) {
        Log.d(TAG, "disconnect->>>>>>>>>>>>>>");
        if (null != context) {
            context.unbindService(serviceConnection);
        }
        subAppInterface = null;
        context = null;
    }
}
