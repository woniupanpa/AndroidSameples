package com.example.greendao;

import android.content.Context;

/**
 * @author yanjim
 * @Date 2022/1/4 08:05
 */
public class Initiator {

    private static final String PREFIX= Initiator.class.getSimpleName();

    private Context context;

    public static String DATABASE_NAME = "greendao.db";

    public Initiator() {
        this.context = MyApplication.getContext();
    }

    public void active(){
        initDataBase();
    }

    private void initDataBase(){
        Database.init(context, DATABASE_NAME);
        if (BuildConfig.DEBUG) {
            Database.enableQueryBuilderLog();
        }
    }
}
