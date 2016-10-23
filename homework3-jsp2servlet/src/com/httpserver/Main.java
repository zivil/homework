package com.httpserver;

public class Main {
	
	public static void main(String[] args) {
		ParseJsp a = new ParseJsp("WebRoot/test.jsp");
		a.Parse(1);
		
		Server s = new Server();
		s.setPort(8089);
		s.init();
		s.serve();
	}

}
