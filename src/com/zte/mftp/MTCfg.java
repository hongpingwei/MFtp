package com.zte.mftp;

public class MTCfg {

	public static final String kMtDataFileExt = ".mt";
	public static final String kMtCfgFileExt = ".mt.cfg";
	public static final int kFilenameMaxLen = 256;
	
	private String host_ = null;
	private int port_ = 21;
	private String user_ = null;
	private String password_ = null;
	
	private String src_pathname_ = null;
	private String dest_pathname_ = null;
	private long file_size_ = 0;
	private int is_upload_ = 0;
	private int thread_cnt_ = 0;
	private long[] transfer_info_ = null;
	
	public String get_host() {
		return host_;
	}
	
	public int get_port() {
		return port_;
	}
	
	public String get_user() {
		return user_;
	}
	
	public String get_password() {
		return password_;
	}
	
	public String get_src_pathname() {
		return src_pathname_;
	}

	public String get_dest_pathname() {
		return dest_pathname_;
	}

	public long get_file_size() {
		return file_size_;
	}

	public int get_is_upload() {
		return is_upload_;
	}

	public int get_thread_cnt() {
		return thread_cnt_;
	}
	
	public long get_transfer_info_start(int thread_no) {
		return get_transfer_info(thread_no * 2);
	}
	
	public void set_transfer_info_start(int thread_no, long value) {
		set_transfer_info(thread_no * 2, value);
	}
	
	public long get_transfer_info_end(int thread_no) {
		return get_transfer_info(thread_no * 2 + 1);
	}
	
	public void set_transfer_info_end(int thread_no, long value) {
		set_transfer_info(thread_no * 2 + 1, value);
	}

	private long get_transfer_info(int i) {
		
		if (i>=0 && i< transfer_info_.length) {
			return transfer_info_[i];
		}
		else {
			return -1;
		}
	}
	
	private void set_transfer_info(int i, long value) {
		
		if (i>=0 && i< transfer_info_.length) {
			transfer_info_[i] = value;
		}
	}
	
	public long get_transfered() {
		long left = 0;
		for(int i=0; i<get_thread_cnt(); i++){
			left += get_transfer_info_end(i) - get_transfer_info_start(i);
		}
		return get_file_size() - left;
	}
	
	void InitCfg(String host, int port, String user, String password, 
			String src, String dest, long src_size, boolean is_upload, int thread_cnt) {
		
		host_ = host;
		port_ = port;
		user_ = user;
		password_ = password;
		
		src_pathname_ = src;
		dest_pathname_ = dest;
		file_size_ = src_size;
		if(is_upload)
			is_upload_ = 1;
		else
			is_upload_ = 0;
		thread_cnt_ = thread_cnt;
		if(thread_cnt_ > 0) {
			transfer_info_ = new long[thread_cnt_*2];
		}
	}
	
	void readCfg() {
		
	}
	
	void writeCfg() {
		
	}
	
	void clearCfg() {
		src_pathname_ = null;
		dest_pathname_ = null;
		file_size_ = 0;
		is_upload_ = 0;
		thread_cnt_ = 0;
		transfer_info_ = null;
	}
}
