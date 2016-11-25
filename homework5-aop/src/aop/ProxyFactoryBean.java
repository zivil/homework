package aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import ioc.context.ApplicationContext;
import ioc.context.ClassPathXmlApplicationContext;

public class ProxyFactoryBean {
	private ClassPathXmlApplicationContext xmlbeanfactory;
	private ProxyFactoryBean instance;
	private Object proxyInterfaces;
	private Object target;
	private List<String> interceptorNames;
	public ProxyFactoryBean(){
		
	}
	public ProxyFactoryBean(String string) throws Exception {
		// TODO Auto-generated constructor stub
		xmlbeanfactory = new ClassPathXmlApplicationContext(string);
		instance = (ProxyFactoryBean) xmlbeanfactory.getBean(xmlbeanfactory.getProxyName());
	}
	
	public void setXmlbeanfactory(ClassPathXmlApplicationContext xmlbeanfactory) {
		this.xmlbeanfactory = xmlbeanfactory;
	}

	public void setProxyInterfaces(Object proxyInterfaces) {
		this.proxyInterfaces = proxyInterfaces;
	}

	public void setTarget(Object target) {
		this.target = target;
	}

	public void setInterceptorNames(List<String> interceptorNames) {
		this.interceptorNames = interceptorNames;
	}

	public Object getProxy(String name) throws Exception {
		// TODO Auto-generated method stub
		Object inc = instance.target;
		Advice interceptorname = (Advice) xmlbeanfactory.getBean(instance.interceptorNames.get(0));
		interceptorname.setInstance(inc);
		Object bean = Proxy.newProxyInstance(interceptorname.getClass().getClassLoader(), inc.getClass().getInterfaces(), interceptorname);
		return bean;
	}

	private void P(Object s){
		System.out.println(s);
	}
}
