package com.example.chartengine;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.linechart.ChartFactory;
import com.linechart.charts.LineChart;
import com.linechart.components.CircleView;
import com.linechart.components.MarkerView;
import com.piechartview.PieChartView;

public class MainActivity extends Activity {

	private LineChart lineChart;
	private LinearLayout ll_containt;
	

    //视图
    private PieChartView pieChartView;
    //模拟数据
    private Map<String,Float> chartMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ll_containt = (LinearLayout) findViewById(R.id.ll_containt);
		initLineChart();
		bingtu();
	}

	private void initLineChart() {
		String[] dateTime = { "01-25", "01-26", "01-28", "01-29", "01-30", "01-31", "02-23" };// 日期
		float[] seven = { 3.032f, 2.782f, 3.219f, 3.219f, 3.219f, 3.848f, 2.931f };

		MarkerView mv = new MarkerView(this, R.layout.managemoney_floatframe, R.id.floatframe);
		CircleView cv = new CircleView(this, R.layout.managemoney_circleframe, R.id.circleframe);
		LineChart lineChart = ChartFactory.createChartView(this, dateTime, seven, mv, cv, true);
		ll_containt.addView(lineChart);
	}
	
	private void bingtu(){
        pieChartView = (PieChartView) findViewById(R.id.pie_chart_view);
        chartMap = new HashMap<String, Float>();
        chartMap.put("0",0.9f);
        chartMap.put("1",0.1f);
        pieChartView.setChartMap(chartMap,800,Color.WHITE,Color.BLUE,R.color.a,R.color.b);//弧形的条数及比例；初始化 矩阵的高度和宽度；小圆的颜色;小圆中百分比的颜色；第一道弧形的颜色；第二道弧形的颜色

	}
}
