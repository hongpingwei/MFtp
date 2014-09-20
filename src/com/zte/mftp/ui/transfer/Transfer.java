package com.zte.mftp.ui.transfer;

import com.zte.mftp.work.TransferInfo;

public interface Transfer {
	
	public static final int TRANSFER_TYPE_DOWNLOAD = 0;
	public static final int TRANSFER_TYPE_UPLOAD = 1;
	public static final int TRANSFER_TYPE_NO = 2;
	
	public int getInterval();
	public long getTransfered();
	public long getFileSize();
	public int getTransferType();
	public boolean transfer(TransferInfo info);
	
	public long getTime();
	public void setTime(long time);
	
}
