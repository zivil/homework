package com.foxer;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Service {
private int port = 8888;
private Map<String, String> route;
public Service(){
	File xmlFile = new File("WebRoot/web.xml");
	DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder;
	try {
		builder = builderFactory.newDocumentBuilder();
		Document doc = builder.parse(xmlFile);
		doc.getDocumentElement().normalize();
		loadroute(doc);
	} catch (ParserConfigurationException e) {
		e.printStackTrace();
	} catch (SAXException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} 
}

private void loadroute( Document doc )
{
	route = new HashMap<String, String>();
	NodeList nList = doc.getElementsByTagName("servlet");
	NodeList uList = doc.getElementsByTagName("servlet-mapping");
	for(int i = 0 ; i<nList.getLength();i++){
		NodeList classname = nList.item(i).getChildNodes();
		NodeList url = uList.item(i).getChildNodes();
		route.put(url.item(3).getTextContent(), classname.item(3).getTextContent());
	}
}

public void setPort(int port) {
	this.port = port;
}

public void serve(){
	ServerSocket ss;
	try {
		ss = new ServerSocket(port);
		while(true){
			Socket s = ss.accept();
			new TestServlet().init(s,route);
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}