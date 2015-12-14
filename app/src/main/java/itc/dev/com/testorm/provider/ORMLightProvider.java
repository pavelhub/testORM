package itc.dev.com.testorm.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import itc.dev.com.baseprovider.ORMProvider;
import itc.dev.com.baseprovider.ProviderPostBack;
import itc.dev.com.baseprovider.UserModel;
import itc.dev.com.testorm.database.table.DataBaseHelper;
import itc.dev.com.testorm.database.table.Users;

/**
 * Created by pavel on 11/30/15.
 */
public class ORMLightProvider implements ORMProvider {
    private DataBaseHelper databaseHelper = null;

    List<Users> users;

    ProviderPostBack providerPostBack;

    @Override
    public void init(Context context, ProviderPostBack providerPostBack) {
        this.providerPostBack = providerPostBack;

        databaseHelper = OpenHelperManager.getHelper(context, DataBaseHelper.class);
    }

    @Override
    public void insertAll(List<UserModel> userModels) {
        users = changeToUsersType(userModels);
        bulkInsert(users);
    }

    @Override
    public void selectAll() {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();

        final long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
            // do ormlite stuff as usual, no callBatchTasks() needed
            List<Users> list = usersDao.queryForAll();
            for (Users users1 : list) {
                users1.getFirst_name();
            }
            long timeSpent = System.currentTimeMillis() - startTime;

            providerPostBack.onOperationComplete("ORM_LIGHT", "SELECT", timeSpent, "size:" + list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        deleteUserTransaction(users);
    }

    @Override
    public void update(List<UserModel> list) {
        updateUserTransaction(users);
    }


    private void saveToDatabase(List<Users> list) {
        long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
            for (Users user : list) {
                usersDao.create(user);
            }
            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Save Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Save Time Seconds: " + timeSpent / 1000);
            providerPostBack.onOperationComplete("ORM_LIGHT", "INSERT", timeSpent, "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserTransaction(final List<Users> list) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        final long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
            // do ormlite stuff as usual, no callBatchTasks() needed
            usersDao.callBatchTasks(new Callable<Void>() {
                public Void call() throws Exception {
                    final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
                    for (int i = 0; i < list.size(); i++) {
                        usersDao.deleteById(i + 1);
                    }
                    return null;
                }
            });
            db.setTransactionSuccessful();

            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Delete Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Delete Time Seconds: " + timeSpent / 1000);
            providerPostBack.onOperationComplete("ORM_LIGHT", "DELETE", timeSpent, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


//    private void deleteUser(List<Users> list) {
//        long startTime = System.currentTimeMillis();
//        int i = 0;
//        try {
//            final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
//            for (Users user : list) {
//                usersDao.deleteById(i);
//                i++;
//            }
//            long timeSpent = System.currentTimeMillis() - startTime;
//            Log.wtf("TAG", "Delete Time Milliseconds: " + timeSpent);
//            Log.wtf("TAG", "Delete Time Seconds: " + timeSpent / 1000);
//            dismissProgress();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

//    private void updateUser(List<Users> list) {
//        long startTime = System.currentTimeMillis();
//        try {
//            final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
//            for (Users user : list) {
//                usersDao.update(user);
//            }
//            long timeSpent = System.currentTimeMillis() - startTime;
//            Log.wtf("TAG", "Update Time Milliseconds: " + timeSpent);
//            Log.wtf("TAG", "Update Time Seconds: " + timeSpent / 1000);
//            dismissProgress();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    private void updateUserTransaction(final List<Users> list) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        final long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
            // do ormlite stuff as usual, no callBatchTasks() needed
            usersDao.callBatchTasks(new Callable<Void>() {
                public Void call() throws Exception {
                    final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
                    for (Users user : list) {
                        usersDao.update(user);
                    }
                    return null;
                }
            });
            db.setTransactionSuccessful();

            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Update Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Update Time Seconds: " + timeSpent / 1000);
            providerPostBack.onOperationComplete("ORM_LIGHT", "UPDATE", timeSpent, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void bulkInsert(final List<Users> list) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
        final long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
            // do ormlite stuff as usual, no callBatchTasks() needed
            usersDao.callBatchTasks(new Callable<Void>() {
                public Void call() throws Exception {
                    final Dao<Users, Integer> usersDao = databaseHelper.getUsersDao();
                    for (Users user : list) {
                        usersDao.create(user);
                    }
                    return null;
                }
            });
            db.setTransactionSuccessful();

            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Save Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Save Time Seconds: " + timeSpent / 1000);
            providerPostBack.onOperationComplete("ORM_LIGHT", "INSERT", timeSpent, "");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    private List<Users> changeToUsersType(List<UserModel> user) {
        List<Users> users = new ArrayList<>();
        for (UserModel user1 : user) {
            Users user2 = new Users();
            user2.setFirst_name(user1.getFirst_name());
            user2.setLast_name(user1.getLast_name());
            user2.setEmail(user1.getEmail());
            user2.setPassword(user1.getPassword());
            user2.setPhone_code(user1.getPhone_code());
            user2.setPhone_value(user1.getPhone_code());
            user2.setPhoto_url(user1.getPhoto_url());
            user2.setIs_admin(user1.isAdmin());
            user2.setAge(user1.getAge());
            user2.setHeight(user1.getAge());

            users.add(user2);
        }
        return users;
    }

}
