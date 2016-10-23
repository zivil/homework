package com.httpserver;

import java.io.PrintWriter;

public class Response {
	private PrintWriter out;
	public Response(){
	}
	
	public PrintWriter getWriter(){
		return out;
	}
	
	public void setWriter(PrintWriter printWriter) {
		out = printWriter;	
	}
	
	public void setCookie(String key, String value){
		out.print("Set-Cookie: "+key+"="+value+"\r\n");
	}
	
	
}
