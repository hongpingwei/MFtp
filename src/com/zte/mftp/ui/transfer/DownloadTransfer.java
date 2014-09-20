package com.zte.mftp.ui.transfer;

import com.zte.mftp.ui.MFtpUi;
import com.zte.mftp.work.MFtper;
import com.zte.mftp.work.TransferInfo;


public class DownloadTransfer implements Transfer {

	private MFtpUi mftpui = null;
	private long time = 0;
	
	public DownloadTransfer(MFtpUi mftper) {
		this.mftpui = mftper;
	}
	
	public int getInterval() {
		// TODO Auto-generated method stub
		return mftpui.getInterval();
	}

	public long getTransfered() {
		// TODO Auto-generated method stub
		//return mftper.getMFtp().getMtDownloadManager().getTransfered();
		return MFtper.getInstance().getTransfered();
	}

	public long getFileSize() {
		// TODO Auto-generated method stub
		//return mftper.getMFtp().getMtDownloadManager().getFileSize();
		return MFtper.getInstance().getFileSize();
	}

	public int getTransferType() {
		// TODO Auto-generated method stub
		return TRANSFER_TYPE_DOWNLOAD;
	}
	
	public void setTime(long time) {
		this.time = time;
	}
	
	public long getTime() {
		return time;
	}

	public boolean transfer(TransferInfo info) {
		// TODO Auto-generated method stub
		return MFtper.getInstance().download(info);
	}

}
