package itc.dev.com.sqlbriteprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import itc.dev.com.baseprovider.FileUril;
import itc.dev.com.baseprovider.ORMProvider;
import itc.dev.com.baseprovider.ProviderPostBack;
import itc.dev.com.baseprovider.UserModel;
import itc.dev.com.sqlbriteprovider.db.DbOpenHelper;
import itc.dev.com.sqlbriteprovider.db.Users;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by pavel on 11/15/15.
 */
public class SqlBriteProvider implements ORMProvider {
    BriteDatabase briteDatabase;
    SqlBrite sqlBrite;
    ProviderPostBack providerPostBack;
    SQLiteOpenHelper helper;

    @Override
    public void init(Context context, ProviderPostBack providerPostBack) {
        this.providerPostBack = providerPostBack;
        File dbFile = FileUril.getDataBaseFile();
        if (dbFile.exists())
            dbFile.delete();
        helper = new DbOpenHelper(context, dbFile);
        sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Timber.tag("Database").v(message);
            }
        });
        briteDatabase = sqlBrite.wrapDatabaseHelper(helper);
    }

    @Override
    public void insertAll(List<UserModel> jsonModel) {
        final long startTime = Calendar.getInstance().getTimeInMillis();
//        final BriteDatabase.Transaction transaction = briteDatabase.newTransaction();

        rx.Observable.from(jsonModel)
                .map(new Func1<UserModel, ContentValues>() {
                    @Override
                    public ContentValues call(UserModel userModel) {

                        Users.Builder builder = new Users.Builder();
                        builder.id(userModel.id);
                        builder.firstName(userModel.first_name);
                        builder.lastName(userModel.last_name);
                        builder.email(userModel.email);
                        builder.password(userModel.password);
                        builder.photoUrl(userModel.photo_url);
                        builder.phoneCode(userModel.phone_code);
                        builder.phoneValue(userModel.phone_value);

                        builder.age(userModel.age);
                        builder.isAdmin(userModel.isAdmin);
                        builder.height(userModel.height);
                        return builder.build();
                    }
                }).doOnNext(new Action1<ContentValues>() {

            @Override
            public void call(ContentValues contentValues) {
//                helper.getWritableDatabase().insert(Users.TABLE,null, contentValues);
                briteDatabase.insert(Users.TABLE, contentValues);

            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContentValues>() {
                    @Override
                    public void onCompleted() {
                        long endTime = Calendar.getInstance().getTimeInMillis();
//                        transaction.markSuccessful();
//                        transaction.end();
                        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "INSERT_ALL", endTime - startTime, null);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        transaction.end();
                        e.printStackTrace();
                        providerPostBack.error(SqlBriteProvider.this.getClass().getName(), "INSERT_ALL", e.getMessage());
                    }

                    @Override
                    public void onNext(ContentValues contentValues) {

                    }
                });
    }

    @Override
    public void select(String key, Object value) {
        final long startTime = Calendar.getInstance().getTimeInMillis();
        String QUERY = ""
                + "SELECT * "
                + " FROM " + Users.TABLE
                + " where age >25";// + key + " = '" + value+"'";

        briteDatabase.createQuery(Users.TABLE, QUERY)
                .mapToOne(Users.MAP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Users>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        providerPostBack.error(SqlBriteProvider.this.getClass().getName(), "SELECT", e.getMessage());
                    }

                    @Override
                    public void onNext(List<Users> userses) {
                        long endTime = Calendar.getInstance().getTimeInMillis();
                        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "SELECT ", endTime - startTime, "size:" + userses.size());
                    }

                });
    }

    @Override
    public void delete(String key, Object value) {
        briteDatabase.delete(Users.TABLE, "last_name=?", "Hubskiy");
//
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Users>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        providerPostBack.error(SqlBriteProvider.this.getClass().getName(), "SELECT", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<Users> userses) {
//                        long endTime = Calendar.getInstance().getTimeInMillis();
//                        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "SELECT ", endTime - startTime, "size:" + userses.size());
//                    }
//
//                });
    }

    @Override
    public void update(String key, Object newValue) {
//
//        briteDatabase.update(Users.TABLE, QUERY)
//                .mapToOne(Users.MAP)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<List<Users>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        providerPostBack.error(SqlBriteProvider.this.getClass().getName(), "SELECT", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<Users> userses) {
//                        long endTime = Calendar.getInstance().getTimeInMillis();
//                        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "SELECT ", endTime - startTime, "size:" + userses.size());
//                    }
//
//                });
    }
}
