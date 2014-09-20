package com.zte.util;

public class UtilLexicalCast {

	public static int ParseInt(String str, int errorValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			// TODO: handle exception
			return errorValue;
		}
	}
	
	public static long ParseLong(String str, long errorValue) {
		try {
			return Long.parseLong(str);
		} catch (Exception e) {
			// TODO: handle exception
			return errorValue;
		}
	}
	
	public static float ParseFloat(String str, float errorValue) {
		try {
			return Float.parseFloat(str);
		} catch (Exception e) {
			// TODO: handle exception
			return errorValue;
		}
	}
	
	public static double ParseDouble(String str, double errorValue) {
		try {
			return Double.parseDouble(str);
		} catch (Exception e) {
			// TODO: handle exception
			return errorValue;
		}
	}
}
