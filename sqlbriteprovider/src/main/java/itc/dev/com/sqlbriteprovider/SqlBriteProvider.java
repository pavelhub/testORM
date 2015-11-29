package itc.dev.com.sqlbriteprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
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
        final long startTime = System.currentTimeMillis();
//        final BriteDatabase.Transaction transaction = briteDatabase.newTransaction();
////        List<ContentValues> cotentContentValues = new ArrayList<>(jsonModel.size());
////        for (UserModel userModel : jsonModel) {
////
////            Users.Builder builder = new Users.Builder();
////            builder.id(userModel.id);
////            builder.firstName(userModel.first_name);
////            builder.lastName(userModel.last_name);
////            builder.email(userModel.email);
////            builder.password(userModel.password);
////            builder.photoUrl(userModel.photo_url);
////            builder.phoneCode(userModel.phone_code);
////            builder.phoneValue(userModel.phone_value);
////
////            builder.age(userModel.age);
////            builder.isAdmin(userModel.isAdmin);
////            builder.height(userModel.height);
////
////            cotentContentValues.add(builder.build());
////
////        }
////        long endTime = System.currentTimeMillis();
////        Log.e("Insert all", "time create content values" + (endTime - startTime));
////        startTime = System.currentTimeMillis();
////        for (ContentValues cotentContentValue : cotentContentValues) {
////            briteDatabase.insert(Users.TABLE, cotentContentValue);
////        }
////        endTime = System.currentTimeMillis();
//
//
//        Log.e("Insert all", "time " + (endTime - startTime));
//        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "INSERT_ALL", endTime - startTime, null);
        Observable.from(jsonModel)
                .buffer(500)
                .map(new Func1<List<UserModel>, StringBuilder>() {
                    @Override
                    public StringBuilder call(List<UserModel> userModels) {
                        StringBuilder stringBuilder = new StringBuilder();
                        UserModel userModel = userModels.get(0);
                        stringBuilder.append("insert into users (")
                                .append(Users.ID)
                                .append(",")
                                .append(Users.FIRST_NAME)
                                .append(",")
                                .append(Users.LAST_NAME)
                                .append(",")
                                .append(Users.PHOTO_URL)
                                .append(",")
                                .append(Users.AGE)
                                .append(",")
                                .append(Users.EMAIL)
                                .append(",")
                                .append(Users.PASSWORD)
                                .append(",")
                                .append(Users.PHONE_CODE)
                                .append(",")
                                .append(Users.PHONE_VALUE)
                                .append(",")
                                .append(Users.HEIGHT)
                                .append(",")
                                .append(Users.IS_ADMIN)
                                .append(")")

                                .append(" select ")
                                .append(userModel.id)
                                .append(" as ")
                                .append(Users.ID)
                                .append(", '")
                                .append(userModel.first_name)
                                .append("' as ")
                                .append(Users.FIRST_NAME)
                                .append(", '")
                                .append(userModel.last_name)
                                .append("' as ")
                                .append(Users.LAST_NAME)
                                .append(", '")
                                .append(userModel.photo_url)
                                .append("' as ")
                                .append(Users.PHOTO_URL)
                                .append(", ")
                                .append(userModel.age)
                                .append(" as ")
                                .append(Users.AGE)
                                .append(", '")
                                .append(userModel.email)
                                .append("' as ")
                                .append(Users.EMAIL)
                                .append(", '")
                                .append(userModel.password)
                                .append("' as ")
                                .append(Users.PASSWORD)
                                .append(", '")
                                .append(userModel.phone_code)
                                .append("' as ")
                                .append(Users.PHONE_CODE)
                                .append(", '")
                                .append(userModel.phone_value)
                                .append("' as ")
                                .append(Users.PHONE_VALUE)
                                .append(", ")
                                .append(userModel.height)
                                .append(" as ")
                                .append(Users.HEIGHT)
                                .append(", ")
                                .append(userModel.isAdmin ? 1 : 0)
                                .append(" as ")
                                .append(Users.IS_ADMIN);
                        for (int i = 1; i < userModels.size(); i++) {
                            stringBuilder.append(" UNION ALL SELECT ");
                            stringBuilder.append(userModels.get(i).id);
                            stringBuilder.append(", '");
                            stringBuilder.append(userModels.get(i).first_name);
                            stringBuilder.append("' ");
                            stringBuilder.append(", '");
                            stringBuilder.append(userModels.get(i).last_name);
                            stringBuilder.append("', '")
                                    .append(userModel.photo_url)
                                    .append("', ")
                                    .append(userModel.age)
                                    .append(", '")
                                    .append(userModel.email)
                                    .append("', '")
                                    .append(userModel.password)
                                    .append("', '")
                                    .append(userModel.phone_code)
                                    .append("', '")
                                    .append(userModel.phone_value)
                                    .append("', ")
                                    .append(userModel.height)
                                    .append(", ")
                                    .append(userModel.isAdmin ? 1 : 0)
                            ;
                        }
                        return stringBuilder;
                    }
                })
//                .flatMap(new Func1<StringBuilder, Observable<?>>() {
//                    @Override
//                    public QueryObservable call(StringBuilder stringBuilder) {
//                        return
//                    }
//                })
                .doOnNext(new Action1<StringBuilder>() {
                    @Override
                    public void call(StringBuilder stringBuilder) {
                        briteDatabase.execute(stringBuilder.toString());
//                        briteDatabase.createQuery(Users.TABLE,stringBuilder.toString(),null).subscribe();
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        long endTime = System.currentTimeMillis();
                        Log.e("Insert all", "time " + (endTime - startTime));
                        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "INSERT_ALL", endTime - startTime, null);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        providerPostBack.error(SqlBriteProvider.this.getClass().getName(), "INSERT_ALL", e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.e("Insert all", "insert");

                    }
                });
//                .subscribe(new Observer<StringBuilder>() {
//                    @Override
//                    public void onCompleted() {
//                        long endTime = System.currentTimeMillis();
//                        Log.e("Insert all", "time " + (endTime - startTime));
//                        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "INSERT_ALL", endTime - startTime, null);
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        transaction.end();
//                        e.printStackTrace();
//                        providerPostBack.error(SqlBriteProvider.this.getClass().getName(), "INSERT_ALL", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(StringBuilder contentValues) {
//                        Log.e("Insert all", "insert");
//                    }
//                });
    }

    @Override
    public void select(String key, Object value) {
        final long startTime = Calendar.getInstance().getTimeInMillis();
        String QUERY = ""
                + "SELECT * "
                + " FROM " + Users.TABLE;
//                + " where age >=22";// + key + " = '" + value+"'";

        briteDatabase.createQuery(Users.TABLE, QUERY)
                .mapToOne(Users.MAP)
                .subscribeOn(Schedulers.computation())
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
                        for (Users users : userses) {
                            Log.i("users:", " id:" + users.id());
                        }
                    }

                });
    }

    @Override
    public void delete(String key, Object value) {
        final long startTime = Calendar.getInstance().getTimeInMillis();
        int deleted = briteDatabase.delete(Users.TABLE, "age>=?", String.valueOf(21));
        long endTime = Calendar.getInstance().getTimeInMillis();
        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "DELETE ", endTime - startTime, "size:" + deleted);
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
        final long startTime = Calendar.getInstance().getTimeInMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put("age", "21");
        int updated = briteDatabase.update(Users.TABLE, contentValues, null, null);
        long endTime = Calendar.getInstance().getTimeInMillis();
        providerPostBack.onOperationComplete(SqlBriteProvider.this.getClass().getName(), "UPDATE ", endTime - startTime, "size:" + updated);
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
