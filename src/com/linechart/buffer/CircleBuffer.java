package com.linechart.buffer;

import java.util.List;

import com.linechart.data.Entry;

public class CircleBuffer extends AbstractBuffer<Entry> {
	public CircleBuffer(int size) {
		super(size);
	}

	protected void addCircle(float x, float y) {
		this.buffer[(this.index++)] = x;
		this.buffer[(this.index++)] = y;
	}

	public void feed(List<Entry> entries) {
		int size = (int) Math.ceil((this.mTo - this.mFrom) * this.phaseX + this.mFrom);

		for (int i = this.mFrom; i < size; i++) {
			Entry e = (Entry) entries.get(i);
			addCircle(e.getXIndex(), e.getVal() * this.phaseY);
		}
		reset();
	}
}
