package com.linechart.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import com.linechart.charts.LineChart;
import com.linechart.components.YAxis;
import com.linechart.utils.PointD;
import com.linechart.utils.Transformer;
import com.linechart.utils.Utils;
import com.linechart.utils.ViewPortHandler;

public class YAxisRenderer extends AxisRenderer {
	protected YAxis mYAxis;

	public YAxisRenderer(ViewPortHandler viewPortHandler, YAxis yAxis, Transformer trans) {
		super(viewPortHandler, trans);

		this.mYAxis = yAxis;

		this.mAxisLabelPaint.setColor(-16777216);
		this.mAxisLabelPaint.setTextSize(Utils.convertDpToPixel(10.0F));
	}

	public void computeAxis(float yMin, float yMax) {
		if ((this.mViewPortHandler.contentWidth() > 10.0F) && (!this.mViewPortHandler.isFullyZoomedOutY())) {
			PointD p1 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(),
					this.mViewPortHandler.contentTop());
			PointD p2 = this.mTrans.getValuesByTouchPoint(this.mViewPortHandler.contentLeft(),
					this.mViewPortHandler.contentBottom());
			if (!this.mYAxis.isInverted()) {
				yMin = (float) p2.y;
				yMax = (float) p1.y;
			} else {
				yMin = (float) p1.y;
				yMax = (float) p2.y;
			}
		}
		computeAxisValues(yMin, yMax);
	}

	protected void computeAxisValues(float min, float max) {
		float yMin = min;
		float yMax = max;

		int labelCount = this.mYAxis.getLabelCount();
		double range = Math.abs(yMax - yMin);

		if ((labelCount == 0) || (range <= 0.0D)) {
			this.mYAxis.mEntries = new float[0];
			this.mYAxis.mEntryCount = 0;

			return;
		}
		double interval = this.mYAxis.scale;
		interval = Double.parseDouble(Utils.getFourDecimal(interval));

		double intervalMagnitude = Math.pow(10.0D, (int) Math.log10(interval));
		int intervalSigDigit = (int) (interval / intervalMagnitude);
		if (intervalSigDigit > 5) {
			interval = Math.floor(10.0D * intervalMagnitude);
		}

		if (this.mYAxis.isShowOnlyMinMaxEnabled()) {
			this.mYAxis.mEntryCount = 2;
			this.mYAxis.mEntries = new float[2];
			this.mYAxis.mEntries[0] = yMin;
			this.mYAxis.mEntries[1] = yMax;
		} else {
			double first = yMin;

			this.mYAxis.mEntryCount = labelCount;
			if (this.mYAxis.mEntries.length < labelCount) {
				this.mYAxis.mEntries = new float[labelCount];
			}

			double f = first;
			for (int i = 0; i < labelCount; i++) {
				this.mYAxis.mEntries[i] = (float) f;
				f += interval;
			}

		}

		if (interval < 1.0D)
			this.mYAxis.mDecimals = (int) Math.ceil(-Math.log10(interval));
		else
			this.mYAxis.mDecimals = 0;
	}

	public void renderAxisLabels(Canvas c) {
		if ((!this.mYAxis.isEnabled()) || (!this.mYAxis.isDrawLabelsEnabled())) {
			return;
		}
		float[] positions = new float[this.mYAxis.mEntryCount * 2];

		for (int i = 0; i < positions.length; i += 2) {
			positions[(i + 1)] = this.mYAxis.mEntries[(i / 2)];
		}

		this.mTrans.pointValuesToPixel(positions);

		this.mAxisLabelPaint.setTypeface(this.mYAxis.getTypeface());
		this.mAxisLabelPaint.setTextSize(this.mYAxis.getTextSize());
		this.mAxisLabelPaint.setColor(this.mYAxis.getTextColor());

		float xoffset = this.mYAxis.getXOffset();
		float yoffset = Utils.calcTextHeight(this.mAxisLabelPaint, "A") / 2.5F;

		YAxis.AxisDependency dependency = this.mYAxis.getAxisDependency();
		YAxis.YAxisLabelPosition labelPosition = this.mYAxis.getLabelPosition();

		float xPos = 0.0F;

		if (dependency == YAxis.AxisDependency.LEFT) {
			if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
				this.mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
				xPos = this.mViewPortHandler.offsetLeft() - xoffset;
			} else {
				this.mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
				xPos = this.mViewPortHandler.offsetLeft() + xoffset;
			}
		} else if (labelPosition == YAxis.YAxisLabelPosition.OUTSIDE_CHART) {
			this.mAxisLabelPaint.setTextAlign(Paint.Align.LEFT);
			xPos = this.mViewPortHandler.contentRight() + xoffset;
		} else {
			this.mAxisLabelPaint.setTextAlign(Paint.Align.RIGHT);
			xPos = this.mViewPortHandler.contentRight() - xoffset;
		}
		drawYLabels(c, xPos, positions, yoffset);
	}

	public void renderAxisLine(Canvas c) {
		if ((!this.mYAxis.isEnabled()) || (!this.mYAxis.isDrawAxisLineEnabled())) {
			return;
		}
		this.mAxisLinePaint.setColor(this.mYAxis.getGridColor());
		this.mAxisLinePaint.setStrokeWidth(this.mYAxis.getGridLineWidth());
		// eden
		// 去纵坐标最左边的坚线
		// if (this.mYAxis.getAxisDependency() == YAxis.AxisDependency.LEFT)
		// c.drawLine(this.mViewPortHandler.contentLeft(),
		// this.mViewPortHandler.contentTop(),
		// this.mViewPortHandler.contentLeft(),
		// this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
		// else
		// c.drawLine(this.mViewPortHandler.contentRight(),
		// this.mViewPortHandler.contentTop(),
		// this.mViewPortHandler.contentRight(),
		// this.mViewPortHandler.contentBottom(), this.mAxisLinePaint);
	}

	protected void drawYLabels(Canvas c, float fixedPosition, float[] positions, float offset) {
		for (int i = 0; i < this.mYAxis.mEntryCount; i++) {
			// eden
			// if (i == 0) {
			// continue;
			// }
			String text = this.mYAxis.getFormattedLabel(i);

			if ((!this.mYAxis.isDrawTopYLabelEntryEnabled()) && (i >= this.mYAxis.mEntryCount - 1)) {
				return;
			}
			// eden
			// c.drawText(text, fixedPosition, positions[(i * 2 + 1)] + offset,
			// this.mAxisLabelPaint);
			if (LineChart.percentage) {
				c.drawText(Utils.getFourDecimal(this.mYAxis.mEntries[i]) + "%", fixedPosition, positions[(i * 2 + 1)]
						+ offset, this.mAxisLabelPaint);
			} else {
				c.drawText(Utils.getFourDecimal(this.mYAxis.mEntries[i]), fixedPosition, positions[(i * 2 + 1)]
						+ offset, this.mAxisLabelPaint);
			}
		}
	}

	public void renderGridLines(Canvas c) {
		if ((!this.mYAxis.isDrawGridLinesEnabled()) || (!this.mYAxis.isEnabled())) {
			return;
		}

		float[] position = new float[2];

		this.mGridPaint.setColor(this.mYAxis.getGridColor());
		this.mGridPaint.setStrokeWidth(this.mYAxis.getGridLineWidth());
		this.mGridPaint.setPathEffect(this.mYAxis.getGridDashPathEffect());

		Path gridLinePath = new Path();

		float xoffset = this.mYAxis.getXOffset();
		float widthTextoffset = Utils.calcTextWidth(this.mAxisLabelPaint, this.mYAxis.getLongestLabel());

		for (int i = 0; i < this.mYAxis.mEntryCount; i++) {
			position[1] = this.mYAxis.mEntries[i];
			this.mTrans.pointValuesToPixel(position);

			if (i > 0) {
				// eden
				// gridLinePath.moveTo(this.mViewPortHandler.offsetLeft() +
				// xoffset * 2.0F + widthTextoffset, position[1]);
				gridLinePath.moveTo(xoffset * 2.0F + widthTextoffset, position[1]);
				gridLinePath.lineTo(this.mViewPortHandler.contentRight() + xoffset * 2.0F + widthTextoffset,
						position[1]);
			} else {
				gridLinePath.moveTo(this.mViewPortHandler.offsetLeft(), position[1]);
				gridLinePath.lineTo(this.mViewPortHandler.contentRight(), position[1]);
			}
			c.drawPath(gridLinePath, this.mGridPaint);

			gridLinePath.reset();
		}
	}

	public void renderLimitLines(Canvas c) {
	}
}
