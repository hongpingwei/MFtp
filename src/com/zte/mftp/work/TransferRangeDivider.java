package com.zte.mftp.work;

import java.util.ArrayList;

public class TransferRangeDivider {
	
	private ArrayList<TransferRange> ranges = new ArrayList<TransferRange>();
	
	public ArrayList<TransferRange> getRanges() {
		return ranges;
	}
	
	public TransferRangeDivider(long start, long end, int divide_cnt) {
		long total = end - start;
		long part_len = total / divide_cnt;
		
		int i = 0;
		TransferRange range = null;
		for (i = 0; i < divide_cnt; ++i) {
			range = new TransferRange(i*part_len, (i+1)*part_len);
			ranges.add(range);
		}
		if(total%divide_cnt!=0 &&  i>0) {
			range.setEnd(i*part_len + total%divide_cnt);
		}
	}
	
	public static void main(String[] args) {
		
		TransferRangeDivider t = new TransferRangeDivider(0, 591, 5);
		ArrayList<TransferRange> range_list = t.getRanges();
		for(int i=0; i<range_list.size(); ++i) {
			System.out.println(range_list.get(i).getStart() + " ~ " + range_list.get(i).getEnd());
		}
	}
}
