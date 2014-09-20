package com.zte.mftp.work;

public class TransferInfo {

	private String host; 
	private int port;
	private String user; 
	private String password;
	private String local_ip; 
	private String src; 
	private String dest;
	private int thread_cnt;
	private long upload_size;
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLocalIp() {
		return local_ip;
	}
	public void setLocalIp(String local_ip) {
		this.local_ip = local_ip;
	}
	public String getSrcFile() {
		return src;
	}
	public void setSrcFile(String src) {
		this.src = src;
	}
	public String getDestFile() {
		return dest;
	}
	public void setDestFile(String dest) {
		this.dest = dest;
	}
	public int getThreadCnt() {
		return thread_cnt;
	}
	public void setThreadCnt(int thread_cnt) {
		this.thread_cnt = thread_cnt;
	}
	public long getUploadSize() {
		return upload_size;
	}
	public void setUploadSize(long upload_size) {
		this.upload_size = upload_size;
	}
	
	public TransferInfo(String host, int port, String user, 
			String password, String local_ip, 
			String src, String dest, int thread_cnt, long upload_size) {
		setHost(host);
		setPort(port);
		setUser(user);
		setPassword(password);
		setLocalIp(local_ip);
		setSrcFile(src);
		setDestFile(dest);
		setThreadCnt(thread_cnt);
		setUploadSize(upload_size);
	}
}
