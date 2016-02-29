package com.linechart.components;

import android.graphics.Paint;

import com.linechart.utils.Utils;

import java.util.List;

public class Legend extends ComponentBase {
	private int[] mColors;
	private String[] mLabels;
	private LegendPosition mPosition = LegendPosition.BELOW_CHART_LEFT;

	private LegendForm mShape = LegendForm.SQUARE;

	private float mFormSize = 8.0F;

	private float mXEntrySpace = 6.0F;

	private float mYEntrySpace = 5.0F;

	private float mFormToTextSpace = 5.0F;

	private float mStackSpace = 3.0F;

	public float mNeededWidth = 0.0F;

	public float mNeededHeight = 0.0F;

	public float mTextHeightMax = 0.0F;

	public float mTextWidthMax = 0.0F;

	public Legend() {
		this.mFormSize = Utils.convertDpToPixel(8.0F);
		this.mXEntrySpace = Utils.convertDpToPixel(6.0F);
		this.mYEntrySpace = Utils.convertDpToPixel(5.0F);
		this.mFormToTextSpace = Utils.convertDpToPixel(5.0F);
		this.mTextSize = Utils.convertDpToPixel(10.0F);
		this.mStackSpace = Utils.convertDpToPixel(3.0F);
	}

	public void setColors(List<Integer> colors) {
		this.mColors = Utils.convertIntegers(colors);
	}

	public void setLabels(List<String> labels) {
		this.mLabels = Utils.convertStrings(labels);
	}

	public float getMaximumEntryWidth(Paint p) {
		float max = 0.0F;

		for (int i = 0; i < this.mLabels.length; i++) {
			if (this.mLabels[i] == null)
				continue;
			float length = Utils.calcTextWidth(p, this.mLabels[i]);

			if (length > max) {
				max = length;
			}
		}

		return max + this.mFormSize + this.mFormToTextSpace;
	}

	public float getMaximumEntryHeight(Paint p) {
		float max = 0.0F;

		for (int i = 0; i < this.mLabels.length; i++) {
			if (this.mLabels[i] == null)
				continue;
			float length = Utils.calcTextHeight(p, this.mLabels[i]);

			if (length > max) {
				max = length;
			}
		}
		return max;
	}

	public int[] getColors() {
		return this.mColors;
	}

	public LegendPosition getPosition() {
		return this.mPosition;
	}

	public LegendForm getForm() {
		return this.mShape;
	}

	public float getFormSize() {
		return this.mFormSize;
	}

	public float getFullWidth(Paint labelpaint) {
		float width = 0.0F;

		for (int i = 0; i < this.mLabels.length; i++) {
			if (this.mLabels[i] != null) {
				if (this.mColors[i] != -2) {
					width += this.mFormSize + this.mFormToTextSpace;
				}
				width += Utils.calcTextWidth(labelpaint, this.mLabels[i]);

				if (i < this.mLabels.length - 1)
					width += this.mXEntrySpace;
			} else {
				width += this.mFormSize;
				if (i < this.mLabels.length - 1)
					width += this.mStackSpace;
			}
		}
		return width;
	}

	public float getFullHeight(Paint labelpaint) {
		float height = 0.0F;

		for (int i = 0; i < this.mLabels.length; i++) {
			if (this.mLabels[i] == null)
				continue;
			height += Utils.calcTextHeight(labelpaint, this.mLabels[i]);

			if (i < this.mLabels.length - 1) {
				height += this.mYEntrySpace;
			}
		}
		return height;
	}

	public void calculateDimensions(Paint labelpaint) {
		if ((this.mPosition == LegendPosition.RIGHT_OF_CHART)
				|| (this.mPosition == LegendPosition.RIGHT_OF_CHART_CENTER)
				|| (this.mPosition == LegendPosition.LEFT_OF_CHART)
				|| (this.mPosition == LegendPosition.LEFT_OF_CHART_CENTER)
				|| (this.mPosition == LegendPosition.PIECHART_CENTER)) {
			this.mNeededWidth = getMaximumEntryWidth(labelpaint);
			this.mNeededHeight = getFullHeight(labelpaint);
			this.mTextWidthMax = this.mNeededWidth;
			this.mTextHeightMax = getMaximumEntryHeight(labelpaint);
		} else {
			this.mNeededWidth = getFullWidth(labelpaint);
			this.mNeededHeight = getMaximumEntryHeight(labelpaint);
			this.mTextWidthMax = getMaximumEntryWidth(labelpaint);
			this.mTextHeightMax = this.mNeededHeight;
		}
	}

	public static enum LegendDirection {
		LEFT_TO_RIGHT, RIGHT_TO_LEFT;
	}

	public static enum LegendForm {
		SQUARE, CIRCLE, LINE;
	}

	public static enum LegendPosition {
		RIGHT_OF_CHART, RIGHT_OF_CHART_CENTER, RIGHT_OF_CHART_INSIDE, LEFT_OF_CHART, LEFT_OF_CHART_CENTER, LEFT_OF_CHART_INSIDE, BELOW_CHART_LEFT, BELOW_CHART_RIGHT, BELOW_CHART_CENTER, PIECHART_CENTER;
	}
}
