package ioc.beans.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ioc.BeanReference;
import ioc.beans.AbstractBeanDefinitionReader;
import ioc.beans.BeanDefinition;
import ioc.beans.PropertyValue;
import ioc.beans.io.ResourceLoader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {
	private String ProxyName;
	public XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}
	
	@Override
	public void loadBeanDefinitions(String location) throws Exception {
		InputStream inputStream = getResourceLoader().getResource(location).getInputStream();
		doLoadBeanDefinitions(inputStream);
	}
	
	protected void doLoadBeanDefinitions(InputStream inputStream) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		Document doc = docBuilder.parse(inputStream);
		registerBeanDefinitions(doc);
		inputStream.close();
	}

	public void registerBeanDefinitions(Document doc) {
		Element root = doc.getDocumentElement();

		parseBeanDefinitions(root);
	}

	protected void parseBeanDefinitions(Element root) {
		NodeList nl = root.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element) {
				Element ele = (Element) node;
				processBeanDefinition(ele);
			}
		}
	}

	protected void processBeanDefinition(Element ele) {
		String name = ele.getAttribute("id");
		String className = ele.getAttribute("class");
		if(className.equals("aop.ProxyFactoryBean")){
			this.setProxyName(name);
		}
		BeanDefinition beanDefinition = new BeanDefinition();
		processProperty(ele, beanDefinition);
		beanDefinition.setBeanClassName(className);
		getRegistry().put(name, beanDefinition);
	}

	private void processProperty(Element ele, BeanDefinition beanDefinition) {
		NodeList propertyNode = ele.getElementsByTagName("property");
		for (int i = 0; i < propertyNode.getLength(); i++) {
			Node node = propertyNode.item(i);
			if (node instanceof Element) {
				Element propertyEle = (Element) node;
				String name = propertyEle.getAttribute("name");
				String value = propertyEle.getAttribute("value");
				String ref = propertyEle.getAttribute("ref");
				if (value != null && value.length() > 0) {
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
				} else if(ref != null && ref.length() > 0){
					BeanReference beanReference = new BeanReference(ref);
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
				} else if(propertyEle.hasChildNodes()){
					NodeList valueNodes = propertyEle.getElementsByTagName("value");
					List<String> list = new ArrayList<String>();
					for (int j = 0; j < valueNodes.getLength(); j++) {
						Node valueNode = valueNodes.item(j);
						list.add(valueNode.getTextContent());
					}
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, list));
				} else {
					System.out.println(propertyEle.getFirstChild().getNodeName());
					throw new IllegalArgumentException("Configuration problem: <property> element for property '"
							+ name + "' must specify a ref or value or a list");
				}
			}
		}
	}
	
	private void P(Object string){
		System.out.println(string);
	}

	public String getProxyName() {
		return ProxyName;
	}

	public void setProxyName(String proxyName) {
		ProxyName = proxyName;
	}
}
