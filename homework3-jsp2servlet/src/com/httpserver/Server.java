package com.httpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket ss;
	
	private int port = 8089;
	
	public void setPort(int port){
		this.port = port;
	}
	
	public void init(){
		try {
			ServerConfig.init();
			//set the pconnect timeout
			ServerConfig.setTimeout(9000);
			ServerConfig.setWorkerlimit(10);
			ss = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void serve() {
		while(true){
			try {
				Socket s = ss.accept();
				Worker w = new Worker();
				w.setSocket(s);
				w.init();
				Thread t = new Thread(w);
				w.setId(t.getId());
				if(ServerConfig.getThreadPoll().size() < ServerConfig.getWorkerlimit()){
					ServerConfig.getThreadPoll().put(t.getId(), t);
					t.start();
				} else {
					System.out.println("连接数已经达到上限。");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	

}
