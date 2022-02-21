package com.example.aidlservice;

import android.content.Context;
import android.os.RemoteException;

/**
 * @author yanjim
 * @Date 2022/1/13 10:56
 */
public class SubAppBinder extends IMyAidlInterface.Stub {

    private Context context;

    public SubAppBinder(Context context) {
        this.context = context;
    }


    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }

    @Override
    public int add(int a, int b, ISubAppAidlListener listener) throws RemoteException {
        listener.onMethodFinished("aaaaaaaaaaaaaaaaaaaaaaaaaa");
        return a + b;
    }
}
