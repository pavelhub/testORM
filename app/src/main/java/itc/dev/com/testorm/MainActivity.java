package itc.dev.com.testorm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

import itc.dev.com.generate.GenerateExecutor;

@EActivity(R.layout.activity_main) // Sets content view to R.layout.translate
public class MainActivity extends Activity {

    ProgressDialog progressDialog;


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
        GenerateExecutor.generateAndSaveToFile();
        showMessage("Generation model complete.");
        dismissProgress();

    }

    @UiThread
    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.create().show();
    }

}
