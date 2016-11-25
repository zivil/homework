package Test;
import aop.ProxyFactoryBean;
import ioc.context.*;

public class AopTestMain {
	public static void main(String[] args) {
		try {
			ProxyFactoryBean applicationContext = new ProxyFactoryBean("aop.xml");
			FooInterface foo = (FooInterface) applicationContext.getProxy("foo");
		    foo.printFoo();
		    foo.dummyFoo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  }
}
