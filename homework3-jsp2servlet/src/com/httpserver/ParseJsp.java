package com.httpserver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseJsp {
	private FileReader fr = null;
	private FileWriter fw = null;
	private String name = null;
	public ParseJsp(String filename){
		try {
			fr = new FileReader(filename);
			String[] tmp = filename.split("/");
			String outputname = tmp[tmp.length - 1];
			outputname = outputname.substring(0, outputname.length() - 4);
			name = outputname+".java";
			fw = new FileWriter(filename.substring(0, filename.length() - 4)+".java");
			fw.write("");
			fw.close();
			fw = new FileWriter(filename.substring(0, filename.length() - 4)+".java",true);
			fw.write("package com.httpclient;\n");
			fw.write("import java.io.PrintWriter;\n");
			fw.write("import com.httpserver.Request;\n");
			fw.write("import com.httpserver.Response;\n");
			fw.write("import com.httpserver.Servlet;\n");
			fw.write("import java.util.Map;\n");
			fw.write("public class "+outputname+" extends Servlet{\n");
			fw.write("\tpublic void doserve(Request request, Response response){\n");
			fw.write("\t\tPrintWriter out = response.getWriter();\n");
			fw.write("\t\tout.println(\"HTTP/1.0 200 OK\\r\\nContent-Type: text/html\\r\\n\\r\\n\");\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Parse(){
		try {
			BufferedReader reader = new BufferedReader(fr);
			String nowline = null;
			while((nowline = reader.readLine())!= null){
				fw.write("\t\tout.write(\"");
				nowline = nowline.replaceAll(" +", " ");
				nowline = nowline.replaceAll("\"", "\\\\\"");
				nowline = nowline.replaceAll("[<% %>]", "[\n\t\tout.write(\" \");\n]");
				//nowline = nowline.replaceAll("<% =","\");\n\t\tout.write(\"");
				nowline = nowline.replaceAll("<%", "\");\n\t\t");
				
				nowline = nowline.replaceAll("%>", "\n\t\tout.write(\"");
				fw.write(nowline);
				fw.write("\");\n");
			}
			fw.write("\t}\n");
			fw.write("}\n");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void Parse(int i){
		try {
			BufferedReader reader = new BufferedReader(fr);
			String nowline = null;
			int line = 0;
			while((nowline = reader.readLine())!= null){
				nowline = nowline.replaceAll(" +", " ");
				nowline = nowline.replaceAll("\"", "\\\\\"");
				nowline = "\t\tout.write(\""+nowline;
				String regex = "[\\s\\S]*(?<=<%=)([\\s\\S]+)(?= +%>)[\\s\\S]*";
				nowline = nowline.replaceAll(regex, "\n\t\tout.write($1);\n");
				nowline = nowline.replaceAll("<%", "\");\n\t\t");
				nowline = nowline.replaceAll("%>", "\n\t\tout.write(\"");
				nowline = nowline + "\");\n";
				if(nowline.endsWith("\n\");\n")){
					nowline = nowline.substring(0, nowline.length()-4);
				}
				if(nowline.equals("\r\rout.write(\"\"")){
					nowline = "";
				}
				if(nowline.endsWith("\t\tout.write(\"\");\n")){
					nowline = nowline.substring(0, nowline.length() - 17);
				}
				if(nowline.startsWith("\t\tout.write(\"\");\n")){
					nowline = nowline.substring(17, nowline.length());
				}
				if(nowline.contains("getParameter")){
					nowline.replaceAll("\\\\\"", "\"");
					if(nowline.contains("\\\\\""))
					p("CAT1");
				}
				fw.write(nowline);
				
			}
			fw.write("\t}\n");
			fw.write("}\n");
			fw.close();
			DyComplier d = new DyComplier();
			d.textJavaCompiler("WebRoot/"+name, "");
			//System.out.println (input.replaceAll (regex, "$1"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void p(String nowline){
		System.out.println(nowline);
	}
}
