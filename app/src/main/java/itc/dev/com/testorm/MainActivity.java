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

import itc.dev.com.baseprovider.ORMProvider;
import itc.dev.com.baseprovider.ProviderPostBack;
import itc.dev.com.baseprovider.UserModel;
import itc.dev.com.generate.GenerateExecutor;
import itc.dev.com.sqlbriteprovider.SqlBriteProvider;

@EActivity(R.layout.activity_main) // Sets content view to R.layout.translate
public class MainActivity extends Activity {

    ProgressDialog progressDialog;
    @ViewById
    TextView textViewLoad;
    @ViewById
    TextView textViewGenerate;
    @ViewById
    TextView textViewInsertRXBrite;
    ORMProvider sqlBriteProvider;
    List<UserModel> model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        sqlBriteProvider = new SqlBriteProvider();
        sqlBriteProvider.init(this, new ProviderPostBack() {
            @Override
            public void onOperationComplete(String provider, String operation, long time, String details) {
                dismissProgress();
                textViewInsertRXBrite.append(" provider: " + provider);
                textViewInsertRXBrite.append(" operation: " + operation);
                textViewInsertRXBrite.append(" time: " + time);
                if (details != null && details.length() > 0)
                    textViewInsertRXBrite.append(" details: " + details);
                textViewInsertRXBrite.append("\n");

            }

            @Override
            public void error(String provider, String operation, String message) {
                dismissProgress();
                showMessage("Error: " + provider + " operation" + operation + ": " + message);
            }
        });

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
        List<UserModel> list = GenerateExecutor.generateAndSaveToFile();
        generationComplete(list);
        dismissProgress();

    }

    @UiThread
    void generationComplete(List<UserModel> list) {
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
        model = GenerateExecutor.restoreFromFileModel();
        loadComplete(model);
    }

    @UiThread
    public void loadComplete(List list) {
        dismissProgress();
        textViewLoad.setText("Loaded :" + list.size());
    }

    @Click
    public void insertRXBriteButton() {
        showProgress("Inserting Model...");
        sqlBriteProvider.insertAll(model);

    }

    @Click
    public void selectButtonSqlBrite() {
        showProgress("Select Model... hubskiy");
        sqlBriteProvider.select("last_name", "Hubskiy");

    }

}
