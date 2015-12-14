package itc.dev.com.sqlbriteprovider;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import itc.dev.com.baseprovider.FileUril;
import itc.dev.com.baseprovider.ORMProvider;
import itc.dev.com.baseprovider.ProviderPostBack;
import itc.dev.com.baseprovider.UserModel;
import itc.dev.com.sqlbriteprovider.db.DbOpenHelper;
import itc.dev.com.sqlbriteprovider.db.Users;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by pavel on 11/15/15.
 */
public class SqlBriteProvider implements ORMProvider {
    String TAG = "SqlBriteProvider";
    BriteDatabase briteDatabase;
    SqlBrite sqlBrite;
    ProviderPostBack providerPostBack;
    SQLiteOpenHelper helper;
    Executor thisExecutor = Executors.newSingleThreadExecutor();
    Scheduler scheduler = Schedulers.from(thisExecutor);

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
//        providerPostBack.onOperationComplete(TAG, "INSERT_ALL", endTime - startTime, null);
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
                                    .append(userModels.get(i).photo_url)
                                    .append("', ")
                                    .append(userModels.get(i).age)
                                    .append(", '")
                                    .append(userModels.get(i).email)
                                    .append("', '")
                                    .append(userModels.get(i).password)
                                    .append("', '")
                                    .append(userModels.get(i).phone_code)
                                    .append("', '")
                                    .append(userModels.get(i).phone_value)
                                    .append("', ")
                                    .append(userModels.get(i).height)
                                    .append(", ")
                                    .append(userModels.get(i).isAdmin ? 1 : 0)
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
//                .subscribeOn(scheduler)
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        long endTime = System.currentTimeMillis();
                        Log.e("Insert all", "time " + (endTime - startTime));

                        providerPostBack.onOperationComplete(TAG, "INSERT_ALL", endTime - startTime, null);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        providerPostBack.error(TAG, "INSERT_ALL", e.getMessage());
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
//                        providerPostBack.onOperationComplete(TAG, "INSERT_ALL", endTime - startTime, null);
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
////                        transaction.end();
//                        e.printStackTrace();
//                        providerPostBack.error(TAG, "INSERT_ALL", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(StringBuilder contentValues) {
//                        Log.e("Insert all", "insert");
//                    }
//                });
    }

    Subscription subscriberSelect;

    @Override
    public void selectAll() {
        final long startTime = Calendar.getInstance().getTimeInMillis();
        String QUERY = ""
                + "SELECT * "
                + " FROM " + Users.TABLE;
//                + " where age >=22";// + key + " = '" + value+"'";

        subscriberSelect = briteDatabase.createQuery(Users.TABLE, QUERY)
                .mapToOne(Users.MAP)
//                .subscribeOn(scheduler)
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Users>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        providerPostBack.error(TAG, "SELECT", e.getMessage());
                    }

                    @Override
                    public void onNext(List<Users> userses) {
                        long endTime = Calendar.getInstance().getTimeInMillis();
                        providerPostBack.onOperationComplete(TAG, "SELECT ", endTime - startTime, "size:" + userses.size());
                        if (subscriberSelect != null)
                            subscriberSelect.unsubscribe();
//                        for (Users users : userses) {
//
//                            providerPostBack.onOperationComplete(TAG, "SELECT ", users.id(), users.email());
//                        }
                    }

                });
    }

    @Override
    public void deleteAll() {
        final long startTime = Calendar.getInstance().getTimeInMillis();
        String QUERY = ""
                + "Delete  "
                + " FROM " + Users.TABLE;
//                + " where age >=22";// + key + " = '" + value+"'";

        briteDatabase.createQuery(Users.TABLE, QUERY)
//                .mapToOne(Users.MAP)

//                .subscribeOn(scheduler)
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SqlBrite.Query>() {
                    @Override
                    public void onCompleted() {

                    }


                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        providerPostBack.error(TAG, "DELETE", e.getMessage());
                    }

                    @Override
                    public void onNext(SqlBrite.Query query) {
                        long endTime = Calendar.getInstance().getTimeInMillis();
                        providerPostBack.onOperationComplete(TAG, "DELETE", endTime - startTime, null);

                    }


                });
//        final long startTime = Calendar.getInstance().getTimeInMillis();
//        int deleted = briteDatabase.delete(Users.TABLE, "age>=?", String.valueOf(21));
//        long endTime = Calendar.getInstance().getTimeInMillis();
//        providerPostBack.onOperationComplete(TAG, "DELETE ", endTime - startTime, "size:" + deleted);
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
//                        providerPostBack.error(TAG, "SELECT", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<Users> userses) {
//                        long endTime = Calendar.getInstance().getTimeInMillis();
//                        providerPostBack.onOperationComplete(TAG, "SELECT ", endTime - startTime, "size:" + userses.size());
//                    }
//
//                });
    }


    @Override
    public void update(List<UserModel> userses) {
        final long startTime = Calendar.getInstance().getTimeInMillis();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("age", "21");


//        long endTime = Calendar.getInstance().getTimeInMillis();
//        providerPostBack.onOperationComplete(TAG, "UPDATE ", endTime - startTime, "size:" + updated);
        Observable.from(userses)
                .map(new Func1<UserModel, Integer>() {
                    @Override
                    public Integer call(UserModel userModel) {
                        Users.Builder builder = new Users.Builder();
//                        builder.id(userModel.id);
                        builder.firstName(userModel.first_name);
                        builder.lastName(userModel.last_name);
                        builder.email(userModel.email);
                        builder.password(userModel.password);
                        builder.photoUrl(userModel.photo_url);
                        builder.phoneCode(userModel.phone_code);
                        builder.phoneValue(userModel.phone_value);

                        builder.age(userModel.age + 1);
                        builder.isAdmin(userModel.isAdmin());
                        builder.height(userModel.height + 1);

                        return briteDatabase.update(Users.TABLE, builder.build(), Users.ID + "=?", new String[]{String.valueOf(userModel.getId())});
                    }
                })
//                .subscribeOn(scheduler)
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {
                        long endTime = Calendar.getInstance().getTimeInMillis();
                        providerPostBack.onOperationComplete(TAG, "UPDATE", endTime - startTime, null);

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        providerPostBack.error(TAG, "UPDATE", e.getMessage());
                    }

                    @Override
                    public void onNext(Integer userses) {

                    }

                });
        ;
// List<ContentValues> cotentContentValues = new ArrayList<>(jsonModel.size());
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
//                        providerPostBack.error(TAG, "UPDATE ", e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(List<Users> userses) {
//                        long endTime = Calendar.getInstance().getTimeInMillis();
//                        providerPostBack.onOperationComplete(TAG, "UPDATE", endTime - startTime, "size:" + userses.size());
//                    }
//
//                });
    }

}
