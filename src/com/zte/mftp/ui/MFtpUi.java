package com.zte.mftp.ui;

import java.util.TimerTask;

import com.zte.mftp.MFtpClient;
import com.zte.mftp.ui.transfer.DownloadTransfer;
import com.zte.mftp.ui.transfer.NoTransfer;
import com.zte.mftp.ui.transfer.Transfer;
import com.zte.mftp.ui.transfer.UploadTransfer;
import com.zte.mftp.work.MFtper;
import com.zte.mftp.work.TransferInfo;
import com.zte.util.*;

public class MFtpUi {
	
	// KEYS
	public static final String KEY_HELP = "h";
	public static final String KEY_UPLOAD = "u";
	public static final String KEY_HOST = "hs";
	public static final String KEY_PORT = "p";
	public static final String KEY_USER = "ur";
	public static final String KEY_PASSWORD = "pw";
	public static final String KEY_LOCALIP = "l";
	public static final String KEY_SRC_FILE = "s";
	public static final String KEY_DEST_FILE = "d";
	public static final String KEY_UPLOAD_FILE_SIZE = "us";
	public static final String KEY_DOWNLOAD_MT = "n";
	public static final String KEY_DOWNLOAD_LOCAL_FILE = "lf";
	public static final String KEY_REPORT_INTERVAL = "i";
	
	public static final int TRANSFER_TYPE_DOWNLOAD = 0;
	public static final int TRANSFER_TYPE_UPLOAD = 1;
	
	//State Timer
	private UtilTimer state_timer = null; 
	
	private int interval = 2;
	
	private Transfer transfer = new NoTransfer();
	
	public static void main(String[] args) {
		
		String cmdline = "mftper ";
		for (int i = 0; i < args.length; i++) {
			cmdline += args[i] + " ";
		}
		
		//cmdline = "mftper -hs 10.11.174.124 -u -ur test -pw test -d test.rmvb -us 20000000";
		//cmdline = "mftper -hs 10.11.174.124 -ur test -pw test -s 12g.dat";
		//cmdline = "mftper -hs 10.89.168.2 -u -ur user -pw BStest -us 200000000 -d testfile -n 5 -l 10.90.145.146";
		//cmdline = "mftper -hs 10.89.168.2 -ur user -pw BStest -s 1.dat -n 5";
		//cmdline = "mftper -hs 10.90.145.2 -ur user -pw BStest -s 1.dat -n 5";
		
		MFtpUi mftper = new MFtpUi(cmdline);
		System.exit(0);
	}
	
	public MFtpUi(String cmdline) {
		
		UtilCmdlineParser parser = new UtilCmdlineParser(cmdline, false, " -", " ");
		processCmdline(parser);
	}
	
	public int getInterval() {
		return this.interval;
	}
	
	void showHelp() {
		System.out.println("Usage: mftper [-hs ip] [-ur name] [-pw password] [-s src file] [-d dest file] [-n thread-count]");
		System.out.println("");
		System.out.println("Options:");
		System.out.println("  -h        help.");
		System.out.println("  -u        upload, if not exist means download.");
		System.out.println("  -hs       host ip.");
		System.out.println("  -p        host port, default is 21.");
		System.out.println("  -ur       user.");
		System.out.println("  -pw       password.");
		System.out.println("  -l        local ip to bind on.");
		System.out.println("  -s        src file path.");
		System.out.println("  -d        dest file path.");
		System.out.println("  -us       if upload, it means upload size, meaningless in download.");
		System.out.println("  -n        download threads, meaningless in upload.");
		//System.out.println("  -lf       download local file path, meaningless in upload.");
		System.out.println("  -i        report interval(sec).");
	}
	
	public boolean processCmdline(UtilCmdlineParser parser) {
		
		//help
		if(parser.hasKey(KEY_HELP)) {
			showHelp();
			return true;
		}
		
		String host = parser.getValue(KEY_HOST);
		String port = parser.getValue(KEY_PORT);
		String local_ip = parser.getValue(KEY_LOCALIP);
		String user = parser.getValue(KEY_USER);
		String password = parser.getValue(KEY_PASSWORD);
		String mt = parser.getValue(KEY_DOWNLOAD_MT);
		String src = parser.getValue(KEY_SRC_FILE);
		String dest = parser.getValue(KEY_DEST_FILE);
		String upload_size = parser.getValue(KEY_UPLOAD_FILE_SIZE);
		//String local_file = parser.getValue(KEY_DOWNLOAD_LOCAL_FILE);
		String report_interval = parser.getValue(KEY_REPORT_INTERVAL);
		
		//not enough params
		if("" == host || "" == user || "" == password) {
			
			System.out.println("failed. need params "
								+ "\"" + KEY_HOST + "\", "
								+ "\"" + KEY_USER + "\", "
								+ "\"" + KEY_PASSWORD
								+ "\" at least.");
			return false;
		}
		
		int port_num = UtilLexicalCast.ParseInt(port, 21);
		long upload_size_num = UtilLexicalCast.ParseLong(upload_size, 1024*1024*128);
		int mt_num = UtilLexicalCast.ParseInt(mt, 1);
		interval = UtilLexicalCast.ParseInt(report_interval, 2);
		
		//ensure thread cnt bigger or equal 1
		if(mt_num < 1)
			mt_num = 1;
		
		boolean ret = false;
		
		if(parser.hasKey(KEY_UPLOAD))
			transfer = new UploadTransfer(this);
		else
			transfer = new DownloadTransfer(this);
		
		//timer start
		this.state_timer = new UtilTimer(new StateTimerTask(transfer));
		state_timer.start(0, interval * 1000);
		
		//transfer
		ret = transfer.transfer(
				new TransferInfo(host, port_num, user, password, local_ip, src, dest, mt_num, upload_size_num)
				);
		
		//timer stop
		state_timer.stop();
		
		//end, output result
		if(ret && transfer.getTransfered()>0 && transfer.getTransfered() == transfer.getFileSize())
			System.out.println("complete = 100%  time = "+ transfer.getTime() +" s.");
		else
			System.out.println("failed. (error " + MFtpClient.getErrorValue() + ")");
		
		return ret;
	}
}
