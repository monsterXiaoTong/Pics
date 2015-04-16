package com.jogger.pic.utils;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import android.text.TextUtils;

/**
 * @create date 2015-1-15
 * @author xiaotong zhao
 * @class description Utility Tools Class
 */

public class UtilityTools {
	
	/**
	 * convert UNIX timestamp to Data String
	 * 
	 * @param timestamp
	 * */
	public static String convertTimeStamp(long unixTime) {
		String date = null;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss E");
		date = formatter.format(new Date(unixTime * 1000));
		return date;
	}

	/**
	 * convert byte to KB/MB or do nothing
	 * 
	 * @param sizeStr
	 * */
	public static String convertByte(String sizeStr) {
		if (TextUtils.isEmpty(sizeStr)) {
			return "未知";
		}
		int size = Integer.parseInt(sizeStr);
		int sizeKB = size / 1024;
		int sizeB = size % 1024;
		float sizeMB = 0;
		String result = null;
		if (0 == sizeKB) {
			result = String.valueOf(size);
		} else if (sizeB > 512 && sizeKB < 1024) {
			sizeKB++;
			result = String.valueOf(sizeKB) + "KB";
		} else if (sizeKB > 1024) {
			if (sizeB > 512) {
				sizeKB++;
			}
			sizeMB = (float) sizeKB / 1024;
			DecimalFormat df = new DecimalFormat(".00");
			result = df.format(sizeMB) + "MB";
		} else {
			result = String.valueOf(sizeKB) + "KB";
		}
		return result;
	}

}