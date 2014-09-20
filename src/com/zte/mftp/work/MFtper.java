package com.zte.mftp.work;

import java.io.File;
import java.util.ArrayList;

public class MFtper implements TaskListener {
	
	private MFtpTask task = new MFtpTask(null);
	private static MFtper _INSTANCE = null;
	// thread lock
	private final Object lock = new Object();
	private TaskManager task_manager = new TaskManager();
	private boolean run = false;
	private long file_size = 0; 

	public synchronized static MFtper getInstance() {
		if (_INSTANCE == null) {
			_INSTANCE = new MFtper();
		}
		return _INSTANCE;
	}
	
	boolean connect(String host, int port, String user, String password, String local_ip) {
		return task.connect(host, port, user, password, local_ip);
	}
	
	long getHostFileSize(String file) {
		return task.getSize(file);
	}
	
	void disconnect() {
		task.disconnect();
	}
	
	public boolean isRunning() {
		return task_manager.isRunning();
	}
	
	public long getFileSize() {
		return file_size;
	}
	
	private void threadWait() {
		
		synchronized (lock) {
			try {
				lock.wait();

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void threadNotify() {
		synchronized (lock) {
			lock.notifyAll();
		}
	}
	
	public long getTransfered() {
		long transfered = 0;
		for(int i=0; i<task_manager.getTaskCnt(); ++i) {
			transfered += task_manager.getTask(i).getTransfered();
		}
		return transfered;
	}
	
	public boolean upload(TransferInfo transfer_info) {
		file_size = 0;
		if(!connect(transfer_info.getHost(), 
				transfer_info.getPort(), 
				transfer_info.getUser(),
				transfer_info.getPassword(), 
				transfer_info.getLocalIp()))
		{
			disconnect();
			return false;
		}
		
		//src file
		if("" == transfer_info.getSrcFile()) {
			file_size = transfer_info.getUploadSize();
		}
		else {
			File file = new File(transfer_info.getSrcFile());
			file_size = file.length();
			if(file_size <= 0)
			{
				disconnect();
				return false;
			}
		}
		
		//cut main connect
		disconnect();
		
		task_manager.cleanup();
//		task_manager.addTask(
//				new TaskRunner(this, 1, new MFtpUploadTask(1, this), transfer_info, new TransferRange(0, file_size))
//				);
//		task_manager.run();
		TransferRangeDivider divider = new TransferRangeDivider(0, file_size, transfer_info.getThreadCnt());
		for(int i=0; i<divider.getRanges().size(); ++i) {
			
			task_manager.addTask(
					new TaskRunner(this, i, new MFtpUploadTask(i, this), transfer_info, divider.getRanges().get(i))
					);
		}
		task_manager.run();
		
		threadWait();
		
		return getTransfered() == getFileSize();
	}
	
	public boolean download(TransferInfo transfer_info) {
		
		file_size = 0;
		if(!connect(transfer_info.getHost(), 
				transfer_info.getPort(), 
				transfer_info.getUser(),
				transfer_info.getPassword(), 
				transfer_info.getLocalIp()))
		{
			disconnect();
			return false;
		}
		
		//src file
		file_size = getHostFileSize(transfer_info.getSrcFile());
		if(file_size <= 0)
		{
			disconnect();
			return false;
		}
		
		//cut main connect
		disconnect();
		
		TransferRangeDivider divider = new TransferRangeDivider(0, file_size, transfer_info.getThreadCnt());
		task_manager.cleanup();
		for(int i=0; i<divider.getRanges().size(); ++i) {
		
			task_manager.addTask(
					new TaskRunner(this, i, new MFtpDownloadTask(i, this), transfer_info, divider.getRanges().get(i))
					);
		}
		task_manager.run();
		
		threadWait();
		
		return getTransfered() == getFileSize();
	}
	
	public void complete(int task_no) {
		// TODO Auto-generated method stub
		if(!isRunning()) {
			threadNotify();
		}
	}
}

class TaskManager {
	
	private ArrayList<TaskRunner> task_array = new ArrayList<TaskRunner>();

	public void addTask(TaskRunner taskrunner) {
		task_array.add(taskrunner);
	}
	
	public int getTaskCnt() {
		return task_array.size();
	}
	
	public void cleanup() {
		task_array.clear();
	}
	
	public MFtpTask getTask(int i) {
		if(i>=0 && i<task_array.size())
			return task_array.get(i).getTask();
		else
			return null;
	}
	
	public boolean isRunning() {
		int run = 0;
		for(int i=0; i<task_array.size(); ++i) {
			if(task_array.get(i).isRunning())
				++run;
		}
		if(run > 0)
			return true;
		
		return false;
	}
	
	void run() {
		for(int i=0; i<task_array.size(); ++i) {
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Thread thread  = new Thread(task_array.get(i));
			thread.start();
		}
	}
}

class TaskRunner implements Runnable {

	private MFtper mftper = null;
	private MFtpTask task = null;
	private TransferInfo info = null;
	private TransferRange range = null;
	
	public TaskRunner(MFtper mftper, int task_no, MFtpTask task, TransferInfo info, TransferRange range) {
		this.task = task;
		this.info = info;
		this.range = range;
		this.mftper = mftper;
	}
	
	public boolean isRunning() {
		return task.isRun();
	}
	
	public MFtpTask getTask() {
		return task;
	}
	
	public long getTransfered() {
		return task.getTransfered();
	}
	
	public void run() {
		// TODO Auto-generated method stub
		if(!task.connect(info.getHost(), 
				info.getPort(), 
				info.getUser(), 
				info.getPassword(), 
				info.getLocalIp()))
			return;
		
		task.transfer(info.getSrcFile(), info.getDestFile(), range.getStart(), range.getEnd());
	}
}
