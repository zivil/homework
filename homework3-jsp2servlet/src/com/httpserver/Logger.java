package com.httpserver;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	public enum logrank{
		error,
		log
	}
	public static void write(String ip,String content){
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat logtime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		FileWriter log;
		try {
			log = new FileWriter(dateFormat.format(now)+".log", true);
			log.write("From "+ip+" at "+logtime.format(now)+" "+content+"\r\n");
			log.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
