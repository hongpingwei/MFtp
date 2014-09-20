package com.zte.mftp;

public class Common {

	private static boolean enable_stdout = false;
	
	public static void println(String str) {
		if(enable_stdout)
			System.out.println(str);
	}
	
	public static void enableStdOut(boolean state) {
		enable_stdout = state;
	}
}
