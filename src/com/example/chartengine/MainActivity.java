package com.example.chartengine;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.linechart.ChartFactory;
import com.linechart.charts.LineChart;
import com.linechart.components.CircleView;
import com.linechart.components.MarkerView;

public class MainActivity extends Activity {

	private LineChart lineChart;
	private LinearLayout ll_containt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ll_containt = (LinearLayout) findViewById(R.id.ll_containt);
		initLineChart();
	}

	private void initLineChart() {

		// String[] dateTime = { "01-01", "01-02", "12-03", "01-04", "01-07",
		// "01-08", "01-09" };// 日期
		// float[] seven = {1.00f,2.00f,9.00f,4.00f,1.00f,6.00f,3.00f};//日期
		// float[] seven = {4.35f, 4.35f, 4.35f, 4.35f, 4.35f, 8.35f, 4.35f};
		// String[] dateTime = { "01-24", "01-25", "01-26", "01-28", "01-29",
		// "01-30", "01-31" };// 日期
		// float[] seven = {0.7947f, 5.638f, 5.638f, 5.638f, 0.7432f, 0.7024f,
		// 0.7186f};
		String[] dateTime = { "01-25", "01-26", "01-28", "01-29", "01-30", "01-31", "02-23" };// 日期
		float[] seven = { 3.032f, 2.782f, 3.219f, 3.219f, 3.219f, 3.848f, 2.931f };
		MarkerView mv = new MarkerView(this, R.layout.managemoney_floatframe, R.id.floatframe);
		CircleView cv = new CircleView(this, R.layout.managemoney_circleframe, R.id.circleframe);
		LineChart lineChart = ChartFactory.createChartView(this, dateTime, seven, mv, cv, true);//true：展示百分号
		ll_containt.addView(lineChart);
	}
}
