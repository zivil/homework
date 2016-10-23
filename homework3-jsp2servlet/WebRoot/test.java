package com.httpclient;
import java.io.PrintWriter;
import com.httpserver.Request;
import com.httpserver.Response;
import com.httpserver.Servlet;
import java.util.Map;
public class test extends Servlet{
	public void doserve(Request request, Response response){
		PrintWriter out = response.getWriter();
		out.println("HTTP/1.0 200 OK\r\nContent-Type: text/html\r\n\r\n");
		 int a = 0; 
		out.write("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
		out.write("<html>");
		out.write("<head>");
		out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">");
		out.write("<link type=\"text/css\" href=\"css/success.css\" rel = \"stylesheet\">");
		out.write("<title>³É¹¦</title>");
		out.write("</head>");
		out.write("<body>");

		out.write(a);
		out.write("<br />");
		out.write("<center><font color=red>");
		 Map<String, String> param = request.getParameterMap();for (Map.Entry<String, String> entry : param.entrySet()) { out.println(entry.getKey() + entry.getValue()); } 
		out.write("</font></center>");
		out.write("</body>");
		out.write("</html>");
	}
}
