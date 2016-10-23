package com.httpserver;

import java.util.HashMap;
import java.util.Map;

public class Request {
	private Map<String, String> param;
	private Map<String, String> cookie;
	
	public Request(){
		param = new HashMap<String, String>();
		cookie = new HashMap<String, String>();
	}
	
	public String getParameter(String key){
		return param.get(key);
	}
	
	public Map<String, String> getParameterMap(){
		return param;
	}
	
	public void setParameter(String key, String value){
		param.put(key, value);
	}
	
	public void printParameterMap(){
		for(Map.Entry<String, String> entry:param.entrySet()){    
		     System.out.println(entry.getKey()+"--->"+entry.getValue());    
		}
	}

	public void printCookieMap(){
		for(Map.Entry<String, String> entry:cookie.entrySet()){    
		     System.out.println(entry.getKey()+"--->"+entry.getValue());    
		}
	}
	
	public void setCookie(String key, String value) {
		cookie.put(key, value);
	}
	
	public String getCookie(String key){
		return cookie.get(key);
	}
	
	public Map<String, String> getCookieMap(){
		return cookie;
	}
}
