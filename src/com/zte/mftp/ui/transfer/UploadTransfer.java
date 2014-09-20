package com.zte.mftp.ui.transfer;

import com.zte.mftp.ui.MFtpUi;
import com.zte.mftp.work.MFtper;
import com.zte.mftp.work.TransferInfo;


public class UploadTransfer implements Transfer {

	private MFtpUi mftper = null;
	private long time = 0;
	
	public UploadTransfer(MFtpUi mftper) {
		this.mftper = mftper;
	}
	
	public int getInterval() {
		// TODO Auto-generated method stub
		return mftper.getInterval();
	}

	public long getTransfered() {
		// TODO Auto-generated method stub
		return MFtper.getInstance().getTransfered();
	}

	public long getFileSize() {
		// TODO Auto-generated method stub
		return MFtper.getInstance().getFileSize();
	}

	public int getTransferType() {
		// TODO Auto-generated method stub
		return TRANSFER_TYPE_UPLOAD;
	}

	public void setTime(long time) {
		this.time = time;
	}
	
	public long getTime() {
		return time;
	}

	public boolean transfer(TransferInfo info) {
		// TODO Auto-generated method stub
		return MFtper.getInstance().upload(info);
	}
}
