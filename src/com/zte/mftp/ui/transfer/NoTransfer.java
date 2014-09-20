package com.zte.mftp.ui.transfer;

import com.zte.mftp.work.TransferInfo;

public class NoTransfer implements Transfer {
	
	public int getInterval() {
		// TODO Auto-generated method stub
		return 2;
	}

	public long getTransfered() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getFileSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getTransferType() {
		// TODO Auto-generated method stub
		return TRANSFER_TYPE_NO;
	}
	
	public void setTime(long time) {
		;
	}
	
	public long getTime() {
		return 0;
	}

	public boolean transfer(TransferInfo info) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
