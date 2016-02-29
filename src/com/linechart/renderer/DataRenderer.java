package com.linechart.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.linechart.animation.ChartAnimator;
import com.linechart.data.DataSet;
import com.linechart.utils.Highlight;
import com.linechart.utils.Utils;
import com.linechart.utils.ViewPortHandler;

public abstract class DataRenderer extends Renderer {
	protected Paint mRenderPaint;
	protected Paint mHighlightPaint;
	protected Paint mDrawPaint;
	protected Paint mValuePaint;
    protected ChartAnimator mAnimator;

	public DataRenderer(ChartAnimator animator,ViewPortHandler viewPortHandler) {
		super(viewPortHandler);
        this.mAnimator = animator;

		this.mRenderPaint = new Paint(1);
		this.mRenderPaint.setStyle(Paint.Style.FILL);

		this.mDrawPaint = new Paint(4);

		this.mValuePaint = new Paint(1);
		this.mValuePaint.setColor(Color.rgb(63, 63, 63));
		this.mValuePaint.setTextAlign(Paint.Align.CENTER);
		this.mValuePaint.setTextSize(Utils.convertDpToPixel(9.0F));

		this.mHighlightPaint = new Paint(1);
		this.mHighlightPaint.setStyle(Paint.Style.STROKE);
		this.mHighlightPaint.setStrokeWidth(2.0F);
		this.mHighlightPaint.setColor(Color.rgb(255, 187, 115));
	}

	public Paint getPaintRender() {
		return this.mRenderPaint;
	}

	protected void applyValueTextStyle(DataSet<?> set) {
		this.mValuePaint.setColor(set.getValueTextColor());
		this.mValuePaint.setTypeface(set.getValueTypeface());
		this.mValuePaint.setTextSize(set.getValueTextSize());
	}

	public abstract void initBuffers();

	public abstract void drawData(Canvas paramCanvas);

	public abstract void drawValues(Canvas paramCanvas);

	public abstract void drawExtras(Canvas paramCanvas);

	public abstract void drawHighlighted(Canvas paramCanvas, Highlight[] paramArrayOfHighlight);
}
