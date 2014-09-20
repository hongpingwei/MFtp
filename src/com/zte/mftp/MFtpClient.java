package com.zte.mftp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

public class MFtpClient {

	private FTPDataTransferListener transfer_listener_ = null;
	private FTPClient ftp_client_ = new FTPClient();
	
	
	private ByteArrayOutputStream output_stream_ = new ByteArrayOutputStream();
	
	private long upload_size_ = 0;
	private byte[] input_array_ = new byte[FTPClient.getSendAndReceiveBufferSize()];
	private ByteArrayInputStream input_stream_ = new ByteArrayInputStream(input_array_);
	
	public static final int ERR_UNKNOWN = 0;
	public static final int ERR_CONNECT_FAIL = 1;
	public static final int ERR_LOGIN_FAIL = 2;
	public static final int ERR_FILE_NOT_FOUND = 3;
	
	public static int s_error_value_ = ERR_UNKNOWN;
	
	public synchronized static int getErrorValue() {
		return s_error_value_;
	}
	
	public synchronized static void setErrorValue(int err) {
		s_error_value_ = err;
	}
	
	public ByteArrayOutputStream getOutputStream() {
		return output_stream_;
	}
	
	public ByteArrayInputStream getInputStream() {
		return input_stream_;
	}
	
	public void resetInputStream() {
		input_stream_.reset();
	}
	
	public void resetInputStreamPos(long pos) {
		
		input_stream_.reset();
		if (pos > 0 && pos < FTPClient.getSendAndReceiveBufferSize()) {
			input_stream_.skip(pos);
		}
	}
	
	public void resetInputStreamBytesToRead(long bytes) {
		
		input_stream_.reset();
		if (bytes > 0 && bytes < FTPClient.getSendAndReceiveBufferSize()) {
			input_stream_.skip(FTPClient.getSendAndReceiveBufferSize() - bytes);
		}
	}
	
	public long getPacketLen() {
		return ftp_client_.getSendAndReceiveBufferSize();
	}
	
	public long getUploadSize() {
		return upload_size_;
	}
	
	public void disconnect() {
			
			try {
				ftp_client_.abortCurrentDataTransfer(false);
				ftp_client_.logout();
				ftp_client_.disconnect(false);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (FTPIllegalReplyException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} catch (FTPException e) {
				// TODO Auto-generated catch block
				//eat it
				//e.printStackTrace();
			} catch (Exception e) {
				//e.printStackTrace();
			}	
	}
	
	public void abortTransfer(){
		try {
			ftp_client_.abortCurrentDataTransfer(false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}
	
	public MFtpClient(FTPDataTransferListener transfer_listener) {
		transfer_listener_ = transfer_listener;
	}
	
	public String[] connect(String host, int port)
	{
		try {
			return ftp_client_.connect(host, port);
			
		} catch (IllegalStateException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			//e.printStackTrace();
		} catch (FTPException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		setErrorValue(ERR_CONNECT_FAIL);
		
		return null;
	}
	
	public String[] connect(String host, int port, String local_ip)
	{
		try {
			return ftp_client_.connect(host, port, local_ip);
			
		} catch (IllegalStateException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			//e.printStackTrace();
		} catch (FTPException e) {
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		setErrorValue(ERR_CONNECT_FAIL);
		
		return null;
	}
	
	public String[] connect(){
		return connect("127.0.0.1", 21);
	}
	
	public boolean login(String user, String password) {
		
		try {
			ftp_client_.login(user, password);
			return true;
		} catch (IllegalStateException e) {
			//e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		setErrorValue(ERR_LOGIN_FAIL);
		
		return false;
	}
	
	public boolean login()
	{
		return login("anonymous", "");
	}
	
	public boolean disconnect(boolean safe)
	{
		try {
			ftp_client_.disconnect(safe);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public long getFileSize(String path) {
		try {
			return ftp_client_.fileSize(path);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		setErrorValue(ERR_FILE_NOT_FOUND);
		return -1;
	}
	
	public boolean deleteFile(String remote_file) {
		try {
			ftp_client_.deleteFile(remote_file);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		setErrorValue(ERR_FILE_NOT_FOUND);
		return false;
	}
	
	public boolean rename(String oldPath, String newPath) {
		try {
			ftp_client_.rename(oldPath, newPath);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		setErrorValue(ERR_FILE_NOT_FOUND);
		return false;
	}
	
	public boolean deleteDir(String remote_dir) {
		try {
			ftp_client_.deleteDirectory(remote_dir);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public boolean cd(String dir) {
		try {
			ftp_client_.changeDirectory(dir);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public boolean createDir(String dir) {
		try {
			ftp_client_.createDirectory(dir);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public boolean upload(String file) {
		
		try {
			ftp_client_.upload(new File(file), transfer_listener_);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			setErrorValue(ERR_FILE_NOT_FOUND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public boolean download(String remote_file, String local_file){
		
		try {
			ftp_client_.download(remote_file, new File(local_file), transfer_listener_);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			setErrorValue(ERR_FILE_NOT_FOUND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return false;
	}

	public boolean downloadEx(String src_file, long start){
		
		try {	
			ftp_client_.download(src_file, output_stream_, start, transfer_listener_);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			setErrorValue(ERR_FILE_NOT_FOUND);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public boolean uploadEx(String src_file, String dest_file, long size) {
		
		InputStream input_stream = null;
		upload_size_ = 0;
		if (null == src_file || "" == src_file) {
			upload_size_ = size;
			input_stream = input_stream_;
		} else {
			upload_size_ = 0;
			try {
				input_stream = new FileInputStream(new File(src_file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				setErrorValue(ERR_FILE_NOT_FOUND);
				return false;
			}
		}
		
		try {
			ftp_client_.uploadEx(dest_file, input_stream, 0, 0, transfer_listener_);
			return true;
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPIllegalReplyException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPDataTransferException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (FTPAbortedException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return false;
	}

	public FTPFile[] list() {
		// TODO Auto-generated method stub
		try {
		return ftp_client_.list();

	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (FTPIllegalReplyException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (FTPException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (FTPDataTransferException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (FTPAbortedException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (FTPListParseException e) {
		// TODO Auto-generated catch block
		//e.printStackTrace();
	} catch (Exception e) {
		//e.printStackTrace();
	}
	
	return null;
	}
}
