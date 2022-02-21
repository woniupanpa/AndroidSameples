package com.example.greendao;

import android.content.Context;

import com.example.greendao.db.DaoMaster;
import com.example.greendao.db.DaoSession;

import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.query.QueryBuilder;

/**
 * @author yanjim
 * @Date 2022/1/4 08:47
 */
public class Database {
    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private static Context dbContext;
    private static String dbName;

    public static void init(@NotNull Context context, String name) {
        dbContext = context.getApplicationContext();
        dbName = name;
    }

    private static DaoMaster getDaoMaster() {
        if (daoMaster == null) {
            UpdateHelper helper = new UpdateHelper(dbContext, dbName, null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }

        return daoMaster;
    }

    public static DaoSession get() {
        if (daoSession == null) {
            return daoSession = getDaoMaster().newSession(IdentityScopeType.None);
        }

        return daoSession;
    }

    public static void enableQueryBuilderLog() {
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }
}
