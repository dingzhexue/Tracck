package com.gpit.android.logger;

import android.content.Context;
import android.os.Environment;

import com.gpit.android.util.FileUtils;
import com.gpit.android.util.TimeUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class FileLog {
	
	private static final String FILE_NAME = "app.log";
	private static final String FORMAT_TIMESTAMP = "MM/dd/yyy hh:mm:ss";

	public static void logMessage(Context context, String msg) {
		
		Date today = new Date();
		String timestamp = TimeUtils.getDateString(today.getTime(), FORMAT_TIMESTAMP);
		msg = timestamp + ":" + msg;

		File logFile = FileUtils.createSharedFile(context, Environment.DIRECTORY_DOCUMENTS, FILE_NAME);
		if ( !logFile.exists() ) {
			try {
				logFile.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
			buf.append(msg);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
