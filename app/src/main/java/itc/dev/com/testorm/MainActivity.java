package itc.dev.com.testorm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import itc.dev.com.generate.GenerateExecutor;
import itc.dev.com.generate.User;
import itc.dev.com.testorm.database.table.DataBaseHelper;
import itc.dev.com.testorm.database.table.Users;

@EActivity(R.layout.activity_main) // Sets content view to R.layout.translate
public class MainActivity extends Activity {

    private DataBaseHelper databaseHelper = null;
    private List<Users> users;

    ProgressDialog progressDialog;
    @ViewById
    TextView textViewLoad;
    @ViewById
    TextView textViewGenerate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);

    }

    @Click
    public void generateButton() {
        makeGeneration();
    }

    @UiThread
    public void showProgress(String msg) {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    @UiThread
    public void dismissProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Background
    void makeGeneration() {
        showProgress("Genarate Model...");
        List<User> list = GenerateExecutor.generateAndSaveToFile();
        generationComplete(list);
        dismissProgress();

    }

    @UiThread
    void generationComplete(List<User> list) {
        textViewGenerate.setText("Generation complete:" + list.size());
    }

    @UiThread
    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.create().show();
    }

    @Background
    @Click
    public void loadFormFileButton() {
        showProgress("Loading Model...");
        List<User> list = GenerateExecutor.restoreFromFileModel();
        loadComplete(list);
        users = changeToUsersType(list);
    }

    @Background
    @Click
    public void saveAllButton() {
        showProgress("Saving to database...");
        //  saveToDatabase(users);
        bulkInsert(users);
    }

    @Background
    @Click
    public void deleteButton() {
        showProgress("Delete from database...");
     //   deleteUser(users);
        deleteUserTransaction(users);

    }

    @Background
    @Click
    public void updateButton() {
        showProgress("Update users...");
   //     updateUser(users);
        updateUserTransaction(users);
    }

    private void saveToDatabase(List<Users> list) {
        long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
            for (Users user : list) {
                usersDao.create(user);
            }
            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Save Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Save Time Seconds: " + timeSpent / 1000);
            dismissProgress();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserTransaction(final List<Users> list){
        final SQLiteDatabase db = getHelper().getWritableDatabase();
        db.beginTransaction();
        final long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
            // do ormlite stuff as usual, no callBatchTasks() needed
            usersDao.callBatchTasks(new Callable<Void>() {
                public Void call() throws Exception {
                    final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
                    for (int i = 0; i < list.size(); i++) {
                        usersDao.deleteById(i+1);
                    }
                    return null;
                }
            });
            db.setTransactionSuccessful();
            dismissProgress();
            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Delete Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Delete Time Seconds: " + timeSpent / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    private void deleteUser(List<Users> list) {
        long startTime = System.currentTimeMillis();
        int i = 0;
        try {
            final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
            for (Users user : list) {
                usersDao.deleteById(i);
                i++;
            }
            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Delete Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Delete Time Seconds: " + timeSpent / 1000);
            dismissProgress();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUser(List<Users> list) {
        long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
            for (Users user : list) {
                usersDao.update(user);
            }
            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Update Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Update Time Seconds: " + timeSpent / 1000);
            dismissProgress();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateUserTransaction(final List<Users> list){
        final SQLiteDatabase db = getHelper().getWritableDatabase();
        db.beginTransaction();
        final long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
            // do ormlite stuff as usual, no callBatchTasks() needed
            usersDao.callBatchTasks(new Callable<Void>() {
                public Void call() throws Exception {
                    final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
                    for (Users user : list) {
                        usersDao.update(user);
                    }
                    return null;
                }
            });
            db.setTransactionSuccessful();
            dismissProgress();
            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Update Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Update Time Seconds: " + timeSpent / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void bulkInsert(final List<Users> list) {
        final SQLiteDatabase db = getHelper().getWritableDatabase();
        db.beginTransaction();
        final long startTime = System.currentTimeMillis();
        try {
            final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
            // do ormlite stuff as usual, no callBatchTasks() needed
            usersDao.callBatchTasks(new Callable<Void>() {
                public Void call() throws Exception {
                    final Dao<Users, Integer> usersDao = getHelper().getUsersDao();
                    for (Users user : list) {
                        usersDao.create(user);
                    }
                    return null;
                }
            });
            db.setTransactionSuccessful();
            dismissProgress();
            long timeSpent = System.currentTimeMillis() - startTime;
            Log.wtf("TAG", "Save Time Milliseconds: " + timeSpent);
            Log.wtf("TAG", "Save Time Seconds: " + timeSpent / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    private List<Users> changeToUsersType(List<User> user) {
        List<Users> users = new ArrayList<>();
        for (User user1 : user) {
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

    @UiThread
    public void loadComplete(List<User> list) {
        dismissProgress();
        textViewLoad.setText("Loaded :" + list.size());
    }

    private DataBaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DataBaseHelper.class);
        }
        return databaseHelper;
    }

}
