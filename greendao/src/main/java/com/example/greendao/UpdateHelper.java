package com.example.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.greendao.db.DaoMaster;
import com.example.greendao.db.UserDao;
import com.github.yuweiguocn.library.greendao.MigrationHelper;

import org.greenrobot.greendao.database.Database;

/**
 * provide database update operation
 *
 * @author jacklee
 * @date 2016/12/20
 */

public class UpdateHelper extends DaoMaster.OpenHelper {

    public UpdateHelper(Context context, String name) {
        super(context, name);
    }

    UpdateHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Log.i("greenDAO", "Upgrading data from version " + oldVersion + " to " + newVersion);
        if (oldVersion < newVersion) {
            MigrationHelper.migrate(db, UserDao.class);
        }
    }
}
