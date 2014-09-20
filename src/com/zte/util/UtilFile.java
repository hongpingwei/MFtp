package com.zte.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.io.FileWriter;

public class UtilFile {

	public static byte[] fileReadBytes(String filePath) {

		File f = new File(filePath);
		InputStream input = null;
		byte[] b = null;
		try {
			input = new FileInputStream(f);
			b = new byte[(int) f.length()];
			input.read(b);
			input.close(); // 关闭输出流
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return b;
	}

	public static String fileReadString(String filePath) {

		File f = new File(filePath);
		Reader input = null;
		char c[] = null;
		int len = 0;

		try {

			input = new FileReader(f);

			c = new char[(int) f.length()];
			len = input.read(c);
			input.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(c, 0, len);
	}

	public static void fileWriteBytes(String filePath, byte[] abyData,
			boolean append) {

		File f = new File(filePath); // 声明File对象
		OutputStream out = null; // 准备好一个输出的对象

		try {

			out = new FileOutputStream(f, append);
			out.write(abyData);
			out.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void fileWriteString(String filePath, String strWrite,
			boolean append) {

		File f = new File(filePath);
		Writer out = null;

		try {
			out = new FileWriter(f, append);
			out.write(strWrite);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean createBinaryFile(String file_name, long file_size,
			byte default_value) {

		File f = new File(file_name);
		OutputStream out = null; // 准备好一个输出的对象

		try {

			if (file_size > 0) {
				byte[] buf = new byte[10 * 1024 * 1024];
				if (0 != default_value) {
					for (int i = 0; i < buf.length; i++) {
						buf[i] = default_value;
					}
				}
				long i = 0;
				out = new FileOutputStream(f);
				while (i + buf.length < file_size) {
					out.write(buf);
					i += buf.length;
				}
				out.write(buf, 0, (int) (file_size - i));
				out.close();
				return true;
				
			} else if (0 == file_size) {

				out = new FileOutputStream(f);
				out.close();
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static boolean overwirteBinaryFile(String file_pathname,
			byte pbuf[], int offset, int write_len, long pos) {
		try {
			RandomAccessFile file = new RandomAccessFile(file_pathname, "rwd");
			if (null == file) {
				return false;
			}
			overwirteBinaryFile(file, pbuf, offset, write_len, pos);
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static boolean overwirteBinaryFile(RandomAccessFile file,
			byte pbuf[], int offset, int write_len, long pos) {
		if (null == file || null == pbuf) {
			return false;
		}
		try {
			file.seek(pos);
			file.write(pbuf, offset, write_len);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean deleteFile(String pathname) {
		return new File(pathname).delete();
	}
	
	public static boolean renameFile(String src_pathname, String dest_pathname) {
		File src_file = new File(src_pathname);
		return src_file.renameTo(new File(dest_pathname));
	}

	public static String getFileName(String pathname) {
		int index = pathname.lastIndexOf("/") > pathname.lastIndexOf("\\") ? pathname.lastIndexOf("/") : pathname.lastIndexOf("\\");
		if(index > 0)
			return pathname.substring(index+1);
		return pathname;
	}
	
	public static void main(String[] args) {
		
		System.out.println(getFileName("d:/g\\g\\dsaf.cpp"));
	}

}
