package com.linechart.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linechart.charts.LineChart;
import com.linechart.data.Entry;
import com.linechart.utils.MsgChart;
import com.linechart.utils.Utils;

@SuppressLint({ "ViewConstructor" })
public class CircleView extends RelativeLayout {
	private TextView circleFrame;

	public CircleView(Context context, int layoutResource, int id) {
		super(context);
		setupLayoutResource(layoutResource);

		this.circleFrame = ((TextView) findViewById(id));
	}

	private void setupLayoutResource(int layoutResource) {
		View inflated = LayoutInflater.from(getContext()).inflate(layoutResource, this);

		inflated.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
		inflated.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));

		inflated.layout(0, 0, inflated.getMeasuredWidth(), inflated.getMeasuredHeight());
	}

	public void draw(Canvas canvas, float posx, float posy, int position) {
		// posx += (position == 0 ? position : getXOffset());
		posx += getXOffset();
		posy += getYOffset();
		canvas.translate(posx, posy);
		draw(canvas);
		canvas.translate(-posx, -posy);
	}

	public int getXOffset() {
		return -(getWidth() / 2);
	}

	public int getYOffset() {
		return -getHeight();
	}

	public TextView getCircleFrame() {
		return circleFrame;
	}

	public void setCircleFrame(TextView circleFrame) {
		this.circleFrame = circleFrame;
	}
}
