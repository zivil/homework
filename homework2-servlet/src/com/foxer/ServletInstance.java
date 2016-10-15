package com.foxer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

class ServletInstance{
    private Socket s;
    private Request request;
    private Response response;
    
    public ServletInstance(){
        request = new Request();
        response = new Response();
    }
    
    public void init(Socket socket, Map<String, String> route){
    	try {
    		s = socket;
        	request.setReader(new BufferedReader(new InputStreamReader(s.getInputStream(),"utf-8")));  
			response.setWriter(new PrintWriter(s.getOutputStream(), true));
			if(request.getMethod().equals("GET")){
				doget(request, response);
			}else {
				if(route.containsKey(request.url)){
					Class obj = Class.forName(route.get(request.url));
					ServletInstance a = (ServletInstance)obj.newInstance();
					a.dopost(request, response);
				}else{
					dopost(request, response);
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void setRequest(Request req){
    	this.request = req;
    }
    
    public void setResponse(Response res){
    	this.response = res;
    }
    
    public void doget(Request request, Response response){
    	
    }
    
    public void dopost(Request request, Response response){
    	
    }
    
}
