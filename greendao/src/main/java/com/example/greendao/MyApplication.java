package com.example.greendao;

import android.app.Application;
import android.content.Context;

/**
 * @author yanjim
 * @Date 2022/1/4 08:02
 */
public class MyApplication extends Application {

    private static Context context;

    public static Context getContext(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        initApplication();
    }

    private void initApplication(){
        new Initiator().active();
    }
}
