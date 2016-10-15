package com.foxer;

import java.io.PrintWriter;

public class Response {
	private PrintWriter out;
	public PrintWriter getWriter(){
		return out;
	}
	
	public void setWriter(PrintWriter printWriter) {
		out = printWriter;	
	}
}
