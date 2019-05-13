package com.app.mobinyardim.discretetimeconvolution;

import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private LineChart xnChart, hnChart , convChart;
    private TextInputLayout xn, nx, hn, nh;
    private Button convolution, draw;

    private float[][] input, feedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xn = findViewById(R.id.xn);
        nx = findViewById(R.id.nx);
        hn = findViewById(R.id.hn);
        nh = findViewById(R.id.nh);
        draw = findViewById(R.id.draw);
        convolution = findViewById(R.id.convolution);
        draw = findViewById(R.id.draw);
        xnChart = findViewById(R.id.xn_chart);
        hnChart = findViewById(R.id.hn_chart);
        convChart = findViewById(R.id.conv_chart);

        Objects.requireNonNull(xn.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                xn.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Objects.requireNonNull(nx.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nx.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Objects.requireNonNull(nh.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nh.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Objects.requireNonNull(hn.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hn.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        draw.setOnClickListener(v -> {
                    if (getData()) {
                        drawCharts();
                    }
                }
        );

        convolution.setOnClickListener(v -> {
            ArrayList<ArrayList<Float>> list = convolution();



            List<Entry> entries = new ArrayList<>();
            for (ArrayList<Float> pos : list) {
                entries.add(new Entry(pos.get(0), pos.get(1)));
            }
            LineDataSet dataSet = new LineDataSet(entries, "y[n]");
            dataSet.setCircleColor(Color.RED);
            dataSet.setColor(Color.GREEN);
            dataSet.disableDashedLine();

            convChart.setData(new LineData(dataSet));
            convChart.invalidate();
        });

     /*   float[][] arr = {{1, 2, 3}, {1, 2, 3}};
        reverse(arr);
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                Log.i("Result", "res: " + arr[i][j]);*/

    }

    private ArrayList<ArrayList<Float>> convolution() {
        float[][] reverse = feedBack.clone();
        for (int i = 0; i < reverse.length; i++) {
            if (reverse[i][0] != 0)
                reverse[i][0] *= -1;
        }

        ArrayList<ArrayList<Float>> newList = new ArrayList<>();

        for (int i = 0; i < reverse.length; i++) {
            float x = reverse[i][0];
            for (int j = 0; j < input.length; j++) {

                ArrayList<Float> row = new ArrayList<>();
                float n = input[j][0] - x;
                row.add(n);
                row.add(sigma(n, reverse, input));
                newList.add(row);

            }
        }
        return newList;
    }

    private Float sigma(float n, float[][] reverse, float[][] input) {

        float clone[][] = new float[reverse.length][reverse[0].length];
        for (int i = 0; i < reverse.length; i++) {
            for (int j = 0; j < reverse[0].length; j++) {
                clone[i][j] = reverse[i][j];
            }
        }
        for (int i = 0; i < reverse.length; i++) {
            clone[i][0] += n;

        }
        float sum = 0;
        for (int i = 0; i < clone.length; i++) {
            for (int j = 0; j < input.length; j++) {
                if (input[j][0] == clone[i][0]) {
                    sum += (clone[i][1] * input[j][1]);
                }
            }
        }
        return sum;
    }

    private void drawCharts() {

        List<Entry> list = new ArrayList<>();
        for (float[] floats1 : input) {
            list.add(new Entry(floats1[0], floats1[1]));
        }
        LineDataSet dataSet = new LineDataSet(list, "X[n]");
        dataSet.setCircleColor(Color.RED);
        dataSet.setColor(Color.BLUE);
        dataSet.disableDashedLine();

        List<Entry> list2 = new ArrayList<>();
        for (float[] floats : feedBack) {
            list2.add(new Entry(floats[0], floats[1]));
        }
        LineDataSet dataSet2 = new LineDataSet(list2, "h[n]");
        dataSet2.setCircleColor(Color.RED);
        dataSet2.setColor(Color.RED);
        dataSet2.disableDashedLine();

        xnChart.setData(new LineData(dataSet2));
        xnChart.invalidate();

        hnChart.setData(new LineData(dataSet2));
        hnChart.invalidate();
    }

    private boolean getData() {
        String strXn = xn.getEditText().getText().toString().trim();
        String[] xnArr = strXn.split(",");

        String strNx = nx.getEditText().getText().toString().trim();
        String[] nxArr = strNx.split(",");

        String strHn = hn.getEditText().getText().toString().trim();
        String[] hnArr = strHn.split(",");

        String strNh = nh.getEditText().getText().toString().trim();
        String[] nhArr = strNh.split(",");


        if (xnArr.length != nxArr.length) {
            nx.setError("number of inputs is not equal with x[n]'s input");
            return false;
        }
        if (hnArr.length != nhArr.length) {
            nh.setError("number of inputs is not equal with x[n]'s input");
            return false;
        }

        input = new float[xnArr.length][2];
        feedBack = new float[nhArr.length][2];

        for (int i = 0; i < xnArr.length; i++) {

            try {
                input[i][0] = Float.valueOf(nxArr[i]);
            } catch (Exception e) {
                nx.setError("format is not valid");
                return false;
            }
            try {
                input[i][1] = Float.valueOf(xnArr[i]);
            } catch (Exception e) {
                xn.setError("format is not valid");
                return false;
            }
            Log.i("ConvertLog", "n: " + input[i][0] + " X[n]: " + input[i][1]);
        }

        for (int i = 0; i < hnArr.length; i++) {

            try {
                feedBack[i][0] = Float.valueOf(nhArr[i]);
            } catch (Exception e) {
                nh.setError("format is not valid");
                return false;
            }
            try {
                feedBack[i][1] = Float.valueOf(hnArr[i]);
            } catch (Exception e) {
                hn.setError("format is not valid");
                return false;
            }
            Log.i("ConvertLog", "n: " + feedBack[i][0] + " h[n]: " + feedBack[i][1]);
        }
        return true;

    }


}
