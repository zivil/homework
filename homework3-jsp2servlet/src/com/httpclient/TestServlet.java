package com.httpclient;

import java.io.PrintWriter;
import java.util.Map;

import com.httpserver.Request;
import com.httpserver.Response;
import com.httpserver.Servlet;

public class TestServlet extends Servlet{
	public void dopost(Request request, Response response){
		PrintWriter out = response.getWriter();
		
		//out.println();
		out.print("HTTP/1.1 200 OK\r\n");
		response.setCookie("ff", "value");
		out.print("Content-Type: text/html\r\n\r\n");
		//out.println("<!DOCTYPE html><html>	<head>		<meta charset=\"UTF-8\"/>	</head>	<body>		<h1>			銈屻亜銇涖倱路銇嗐仼銈撱亽銇勩倱路銈ゃ儕銉�		</h1>	</body></html>");

		out.println("<!DOCTYPE html>");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("POST METHOD.");
		//锟剿达拷锟斤拷锟斤拷password
		//out.println(request.getParameter("password"));
		out.println("<br /><h4>Request.Parameter</h4>");
		Map<String, String> param = request.getParameterMap();
		for (Map.Entry<String, String> entry : param.entrySet()) { 
			out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
			out.println("<br />");
		} 
		out.println("<br /><h4>Cookie</h4>");
		Map<String, String> cookie = request.getCookieMap();
		for (Map.Entry<String, String> entry : cookie.entrySet()) { 
			out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			out.println("<br />");
		} 
		out.println("  </BODY>");
		out.println("</HTML>");
		
		out.flush();
	}
	
	public void doget(Request request, Response response){
		PrintWriter out = response.getWriter();
		out.println("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\r\n");
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		//锟剿达拷锟斤拷锟斤拷a
		//out.println(request.getParameter("a"));
		Map<String, String> param = request.getParameterMap();
		for (Map.Entry<String, String> entry : param.entrySet()) { 
			out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
		} 
		out.print("<form method='post' action='/TestServlet'>");
		out.print("login:<input name = 'login' />");
		out.println("password:<input name = 'password'/>");
		out.println("<input type = 'submit' value = 'submit' />");
		out.println("  </form>");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
	}
}
