package com.linechart.interfaces;

import com.linechart.data.LineData;
import com.linechart.utils.FillFormatter;

public abstract interface LineDataProvider extends BarLineScatterCandleDataProvider
{
  public abstract LineData getLineData();

  public abstract void setFillFormatter(FillFormatter paramFillFormatter);

  public abstract FillFormatter getFillFormatter();
}
