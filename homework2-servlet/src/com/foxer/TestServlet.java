package com.foxer;

import java.io.PrintWriter;
import java.util.Map;

public class TestServlet extends ServletInstance{
	public void dopost(Request request, Response response){
		PrintWriter out = response.getWriter();
		out.println("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\r\n");
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("POST METHOD.");
		//此处测试password
		//out.println(request.getParameter("password"));
		Map<String, String> param = request.getParameterMap();
		for (Map.Entry<String, String> entry : param.entrySet()) { 
			out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
		} 
		out.println("  </BODY>");
		out.println("</HTML>");
		
		out.flush();
		out.close();
	}
	
	public void doget(Request request, Response response){
		PrintWriter out = response.getWriter();
		out.println("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\r\n");
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		//此处测试a
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
		out.close();
	}
}
