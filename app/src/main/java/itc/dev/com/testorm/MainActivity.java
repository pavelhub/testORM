package itc.dev.com.testorm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import itc.dev.com.baseprovider.ORMProvider;
import itc.dev.com.baseprovider.ProviderPostBack;
import itc.dev.com.baseprovider.UserModel;
import itc.dev.com.generate.GenerateExecutor;
import itc.dev.com.greendao.GreenDaoProvider;
import itc.dev.com.realmprovider.RealmProvider;
import itc.dev.com.testorm.provider.ORMLightProvider;

@EActivity(R.layout.activity_main_statistic) // Sets content view to R.layout.translate
public class MainActivity extends Activity {

    ProgressDialog progressDialog;
    @ViewById
    EditText editTextGenerate;
    @ViewById(R.id.multiSelectionSpinner)
    MultiSelectionSpinner selectProvider;
    @ViewById
    Button buttonClearCach;
    @ViewById
    ListView listViewResalt;
    @ViewById
    Button buttonShowStataistic;
    static Map<String, Map<String, Long>> mapOperationMapProvider;

    List<UserModel> model;
    Map<String, ORMProvider> providerMap;
    AdapterListLog adapterListLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        providerMap = new HashMap<>();
        mapOperationMapProvider = new Hashtable<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        buildMapProviders(getResources().getStringArray(R.array.providers));
        adapterListLog = new AdapterListLog();
        listViewResalt.setAdapter(adapterListLog);

        selectProvider.setItems(getResources().getStringArray(R.array.providers));
        selectProvider.setSelection(getResources().getStringArray(R.array.providers));
        selectProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<String> selections = selectProvider.getSelectedStrings();
                buildMapProviders(selections.toArray(new String[selections.size()]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Click
    public void buttonClearCach() {
        buildMapProviders(getResources().getStringArray(R.array.providers));
        adapterListLog = new AdapterListLog();
        listViewResalt.setAdapter(adapterListLog);
        mapOperationMapProvider = new Hashtable<>();

    }

    public void addLog(String string) {
        adapterListLog.addToLog(string);
        adapterListLog.notifyDataSetChanged();
    }


    private void buildMapProviders(String[] providers) {
        String[] defProviders = getResources().getStringArray(R.array.providers);
        providerMap = new HashMap<>();
        for (String provider : providers) {

            ORMProvider ormProvider = null;
            if (provider.equals(defProviders[0])) {
                ormProvider = new GreenDaoProvider();
            } else if (provider.equals(defProviders[1])) {
                ormProvider = new ORMLightProvider();
            } else if (provider.equals(defProviders[2])) {
                ormProvider = new RealmProvider();
            }
            ormProvider.init(this, new ProviderPostBack() {
                @Override
                public void onOperationComplete(final String provider, final String operation, final long time, final String details) {
                    dismissProgress();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            StringBuilder logAction = new StringBuilder();
                            logAction.append(" provider: " + provider);
                            logAction.append(" " + operation);
                            logAction.append(" " + time);
                            if (details != null && details.length() > 0)
                                logAction.append(" details: " + details);
                            logAction.append("\n");
                            addLog(logAction.toString());
                            Map<String, Long> listMapProvider = new Hashtable<String, Long>();
                            if (mapOperationMapProvider.containsKey(operation)) {
                                listMapProvider = mapOperationMapProvider.get(operation);
                            }
                            mapOperationMapProvider.put(operation, listMapProvider);

                            listMapProvider.put(provider, time);

                        }
                    });


                }

                @Override
                public void error(final String provider, final String operation, final String message) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dismissProgress();
                            showMessage("Error: " + provider + " operation" + operation + ": " + message);
                            addLog("Error: " + provider + " operation" + operation + ": " + message);
                        }
                    });

                }
            });
            providerMap.put(provider, ormProvider);
        }
    }

    @Click
    public void buttonShowStataistic() {
        startActivity(new Intent(this, BarChartActivity.class));
    }

    @Click
    @Background
    public void buttonStartTest() {
        makeGeneration();

        for (String provider : selectProvider.getSelectedStrings()) {

            ORMProvider ormProvider = providerMap.get(provider);
            showProgress("Inserting " + provider);
            ormProvider.insertAll(model);
            showProgress("Select " + provider);
            ormProvider.selectAll();
            showProgress("Update " + provider);
            ormProvider.update(model);
            showProgress("Delete " + provider);
            ormProvider.deleteAll();
        }
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


    void makeGeneration() {
        showProgress("Genarate Model...");
        model = GenerateExecutor.generateAndSaveToFile(Integer.parseInt(editTextGenerate.getText().toString()));
        generationComplete(model);
        dismissProgress();

    }

    @UiThread
    void generationComplete(List<UserModel> list) {
        addLog("Generation complete:" + list.size());
//        textViewGenerate.setText("Generation complete:" + list.size());
    }

    @UiThread
    public void showMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.create().show();
    }

    class AdapterListLog extends BaseAdapter {
        List<String> log = new ArrayList<>();

        @Override
        public int getCount() {
            return log.size();
        }

        @Override
        public Object getItem(int position) {
            return log.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(parent.getContext());
            textView.setText(log.get(position));
            return textView;
        }

        public void addToLog(String string) {
            log.add(string);
        }
    }

}
