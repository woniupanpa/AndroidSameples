package com.example.greendao;

import android.util.Log;

import com.example.greendao.db.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * @author yanjim
 * @Date 2022/1/4 09:05
 */
public class UserDaoManager {

    private static final String TAG = "ExampleInstrumentedTest";

    /**
     * 获取所有记录
     */
    public static Observable<User> getAllUser() {
        return getRecordOneByOne(getQueryBuilder());
    }


    /**
     * 保存单笔记录
     */
    public static Single<User> saveRecord(User record) {
        return RxJavaInterop.toV2Single(getDao().rx().save(record).toSingle());
    }

    /**
     * 保存多笔记录
     */
    public static Completable saveRecords(List<User> records) {
        return RxJavaInterop.toV2Completable(getDao().rx().saveInTx(records).toCompletable());
    }

    public static Single<User> getUserByAgeAndName(int age, String name) {
        return getRecordUnique(getQueryBuilder()
                .where(UserDao.Properties.Age.eq(age))
                .where(UserDao.Properties.Name.eq(name)));
    }

    public static Single<User> getUserByAgeAndNameRaw(int age, String name) {
        // Clear cache.
        getDao().detachAll();
        String sqlWhere = UserDao.Properties.Age.columnName + " = '" + age + "' AND " +
                UserDao.Properties.Name.columnName + " = '" + name + "'";

        Log.d(TAG, ">>>>>>>getUserByAgeAndNameRaw: "+ sqlWhere);

        return getRecordUnique(getQueryBuilder()
                .where(new WhereCondition.StringCondition(sqlWhere)));
    }

    public static Single<List<User>> getUserByAddress(String address) {
        return RxJavaInterop.toV2Observable(getQueryBuilder()
                .where(UserDao.Properties.Address.eq(address))
                .rx()
                .list()).singleOrError();
    }

    public static User getUserByAgeAndNameSync(int age, String name) {
        return getQueryBuilder()
                .where(UserDao.Properties.Age.eq(age))
                .where(UserDao.Properties.Name.eq(name))
                .unique();
    }

    public static boolean isEmpty() {
        return count() <= 0;
    }

    public static long count() {
        return getQueryBuilder().count();
    }

    public static Completable updateRecord(User record) {
        return RxJavaInterop.toV2Completable(getDao().rx().update(record).toCompletable());
    }

    public static Completable updateRecords(List<User> records) {
        return RxJavaInterop.toV2Completable(getDao().rx().updateInTx(records).toCompletable());
    }

    public static Completable deleteRecord(User record) {
        return RxJavaInterop.toV2Completable(getDao().rx().delete(record).toCompletable());
    }

    public static Completable deleteRecords(List<User> records) {
        return RxJavaInterop.toV2Completable(getDao().rx().deleteInTx(records).toCompletable());
    }

    public static Completable clearRecord() {
        return RxJavaInterop.toV2Completable(getDao().rx().deleteAll().toCompletable());
    }

    private static Observable<List<User>> getRecordsOrEmpty(QueryBuilder<User> queryBuilder) {
        return RxJavaInterop.toV2Observable(queryBuilder.rx().list().switchIfEmpty(rx.Observable.empty()));
    }

    private static UserDao getDao() {
        return Database.get().getUserDao();
    }

    private static QueryBuilder<User> getQueryBuilder() {
        return getDao().queryBuilder();
    }

    private static Single<User> getRecordUnique(QueryBuilder<User> queryBuilder) {
        return RxJavaInterop.toV2Single(queryBuilder.rx().unique().toSingle());
    }

    private static Observable<User> getRecordOneByOne(QueryBuilder<User> queryBuilder) {
        return RxJavaInterop.toV2Observable(queryBuilder.rx().oneByOne());
    }


}
