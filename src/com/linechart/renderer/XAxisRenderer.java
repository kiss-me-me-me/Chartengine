package com.linechart.renderer;

import java.util.List;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.linechart.components.XAxis;
import com.linechart.utils.Transformer;
import com.linechart.utils.Utils;
import com.linechart.utils.ViewPortHandler;

public class XAxisRenderer extends AxisRenderer {
	protected XAxis mXAxis;

	public XAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
		super(viewPortHandler, trans);

		this.mXAxis = xAxis;

		this.mAxisLabelPaint.setColor(-16777216);
		this.mAxisLabelPaint.setTextAlign(Paint.Align.CENTER);
		this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0F));
	}

	public void computeAxis(float xValAverageLength, List<String> xValues) {
		this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
		this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());

		StringBuffer a = new StringBuffer();

		int max = Math.round(xValAverageLength + this.mXAxis.getSpaceBetweenLabels());

		for (int i = 0; i < max; i++) {
			a.append("h");
		}

		this.mXAxis.mLabelWidth = Utils.calcTextWidth(this.mAxisLabelPaint, a.toString());
		this.mXAxis.mLabelHeight = Utils.calcTextHeight(this.mAxisLabelPaint, "Q");
		this.mXAxis.setValues(xValues);
	}

	public void renderAxisLabels(Canvas c) {
		if ((!this.mXAxis.isEnabled()) || (!this.mXAxis.isDrawLabelsEnabled())) {
			return;
		}
		float yoffset = Utils.convertDpToPixel(this.mXAxis.getYOffset());

		this.mAxisLabelPaint.setTypeface(this.mXAxis.getTypeface());
		this.mAxisLabelPaint.setTextSize(this.mXAxis.getTextSize());
		this.mAxisLabelPaint.setColor(this.mXAxis.getTextColor());

		if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP) {
			drawLabels(c, this.mViewPortHandler.offsetTop() - yoffset);
		} else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM) {
			drawLabels(c, this.mViewPortHandler.contentBottom() + this.mXAxis.mLabelHeight + yoffset);
		} else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE) {
			drawLabels(c, this.mViewPortHandler.contentBottom() - yoffset);
		} else if (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE) {
			drawLabels(c, this.mViewPortHandler.offsetTop() + yoffset + this.mXAxis.mLabelHeight);
		} else {
			drawLabels(c, this.mViewPortHandler.offsetTop() - yoffset);
			drawLabels(c, this.mViewPortHandler.contentBottom() + this.mXAxis.mLabelHeight + yoffset * 1.6F);
		}
	}

	public void renderAxisLine(Canvas c) {
		calcXBounds(this.mTrans);
		if ((!this.mXAxis.isDrawAxisLineEnabled()) || (!this.mXAxis.isEnabled())) {
			return;
		}
		this.mAxisLinePaint.setColor(this.mXAxis.getGridColor());
		this.mAxisLinePaint.setStrokeWidth(this.mXAxis.getGridLineWidth());

		if ((this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP)
				|| (this.mXAxis.getPosition() == XAxis.XAxisPosition.TOP_INSIDE)
				|| (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED)) {
			c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentTop(),
					this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentTop(), this.mAxisLinePaint);
		}

		if ((this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM)
				|| (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTTOM_INSIDE)
				|| (this.mXAxis.getPosition() == XAxis.XAxisPosition.BOTH_SIDED))
			c.drawLine(this.mViewPortHandler.contentLeft(), this.mViewPortHandler.contentBottom(),
					this.mViewPortHandler.contentRight(), this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
	}

	protected void drawLabels(Canvas c, float pos) {
		float[] position = { 0.0F, 0.0F };

		int maxx = this.mMaxX;
		int minx = this.mMinX;

		if (maxx >= this.mXAxis.getValues().size()) {
			maxx = this.mXAxis.getValues().size() - 1;
		}
		if (minx < 0) {
			minx = 0;
		}
		for (int i = minx; i <= maxx; i += this.mXAxis.mAxisLabelModulus) {
			position[0] = i;

			this.mTrans.pointValuesToPixel(position);

			if (!this.mViewPortHandler.isInBoundsX(position[0]))
				continue;
			String label = (String) this.mXAxis.getValues().get(i);

			if (i == minx) {
				position[0] += Utils.calcTextWidth(this.mAxisLabelPaint, label) / 2.0F;
			}

			if (this.mXAxis.isAvoidFirstLastClippingEnabled()) {
				if ((i == this.mXAxis.getValues().size() - 1) && (this.mXAxis.getValues().size() > 1)) {
					float width = Utils.calcTextWidth(this.mAxisLabelPaint, label);

					if ((width > this.mViewPortHandler.offsetRight() * 2.0F)
							&& (position[0] + width > this.mViewPortHandler.getChartWidth()))
						position[0] -= width / 2.0F;
				} else if (i == 0) {
					float width = Utils.calcTextWidth(this.mAxisLabelPaint, label);
					position[0] += width / 2.0F;
				}
			}
			// c.drawText(label, position[0], pos, this.mAxisLabelPaint);
			// eden
			if (i == maxx) {
				c.drawText(label, position[0] - 20, pos, this.mAxisLabelPaint);
			} else if (i == 1) {
				c.drawText(label, position[0] + 20, pos, this.mAxisLabelPaint);
			} else {
				c.drawText(label, position[0], pos, this.mAxisLabelPaint);
			}
		}
	}

	public void renderGridLines(Canvas c) {
		if ((!this.mXAxis.isDrawGridLinesEnabled()) || (!this.mXAxis.isEnabled())) {
			return;
		}
		float[] position = { 0.0F, 0.0F };

		this.mGridPaint.setColor(this.mXAxis.getGridColor());
		this.mGridPaint.setStrokeWidth(this.mXAxis.getGridLineWidth());
		this.mGridPaint.setPathEffect(this.mXAxis.getGridDashPathEffect());

		for (float i = this.mMinX + 1; i < this.mMaxX; i += 1.0F) {
			position[0] = i;

			this.mTrans.pointValuesToPixel(position);

			if ((position[0] < this.mViewPortHandler.offsetLeft())
					|| (position[0] > this.mViewPortHandler.getChartWidth()))
				continue;
			// eden
			// 去除纵坐标的坚线
			// c.drawLine(position[0], this.mViewPortHandler.offsetTop(),
			// position[0],
			// this.mViewPortHandler.contentBottom(), this.mGridPaint);
		}
	}

	public void renderLimitLines(Canvas c) {
	}
}
