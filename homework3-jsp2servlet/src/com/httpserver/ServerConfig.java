package com.httpserver;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ServerConfig {
	private static Map<String, String> route;
	private static Map<Long,Thread> connect;
	private static int timeout = 10000;
	private static int workerlimit = 10;
	public static void init(){
		route = new HashMap<String, String>();
		connect = new HashMap<Long, Thread>();
		parseWebRoute();
	}
	
	public static void printRouteMap(){
		for(Map.Entry<String, String> entry:route.entrySet()){    
		     System.out.println(entry.getKey()+"--->"+entry.getValue());    
		}
	}
	
	public static void printConnectMap(){
		for(Map.Entry<Long, Thread> entry:connect.entrySet()){    
		     System.out.println(entry.getKey()+"--->"+entry.getValue());    
		}
	}
	
	public static Map<Long,Thread> getThreadPoll(){
		return connect;
	}
	
	private static void parseWebRoute(){
		try {
			File xmlFile = new File("WebRoot/web.xml");
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;
			builder = builderFactory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("servlet");
			NodeList uList = doc.getElementsByTagName("servlet-mapping");
			for(int i = 0 ; i<nList.getLength();i++){
				NodeList classname = nList.item(i).getChildNodes();
				NodeList url = uList.item(i).getChildNodes();
				route.put(url.item(3).getTextContent(), classname.item(3).getTextContent());
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		ServerConfig.timeout = timeout;
	}

	public static int getWorkerlimit() {
		return workerlimit;
	}

	public static void setWorkerlimit(int workerlimit) {
		ServerConfig.workerlimit = workerlimit;
	}
	
	public static Map<String, String> getRouteMap(){
		return route;
	}
}
