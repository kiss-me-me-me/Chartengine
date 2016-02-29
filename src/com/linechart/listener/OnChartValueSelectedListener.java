package com.linechart.listener;

import com.linechart.data.Entry;
import com.linechart.utils.Highlight;

public abstract interface OnChartValueSelectedListener
{
  public abstract void onValueSelected(Entry paramEntry, int paramInt, Highlight paramHighlight);

  public abstract void onNothingSelected();
}
