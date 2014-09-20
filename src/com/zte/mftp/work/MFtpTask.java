package com.zte.mftp.work;

import java.util.LinkedList;
import java.util.Queue;

import it.sauronsoftware.ftp4j.FTPDataTransferListener;

import com.zte.mftp.MFtpClient;

public class MFtpTask implements FTPDataTransferListener {
	
	private int task_no = 0;
	private MFtpClient mftp_client = new MFtpClient(this);
	private String host = "";
	private int port = 0;
	private String user = "";
	private String password = "";
	private String local_ip = "";
	private boolean connect = false;
	
	private long start = 0;
	private long end = 0;
	private long transfered = 0;
	private boolean run = false;
	private TaskListener task_listener = null;
	
//	private Thread thread = new Thread(this);
//	private Queue<Integer> queue_msg_ = new LinkedList<Integer>();
//	// thread lock
//	private final Object lock_ = new Object();
//	private boolean key_ = false;
	
	public void setListener(TaskListener task_listener) {
		this.task_listener = task_listener;
	}
	
	protected TaskListener getListener() {
		return task_listener;
	}
	
	public synchronized boolean isRun() {
		return run;
	}

	public synchronized void setRun(boolean run) {
		this.run = run;
	}

	public synchronized long getStart() {
		return start;
	}
	
	public synchronized void setStart(long start) {
		this.start = start;
	}

	public synchronized long getEnd() {
		return end;
	}
	
	public synchronized void setEnd(long end) {
		this.end = end;
	}
	
	public synchronized long getTransfered() {
		return transfered;
	}

	public synchronized void setTransfered(long transfered) {
		this.transfered = transfered;
	}

	public void resetState(long start, long end) {
		this.start = start;
		this.end = end;
		transfered = 0;
	}
	
	protected MFtpClient getMFtpClient() {
		return mftp_client;
	}
	
	public int getTaskNo() {
		return task_no;
	}
	
	public void setTaskNo(int task_no) {
		this.task_no = task_no;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getLocal_ip() {
		return local_ip;
	}

	public boolean isConnect() {
		return connect;
	}
	
	//ctor
	public MFtpTask(TaskListener listener) {
		this.task_no = -1;
		setListener(listener);
	}

	public boolean connect(String host, int port, String user, String password, String local_ip) {
		
		mftp_client.disconnect();
		
		connect = false;
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
		this.local_ip = local_ip;
		
		if (null == mftp_client.connect(host, port, local_ip)) {
			System.out.println("thread " + task_no + " connect failed.");
			return false;
		}
		
		if (!mftp_client.login(user, password)) {
			System.out.println("thread " + task_no + " login failed.");
			return false;
		}
		connect = true;
		
		return true;
		//mftp_client_.downloadEx(src_file_, start_, end_);
	}
	
	public long getSize(String file) {
		if(isConnect()){
			return mftp_client.getFileSize(file);
		}
		
		return 0;
	}
	
	public void disconnect() {
		// TODO Auto-generated method stub
		mftp_client.disconnect();
	}
	
	public boolean transfer(String src, String dest, long start, long end) { return false; }
	
	//listeners
	public void started() { setRun(true); }
	
	public void transferred(int length) {}
	
	public void completed() 
	{
		setRun(false);
		if(null != getListener()) {
			getListener().complete(getTaskNo());
		}
	}
	
	public void aborted() { 
		setRun(false); 
		if(null != getListener()) {
			getListener().complete(getTaskNo());
		}
	}
	
	public void failed() { 
		setRun(false); 
		if(null != getListener()) {
			getListener().complete(getTaskNo());
		}
	}

}

