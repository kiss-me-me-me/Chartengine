package com.linechart;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;

import com.linechart.animation.Easing;
import com.linechart.charts.LineChart;
import com.linechart.components.CircleView;
import com.linechart.components.MarkerView;
import com.linechart.components.YAxis;
import com.linechart.data.Entry;
import com.linechart.data.LineData;
import com.linechart.data.LineDataSet;
import com.linechart.utils.MsgChart;

public class ChartFactory {

	public static LineChart createChartView(Context context, Object[] xObject, Object[] yObject, MarkerView markerView,
			CircleView circleView, boolean percentage) {
		try {
			String[] xValue = new String[xObject.length];
			float[] yValue = new float[yObject.length];
			for (int i = 0; i < xObject.length; i++) {
				xValue[i] = (String) xObject[i];
			}
			for (int i = 0; i < yObject.length; i++) {
				yValue[i] = (Float) yObject[i];
			}
			return createChartView(context, xValue, yValue, markerView, circleView, percentage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LineChart lineChart = new LineChart(context);
		lineChart.setBackgroundColor(Color.parseColor("#ffffff"));
		return lineChart;
	}

	public static LineChart createChartView(Context context, String[] xObject, float[] yObject, MarkerView markerView,
			CircleView circleView, boolean percentage) {
		LineChart lineChart = new LineChart(context);
		lineChart.setBackgroundColor(Color.parseColor("#ffffff"));
		if (xObject.length == 0) {
			return lineChart;
		}
		try {
			lineChart.setHighlightEnabled(true);
			lineChart.setTouchEnabled(true);
			lineChart.setDragEnabled(true);
			lineChart.setScaleEnabled(false);
			lineChart.setHighlightIndicatorEnabled(false);
			lineChart.percentage = percentage;
			MarkerView mv = markerView;
			if (mv.getParent() != null) {
				((ViewGroup) mv.getParent()).removeView(mv);
			}
			mv.getFloatFrame().setVisibility(View.GONE);
			lineChart.setMarkerView(mv);
			if (circleView.getParent() != null) {
				((ViewGroup) circleView.getParent()).removeView(circleView);
			}
			circleView.getCircleFrame().setVisibility(View.GONE);
			lineChart.setmCircleView(circleView);
			lineChart.setVisibleXRange(6.5f);
			lineChart.setChartMargins(18f);
			YAxis leftAxis = lineChart.getAxisLeft();
			leftAxis.setLabelCount(6);
			leftAxis.setStartAtZero(false);
			ArrayList<String> xVals = new ArrayList<String>(); // 添加x坐标的值
			for (int x = 0; x < xObject.length; x++) {
				if ((xObject[x]).length() != 4) {
					xVals.add(xObject[x]);
					continue;
				}
			}
			ArrayList<Entry> yVals = new ArrayList<Entry>(); // 添加y坐标的值
			for (int y = 0; y < yObject.length; y++) {
				yVals.add(new Entry(yObject[y], y));
			}

			if (xVals.size() > yVals.size()) {
				for (int i = yVals.size(); i < xVals.size(); i++) {
					xVals.remove(i);
				}
			} else if (yVals.size() > xVals.size()) {
				for (int i = xVals.size(); i < yVals.size(); i++) {
					yVals.remove(i);
				}
			}

			LineDataSet lineDataSet = new LineDataSet(yVals, "");
			lineDataSet.setColor(Color.parseColor("#eb4a19")); // 曲线的颜色
			lineDataSet.setCircleColor(Color.parseColor("#eb4a19")); // 圆的颜色
			lineDataSet.setLineWidth(2f); // 线宽
			lineDataSet.setCircleSize(6f); // 圆的半径
			lineDataSet.setDrawCircleHole(true); // true为空心，false为实心
			lineDataSet.setValueTextSize(12f); // 数值的字体大小
			lineDataSet.setFillAlpha(25); // 填充色的透明度，0为完全透明
//			lineDataSet.setFillColor(Color.parseColor("#ea3636")); // 填充色
			ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
			dataSets.add(lineDataSet);
			LineData data = new LineData(xVals, dataSets);
			lineChart.setData(data);
			if (xVals.size() ==1) {
				lineChart.animateX(10, Easing.EasingOption.EaseInOutQuart);
			} else {
				lineChart.animateX(2000, Easing.EasingOption.EaseInOutQuart);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lineChart;
	}
}
