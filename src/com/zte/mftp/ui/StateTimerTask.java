package com.zte.mftp.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;

import com.zte.mftp.ui.transfer.Transfer;

public class StateTimerTask extends TimerTask {

	private Transfer state = null;
	private long transfered = 0;
	private long time = 0;
	
	public long getTime() {
		return this.time;
	}
	
	public StateTimerTask(Transfer state) {
		this.state = state;
	}
	
	public void run() { 
		
		if(null == state || state.getTransferType() == Transfer.TRANSFER_TYPE_NO)
		{
			this.time += 2;
			return;
		}
		
		//settime
		this.time += state.getInterval();
		state.setTime(this.time);
		
		long transfered = state.getTransfered();
		long total = state.getFileSize();
		
		if(total <= 0)
			return;
		
		int percent = (int) (transfered * 100 / total);
		
		double speed = (double)(transfered - this.transfered) * 8.0 / (1024 * 1024 * state.getInterval());
		String rate = String.format("rate = %.1f mbps", speed);
		
		//update
		this.transfered = transfered;
		
		String trans_type_str = "";
		if(state.getTransferType() == Transfer.TRANSFER_TYPE_UPLOAD)
			trans_type_str = "upload";
		else
			trans_type_str = "download";

		System.out.println(trans_type_str
							+ " "
							+ transfered 
							+ " bytes / " 
							+ total 
							+ " bytes"
							+ "  complete = " 
							+ percent
							+ "%  "
							+ rate
							+ "  time = "
							+ state.getTime()
							+ " s");	
	}
}