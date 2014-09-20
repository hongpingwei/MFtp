package com.zte.mftp.listener;

public interface MtFtpTransferListener {

	void process(int thread_no, long start, long end);
	void arrived(long len);
	void complete(int thread_no);
}
