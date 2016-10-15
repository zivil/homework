package com.foxer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Request {
	BufferedReader in;
	
	String method;
	
	String url;
	
	Map<String, String> param;
	
	int post_content_len = 0;
	
	public Request(){
		param = new HashMap<String, String>();
	}
	
	public String getParameter(String key){
		return param.get(key);
	}
	
	public void setReader(BufferedReader bufferedReader) {
		in = bufferedReader;
		String recv;
		try {
			recv = in.readLine();
			String[] method = recv.split(" ");
			if(method[0].equals("GET")){
				this.method = "GET";
			}
			if(method[0].equals("POST")){
				this.method = "POST";
				while(recv != null){
					if(recv.length() > 14 && recv.substring(0, 14).equals("Content-Length")){
						post_content_len = Integer.valueOf(recv.substring(16));
						break;
					}
					recv = in.readLine();
				}
			}
			if(method[1].contains("?")){
				String[] url = method[1].split("[?]");
				this.url = url[0];
				String[] para = url[1].split("&");
				for(int i = 0; i < para.length; i++){
					String[] con = para[i].split("=");
					param.put(con[0], con[1]);
				}
			} else {
				this.url = method[1];
			}
			in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getMethod(){
		if(method.equals("POST")){
			try {
				
				char[] buf = new char[1];
				in.read(buf);
				while(buf != "".toCharArray()){
					if(buf[0] == '\n'){
						in.read(buf);
						if(buf[0] == '\r'){
							in.read(buf);
							break;
						}
					}
					in.read(buf);
					
				}
				
				char[] data = new char[post_content_len];
				in.read(data, 0, post_content_len);
				String postdata = new String(data, 0, post_content_len);
				String[] para = postdata.split("&");
				for(int i = 0; i < para.length; i++){
					String[] con = para[i].split("=");
					param.put(con[0], con[1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return method;
	}

	public Map<String, String> getParameterMap() {
		return param;
	}
}
