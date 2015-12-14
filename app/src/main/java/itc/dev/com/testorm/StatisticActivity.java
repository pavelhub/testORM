package itc.dev.com.testorm;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.content_statistic)
public class StatisticActivity extends Activity {
    @ViewById
    Spinner spinnerQuary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        containerLayout.removeAllViews();
        ArrayAdapter<Object> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        spinnerQuary.setAdapter(adapter);
        adapter.addAll(getResources().getStringArray(R.array.operations));
        spinnerQuary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showGraph(getResources().getStringArray(R.array.operations)[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerQuary.setSelection(0);

    }

    public void showGraph(String name) {
        float[] values = new float[]{2.0f, 1.5f};
        String[] verlabels = new String[]{"0 sec", "SEC"};
        String[] horlabels = getResources().getStringArray(R.array.providers);
        GraphView graphView = new GraphView(this, values, name, horlabels, verlabels, GraphView.BAR);
//        containerLayout.removeAllViews();
//        containerLayout.addView(graphView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }
}
