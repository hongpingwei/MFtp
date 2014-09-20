package com.zte.util;

import java.util.HashMap;

public class UtilCmdlineParser {

	private String cmdline = "";
	private String cmd = "";
	
	private boolean caseSensitive = false;
	private String params_del = " -";
	private String key_value_del = ":";
	
	private HashMap<String, String> argsMap = new HashMap<String, String>();
	
	public UtilCmdlineParser(String cmdline) {
		parseCmdline(cmdline);
	}
	
	public UtilCmdlineParser(String cmdline, boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
		parseCmdline(cmdline);
	}
	
	public UtilCmdlineParser(String cmdline, boolean cap_sensitive, String params_del, String key_value_del) {
		this.caseSensitive = cap_sensitive;
		this.params_del = params_del;
		this.key_value_del = key_value_del;
		
		parseCmdline(cmdline);
	}

	boolean isCaseSensitive() {
		return caseSensitive;
	}

	private boolean parseCmdline(String cmdline) {
		
		this.cmdline = cmdline.trim();
		String[] args = this.cmdline.split(params_del);
		if (args.length == 0)
			return false;

		//trim spaces
		for (int i = 0; i < args.length; i++) {
			args[i] = args[i].trim();
		}
		//save cmd
		cmd = args[0];

		//save args
		if (args.length > 1) {
			for (int i = 1; i < args.length; i++) {
				String[] pair = args[i].split(key_value_del, 2);
				if (2 == pair.length) {
					if (!isCaseSensitive()) {
						argsMap.put(pair[0].toLowerCase(), pair[1]);
					} else {
						argsMap.put(pair[0], pair[1]);
					}
				}
				else if(1 == pair.length)
				{
					if (!isCaseSensitive()) {
						argsMap.put(pair[0].toLowerCase(), "");
					} else {
						argsMap.put(pair[0].toLowerCase(), "");
					}
				}
			}
		}
		return true;
	}

	public boolean hasKey(String key) {
		String formatKey = key;
		if (!isCaseSensitive())
			formatKey = key.toLowerCase();

		return argsMap.containsKey(formatKey);
	}
	
	public String getCmd() {
		return cmd;
	}

	public String getValue(String key) {
		String formatKey = key;
		if (!isCaseSensitive())
			formatKey = key.toLowerCase();
		if (hasKey(key))
			return argsMap.get(formatKey);
		else
			return "";
	}

	public static void main(String[] args) {
		
		UtilCmdlineParser cp = new UtilCmdlineParser("	 do -domo:33 -fuck:sb -shit:pp0  ");
		System.out.println(cp.getValue("domo"));
		System.out.println(cp.getValue("shit"));
		System.out.println(cp.getCmd());
	}
}
