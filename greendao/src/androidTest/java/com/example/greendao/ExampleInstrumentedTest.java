package com.example.greendao;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.greendao.db.UserDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import io.reactivex.Completable;
import timber.log.Timber;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private String TAG = ExampleInstrumentedTest.class.getSimpleName();

    public static String DATABASE_NAME = "greendao4.db";
    private Context context;
    private static final String FUJIAN = "FUJIAN";
    private static final String ZHEJIANG = "ZHEJIANG";

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.greendao", appContext.getPackageName());
    }

    @Test
    public void initDataBase() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        Database.init(context, DATABASE_NAME);
        if (BuildConfig.DEBUG) {
            Database.enableQueryBuilderLog();
        }
    }


    /**
     * 保存一条记录
     */
    @Test
    public void saveRecord() {
        User user = new User();
        user.setAge(99);
        user.setName("JACK99");
        user.setAddress(ZHEJIANG);
        UserDaoManager.saveRecord(user)
                .subscribe(record -> {
                    Log.d(TAG, "name:" + user.getName());
                    Log.d(TAG, "age:" + user.getAge());
                }, throwable -> {
                    Log.e(TAG, throwable.getMessage());
                });
    }


    /**
     * 保存多条记录
     */
    @Test
    public void saveRecords() {
        List<User> lsUser = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setAge(i);
            user.setName("JACK" + i);
            user.setAddress(FUJIAN);
            lsUser.add(user);
        }
        UserDaoManager.saveRecords(lsUser)
                .subscribe(() -> {
                    Log.d(TAG, "save records success");
                }, throwable -> {
                    Log.e(TAG, throwable.getMessage());
                });
    }

    @Test
    public void getAllRecords() {
        UserDaoManager.getAllUser()
                .switchIfEmpty(Completable.complete().toObservable())
                .concatMapCompletable(user -> {
                    return Completable.complete()
                            .doOnComplete(() -> {
                                Log.d(TAG, "name: " + user.getName());
                                Log.d(TAG, "age: " + user.getAge());
                                Log.d(TAG, "address: " + user.getAddress());
                                Log.d(TAG, "             ");
                            });
                }).subscribe(() -> {
        }, throwable -> {
            Log.e(TAG, throwable.getMessage());
        });
    }

    @Test
    public void clearRecords() {
        UserDaoManager.clearRecord()
                .subscribe();
    }

    @Test
    public void getUserByAgeAndName() {
        UserDaoManager.getUserByAgeAndName(99, "JACK99")
                .subscribe(user -> {
                    Log.d(TAG, "name: " + user.getName());
                    Log.d(TAG, "age: " + user.getAge());
                }, throwable -> {
                    Log.d(TAG, throwable.getMessage());
                });
    }

    @Test
    public void getUserByAgeAndNameRaw() {
        UserDaoManager.getUserByAgeAndNameRaw(8, "JACK8")
                .subscribe(user -> {
                    Log.d(TAG, "name: " + user.getName());
                    Log.d(TAG, "age: " + user.getAge());
                }, throwable -> {
                    Log.d(TAG, throwable.getMessage());
                });
    }

    @Test
    public void getListUserByAddress() {
        UserDaoManager.getUserByAddress(ZHEJIANG)
                .subscribe(userList -> {
                    for (User user : userList) {
                        Log.d(TAG, "name: " + user.getName());
                        Log.d(TAG, "age: " + user.getAge());
                    }
                }, throwable -> {
                    Log.d(TAG, throwable.getMessage());
                });
    }

    @Test
    public void getUserByAgeAndNameSync() {
        User user = UserDaoManager.getUserByAgeAndNameSync(9, "JACK9");
        Log.d(TAG, "name: " + user.getName());
        Log.d(TAG, "age: " + user.getAge());
        Log.d(TAG, "address: " + user.getAddress());
    }

    @Test
    public void getCount() {
        long count = UserDaoManager.count();
        Log.d(TAG, "the count of records is " + count);
    }

    @Test
    public void isEmpty() {
        boolean isEmpty = UserDaoManager.isEmpty();
        Log.d(TAG, "is Empty " + isEmpty);
    }
}