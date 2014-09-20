package com.zte.mftp.work;

public class TransferRange {
	
	private long start;
	private long end;
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	
	public TransferRange(long start, long end) {
		setStart(start);
		setEnd(end);
	}
}
