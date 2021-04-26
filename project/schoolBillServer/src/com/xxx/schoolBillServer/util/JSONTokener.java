package com.xxx.schoolBillServer.util;

public class JSONTokener {

	public static String JSONTokenerAll(String in) {
		 // consume an optional byte order mark (BOM) if it exists
		 if (in != null && in.startsWith("\ufeff")) {
		 in = in.substring(1);
		 }
		 return in;
	}
}
