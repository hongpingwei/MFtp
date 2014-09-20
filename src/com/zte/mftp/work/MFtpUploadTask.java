package com.zte.mftp.work;

import java.io.ByteArrayInputStream;

import com.zte.mftp.MFtpClient;
import com.zte.util.UtilFile;

public class MFtpUploadTask extends MFtpTask {

	public MFtpUploadTask(int task_no, TaskListener listener) {
		super(listener);
		// TODO Auto-generated constructor stub
		setTaskNo(task_no);
	}
	
	public synchronized String getUploadDestName(String origin_name) {
		
		String dest_name = origin_name;
		int i = 0;
		while(true) {
			long size = getMFtpClient().getFileSize(dest_name);
			
			if(size < 0) {
				//not exist
				return dest_name;
			}
			dest_name = String.format("%s.(%d)", origin_name, i);
			++i;
		}
	}
	
	public boolean transfer(String src, String dest, long start, long end) {
		
		resetState(start, end);
		if(!isConnect())
			return false;
		
		if(null == dest || "" == dest)
			return false;
		
		//multi thread should upload several files to server.
		String dname = dest;
		if (getTaskNo() > 0)
			dname += ".mt." + Integer.toString(getTaskNo());
		
		return getMFtpClient().uploadEx(src, dname, end - start);
	}
	
	public void transferred(int length) {
		// TODO Auto-generated method stub
		if(!isRun())
			return;
		
		int to_be_write = length;
		if(getStart() + length >= getEnd()) {
			to_be_write = (int) (getEnd() - getStart());
		}
		
		setStart(getStart() + to_be_write);
		setTransfered(getTransfered() + to_be_write);
	    if(getStart() >= getEnd()) {
	    	getMFtpClient().abortTransfer();
	    }
	    
	    //reset input stream
	    if (getStart() + getMFtpClient().getPacketLen() > getEnd()) {
			getMFtpClient().resetInputStreamBytesToRead(getEnd() - getStart());
		} else {
			getMFtpClient().resetInputStream();
		}
	}
}
