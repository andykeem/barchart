package loc.example.barchartapp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ChartView mBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        List<Float> spends = Arrays.asList(1589.97f, 2196.33f, 1703.10f);

        List<Float> spends = Arrays.asList(100f, 300f, 200f);

//        List<Float> spends = Arrays.asList(200f, 600f, 1000f, 300f, 800f, 500f);

        mBarChart = findViewById(R.id.bar_chart);
        mBarChart.setSpends(spends);

        String json = readMonthlyOverviewJson();
        List<Bar> bars = getBarsByJson(json);

        mBarChart.setBars(bars);
    }

    private List<Bar> getBarsByJson(String json) {
        List<Bar> bars = new ArrayList<>();
        try {
            JSONArray jsonArr = (JSONArray) new JSONTokener(json).nextValue();
            int size = jsonArr.length();
            for (int i = 0; i < size; i++) {
                JSONObject jsonObj = (JSONObject) jsonArr.get(i);
                String month = jsonObj.getString("month");
                String spend = jsonObj.getString("spend");
                Bar bar = new Bar(month, spend);
                bars.add(bar);
            }
        } catch (JSONException je) {
            Log.e(TAG, je.getMessage(), je);
        }
        return bars;
    }

    private String readMonthlyOverviewJson() {
        AssetManager am = getAssets();
        String filename = "monthly-overview.json";
        try {
            InputStream is = am.open(filename);
            int size = is.available();
            byte[] b = new byte[size];
            is.read(b);
            return new String(b);
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage(), ioe);
        }
        return null;
    }
}
