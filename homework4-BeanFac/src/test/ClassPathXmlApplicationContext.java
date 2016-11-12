package test;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ClassPathXmlApplicationContext implements ApplicationContext {
	private String filename;
	private Map<String, Bean> beans;
	public ClassPathXmlApplicationContext(String[] locations) {
		// TODO Auto-generated constructor stub
		filename = locations[0];
		init();
	}
	
	private void init(){
		try {
			beans = new HashMap<String, Bean>();
			doLoadBeanDefinitions();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Object getBean(String string) {
		Object instance = null;
		try {
			Class<?> obj = Class.forName(beans.get(string).getBeanClassName());
			List<PropertyValue> values = beans.get(string).getPropertyValues().getPropertyValues();  
			//参数类型数组  
			Class<?>[] parameterTypes={car.class, office.class};   
			//根据参数类型获取相应的构造函数  
			Constructor<?> constructor=obj.getConstructor(parameterTypes);  
			//参数数组  
			Object[] parameters= new Object[values.size()];  
			//根据获取的构造函数和参数，创建实例  
			for(int i = 0; i < values.size(); i++){
		    	 PropertyValue v = values.get(i);
		    	 if(v.getType() == "value"){
		    		 parameters[i] = v.getValue().toString();
		    	 } else {
		    		 parameters[i] = 
		    				 Class.forName(((BeanReference)(v.getValue())).getName()).newInstance();
		    	 }
		     }
			instance=constructor.newInstance(parameters);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//p(obj.getName());
//		for(Map.Entry<String, Bean> entry:beans.entrySet()){
//		     System.out.println(entry.getKey());
//		     List<PropertyValue> values = entry.getValue().getPropertyValues().getPropertyValues();
//		     for(int i = 0; i < values.size(); i++){
//		    	 PropertyValue v = values.get(i);
//		    	 if(v.getType() == "value"){
//		    		 p(v.getValue().toString());
//		    	 } else {
//		    		 p("class:"+((BeanReference)v.getValue()).getName());
//		    	 }
//		     }
//		}   
		return instance;
	}
	
	private void p(String a){
		System.out.println(a);
	}
	
    public Object getInstance(Object bean, PropertyValues values){
    	Class<?> newoneClass = (Class<?>) bean;
    	int len = values.getPropertyValues().size();
	    Class[] argsClass = new Class[len];                                   
	    for (int i = 0, j = len; i < j; i++) {                                
	        argsClass[i] = values.getPropertyValues().get(i).getValue().getClass();                                        
	    }	  
	    Constructor<?> cons = null;
		try {
			cons = newoneClass.getConstructor(argsClass);
			return cons.newInstance(values.toArray());
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	             
    }
	
	protected void doLoadBeanDefinitions() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		Document doc = docBuilder.parse(new FileInputStream(new File(filename)));
		// 解析bean
		registerBeanDefinitions(doc);
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
		Bean bean = new Bean();
		processProperty(ele, bean);
		bean.setBeanClassName(className);
		beans.put(name, bean);
	}

	private void processProperty(Element ele, Bean beanDefinition) {
		NodeList propertyNode = ele.getElementsByTagName("property");
		for (int i = 0; i < propertyNode.getLength(); i++) {
			Node node = propertyNode.item(i);
			if (node instanceof Element) {
				Element propertyEle = (Element) node;
				String name = propertyEle.getAttribute("name");
				String value = propertyEle.getAttribute("value");
				if (value != null && value.length() > 0) {
					PropertyValue pv = new PropertyValue(name, value);
					pv.setType("value");
					beanDefinition.getPropertyValues().addPropertyValue(pv);
				} else {
					String ref = propertyEle.getAttribute("ref");
					if (ref == null || ref.length() == 0) {
						throw new IllegalArgumentException("Configuration problem: <property> element for property '"
								+ name + "' must specify a ref or value");
					}
					BeanReference beanReference = new BeanReference(ref);
					PropertyValue pv = new PropertyValue(name, beanReference);
					pv.setType("class");
					beanDefinition.getPropertyValues().addPropertyValue(pv);
				}
			}
		}
	}
}


