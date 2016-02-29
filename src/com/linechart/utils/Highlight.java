package com.linechart.utils;

public class Highlight {
	private int mXIndex;
	private int mDataSetIndex;
	private int mStackIndex = -1;

	public Highlight(int x, int dataSet) {
		this.mXIndex = x;
		this.mDataSetIndex = dataSet;
	}

	public Highlight(int x, int dataSet, int stackIndex) {
		this(x, dataSet);
		this.mStackIndex = stackIndex;
	}

	public int getDataSetIndex() {
		return this.mDataSetIndex;
	}

	public int getXIndex() {
		return this.mXIndex;
	}

	public int getStackIndex() {
		return this.mStackIndex;
	}

	public boolean equalTo(Highlight h) {
		if (h == null) {
			return false;
		}

		return (this.mDataSetIndex == h.mDataSetIndex) && (this.mXIndex == h.mXIndex)
				&& (this.mStackIndex == h.mStackIndex);
	}
}
