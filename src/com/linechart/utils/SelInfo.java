package com.linechart.utils;

import com.linechart.data.DataSet;

public class SelInfo {
	public float val;
	public int dataSetIndex;
	public DataSet<?> dataSet;

	public SelInfo(float val, int dataSetIndex, DataSet<?> set) {
		this.val = val;
		this.dataSetIndex = dataSetIndex;
		this.dataSet = set;
	}
}
