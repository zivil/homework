package com.httpserver;

public abstract class Servlet {
	public void dopost(Request request, Response response){}
	
	public void doget(Request request, Response response){}
	
	public void dohead(Request request, Response response){}
	
	public void doput(Request request, Response response){}
	
	public void doserve(Request request, Response response){}
}
