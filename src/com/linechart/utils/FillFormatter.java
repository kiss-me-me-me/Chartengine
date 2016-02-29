package com.linechart.utils;

import com.linechart.data.LineData;
import com.linechart.data.LineDataSet;

public abstract interface FillFormatter
{
  public abstract float getFillLinePosition(LineDataSet paramLineDataSet, LineData paramLineData, float paramFloat1, float paramFloat2);
}
