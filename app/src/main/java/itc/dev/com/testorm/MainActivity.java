package itc.dev.com.testorm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import itc.dev.com.generate.GenerateExecutor;
import itc.dev.com.generate.User;

@EActivity(R.layout.activity_main) // Sets content view to R.layout.translate
public class MainActivity extends Activity {

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
    }

    @Background
    @Click
    public void saveAllButton() {
        showProgress("Saving to database...");
    }

    @Background
    @Click
    public void deleteButton() {
        showProgress("Delete from database...");

    }

    @Background
    @Click
    public void updateButton() {
        showProgress("Update users...");

    }


    @UiThread
    public void loadComplete(List<User> list) {
        dismissProgress();
        textViewLoad.setText("Loaded :" + list.size());
    }



}
