package com.httpserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker implements Runnable{
	private Socket socket;
	private Request request;
	private Response response;
	private InputStream reader;
	private PrintWriter writer;
	private String reqpath = "";
	
	class line{public line(int n, byte[] b){num = n;buf=b;}public int num; public byte[] buf;};
	
	private byte[] buf;
	private int bufpos = -1;
	private long id;
	private int post_content_len = -1;
	public void init(){
		try {
			reader = socket.getInputStream();  
			writer = new PrintWriter(socket.getOutputStream(), true);
			buf = new byte[1024];
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		try {
			int len = 1;
			bufpos = 0;
			socket.setSoTimeout(ServerConfig.getTimeout());
			while((len = reader.read(buf, bufpos, 1)) != -1){
				parseRequest();
				bufpos++;
			}	
		} catch (IOException e) {
			pln("连接："+id+"超过timeout时间，已经断开。");
			ServerConfig.getThreadPoll().remove(id);
			System.gc();
		}
	}

	public void setSocket(Socket s){
		socket = s;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	private void parseRequest(){
		getLine();
		String[] tmp = new String(buf).split(" ");
		if (tmp[1].startsWith("http://")) {
			tmp[1] = tmp[1].substring(tmp[1].indexOf("/", 7), tmp[1].length());
		}
		//tmp[1].in
		request = null;
		request = new Request();
		response = null;
		response = new Response();
		response.setWriter(writer);
		Logger.write(socket.getInetAddress().getHostAddress(),tmp[0]+" "+tmp[1]);
		Servlet thisServlet = null;
		buf = new byte[1024];
		writer.write(HttpContent.getHeader());
		try {
			switch(tmp[0]){
			case "GET":
				parseGet(tmp[1]);
				if((thisServlet = route())!=null){
					thisServlet.doget(request, response);
					thisServlet.doserve(request, response);
					socket.shutdownOutput();
					return;
				}
				break;
			case "POST":
				reqpath = tmp[1];
				parsePost();
				if((thisServlet = route())!=null){
					thisServlet.dopost(request, response);
					thisServlet.doserve(request, response);
					socket.shutdownOutput();
					return;
				}
				break;
			case "HEAD":
				parseHead(tmp[1]);
				if((thisServlet = route())!=null){
					thisServlet.dohead(request, response);
					socket.shutdownOutput();
					return;
				}
				break;
			case "PUT":
				parsePut();
				if((thisServlet = route())!=null){
					thisServlet.doput(request, response);
					socket.shutdownOutput();
					return;
				}
				break;
			default:
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(reqpath.endsWith(".jsp")){
				
				socket.shutdownOutput();
				return;
			}
			sendStaticFile();
			socket.shutdownOutput();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void sendStaticFile() {
		try {
			char[] filebuf = new char[1024];
			FileReader fr = null;
			reqpath = "WebRoot"+reqpath;
			if(reqpath.endsWith("/"))
				fr= new FileReader(reqpath+"index.html");
			else
				fr = new FileReader(reqpath);
			while(fr.read(filebuf) != -1){
				writer.println("\r\n");
				writer.print(filebuf);
			}
			fr.close();
			writer.flush();
		} catch (IOException e) {
			writer.write(HttpContent.get404error());
			writer.flush();
			e.printStackTrace();
		}
	}
	private void parseGet(String link){
		if(link.contains("?")){
			String[] uri = link.split("[?]");
			this.reqpath = uri[0];
			String[] para = uri[1].split("&");
			for(int i = 0; i < para.length; i++){
				String[] con = para[i].split("=");
				request.setParameter(con[0], con[1]);
			}
		} else {
			this.reqpath = link;
		}
		int num = 2;
		while(num > 1 && !byteToString(buf).equals("\n")){
			buf = new byte[1024];
			bufpos = 0;
			num = getLine();
			String a = new String(buf);
			setCookie(a);
		}
	}
	
	private void parsePost(){
		post_content_len = -1;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String recv = "";
		try {
			while(true){
				recv = in.readLine();
				setCookie(recv);
				if(recv.contains("Content-Length")){
					post_content_len = Integer.valueOf(recv.substring(16));
					break;
				}
			}
			char[] buf = new char[1];
			in.read(buf);
			bufpos = 0;
			this.buf = new byte[1024];
			while(true){
				if(buf[0] == '\n'){
					setCookie(new String(this.buf));
					this.buf = new byte[1024];
					bufpos = 0;
					//setCookie();
					in.read(buf);
					if(buf[0] == '\r'){
						in.read(buf);
						break;
					} else {
						
					}
				}
				this.buf[bufpos] = (byte) buf[0];
				bufpos++;
				in.read(buf);
			}
			request.printCookieMap();
			char[] data = new char[post_content_len];
			in.read(data, 0, post_content_len);
			String postdata = new String(data, 0, post_content_len);
			String[] para = postdata.split("&");
			for(int i = 0; i < para.length; i++){
				String[] con = para[i].split("=");
				request.setParameter(con[0], con[1]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void parseHead(String link){
		if(link.contains("?")){
			String[] uri = link.split("[?]");
			this.reqpath = uri[0];
			String[] para = uri[1].split("&");
			for(int i = 0; i < para.length; i++){
				String[] con = para[i].split("=");
				request.setParameter(con[0], con[1]);
			}
		} else {
			this.reqpath = link;
		}
		int num = 2;
		while(num > 1 && !byteToString(buf).equals("\n")){
			buf = new byte[1024];
			bufpos = 0;
			num = getLine();
			String a = new String(buf);
			setCookie(a);
		}
	}
	
	private void parsePut(){
		post_content_len = -1;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String recv = "";
		try {
			while(true){
				recv = in.readLine();
				setCookie(recv);
				if(recv.contains("Content-Length")){
					post_content_len = Integer.valueOf(recv.substring(16));
					break;
				}
			}
			char[] buf = new char[1];
			in.read(buf);
			bufpos = 0;
			this.buf = new byte[1024];
			while(true){
				if(buf[0] == '\n'){
					setCookie(new String(this.buf));
					this.buf = new byte[1024];
					bufpos = 0;
					//setCookie();
					in.read(buf);
					if(buf[0] == '\r'){
						in.read(buf);
						break;
					} else{
						
					}
				}
				this.buf[bufpos] = (byte) buf[0];
				bufpos++;
				in.read(buf);
			}
			request.printCookieMap();
			char[] data = new char[post_content_len];
			in.read(data, 0, post_content_len);
			String postdata = new String(data, 0, post_content_len);
			String[] para = postdata.split("&");
			for(int i = 0; i < para.length; i++){
				String[] con = para[i].split("=");
				request.setParameter(con[0], con[1]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setCookie(String a){
		if(a.contains("Cookie")){
			a = a.split(": ")[1];
			String[] tmp = a.split("; ");
			for(int i =0; i < tmp.length; i++){
				String[] con = tmp[i].split("=");
				request.setCookie(con[0], con[1]);
			}
		}
	}
	
	private Servlet route() {
		Servlet instance = null;
		try {
			if(ServerConfig.getRouteMap().containsKey(reqpath)){
				Class<?> obj = Class.forName(ServerConfig.getRouteMap().get(reqpath));
				instance = (Servlet)obj.newInstance();
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return instance;
	}
	
	private int getLine(){
		int pos = 0;
		try {
			while(buf[bufpos] != (byte)'\n'){
				bufpos++;
				reader.read(buf, bufpos, 1);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pos = bufpos -1;
		bufpos = 0;
		return pos;
	}
	
	private String byteToString(byte[] b){
		return new String(b);
	}
	
	
	private void pln(String str){
		System.out.println(str);
	}
	
}
