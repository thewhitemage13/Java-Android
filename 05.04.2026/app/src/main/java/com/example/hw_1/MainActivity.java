package com.example.hw_1;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    LineChart bitcoinChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bitcoinChart = findViewById(R.id.bitcoinChart);

        ArrayList<Entry> entries = new ArrayList<>();

        entries.add(new Entry(2010, 0.08f));
        entries.add(new Entry(2011, 1.00f));
        entries.add(new Entry(2012, 13.00f));
        entries.add(new Entry(2013, 770.00f));
        entries.add(new Entry(2014, 320.00f));
        entries.add(new Entry(2015, 430.00f));
        entries.add(new Entry(2016, 960.00f));
        entries.add(new Entry(2017, 13800.00f));
        entries.add(new Entry(2018, 3800.00f));
        entries.add(new Entry(2019, 7200.00f));
        entries.add(new Entry(2020, 28900.00f));
        entries.add(new Entry(2021, 47000.00f));
        entries.add(new Entry(2022, 16500.00f));
        entries.add(new Entry(2023, 42000.00f));
        entries.add(new Entry(2024, 69000.00f));
        entries.add(new Entry(2025, 95000.00f));

        LineDataSet dataSet = new LineDataSet(entries, "BTC price, USD");

        dataSet.setColor(Color.rgb(255, 152, 0));
        dataSet.setCircleColor(Color.rgb(255, 152, 0));
        dataSet.setLineWidth(3f);
        dataSet.setCircleRadius(4f);
        dataSet.setValueTextSize(10f);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.rgb(255, 224, 178));

        LineData lineData = new LineData(dataSet);
        bitcoinChart.setData(lineData);

        Description description = new Description();
        description.setText("Bitcoin price history");
        bitcoinChart.setDescription(description);

        bitcoinChart.setTouchEnabled(true);
        bitcoinChart.setDragEnabled(true);
        bitcoinChart.setScaleEnabled(true);
        bitcoinChart.setPinchZoom(true);

        XAxis xAxis = bitcoinChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-45);

        bitcoinChart.getAxisRight().setEnabled(false);

        bitcoinChart.animateX(1500);
        bitcoinChart.invalidate();
    }
}