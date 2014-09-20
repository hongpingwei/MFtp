package com.zte.mftp.work;

import com.zte.mftp.MFtpClient;
import com.zte.util.UtilFile;

public class MFtpDownloadTask extends MFtpTask {

	private long start = 0;
	private long end = 0;
	private long transfered = 0;
	private String dest = "";

	public MFtpDownloadTask(int task_no, TaskListener listener) {
		super(listener);
		setTaskNo(task_no);
		// TODO Auto-generated constructor stub
	}
	
	public synchronized String getAnotherDownLoadFileName(String origin_name) {
		
		String src_name = UtilFile.getFileName(origin_name);
		String dest_name = src_name;
		int i = 0;
		while(true) {
			long size = getMFtpClient().getFileSize(dest_name);
			
			if(size < 0) {
				//not exist
				System.out.println(dest_name);
				return dest_name;
			}
			dest_name = String.format("%s (%d)", src_name, i);
			++i;
		}
	}
	
	public boolean transfer(String src, String dest, long start, long end) {
		
		resetState(start, end);
		if(!isConnect())
			return false;
		
		this.dest = dest;
		return getMFtpClient().downloadEx(src, start);
	}

	public void transferred(int length) {
		// TODO Auto-generated method stub
		if(!isRun())
			return;
		
		byte[] buf = getMFtpClient().getOutputStream().toByteArray();
		int to_be_write = buf.length;
		if(getStart() + buf.length >= getEnd()) {
			to_be_write = (int) (getEnd() - getStart());
		}
		
		//write file
		if ("" != this.dest) {
			UtilFile.overwirteBinaryFile(dest, buf, 0,
				to_be_write, getStart());
		}
		
		setStart(getStart() + to_be_write);
		setTransfered(getTransfered() + to_be_write);
		
	    if(getStart() >= getEnd()) {
	    	getMFtpClient().abortTransfer();
	    }
	    
	    getMFtpClient().getOutputStream().reset();
	}
}
