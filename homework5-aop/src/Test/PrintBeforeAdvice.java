package Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import aop.Advice;

public class PrintBeforeAdvice implements Advice{
	Object obj;

	public PrintBeforeAdvice() {
		// TODO Auto-generated constructor stub
	}
	
	public PrintBeforeAdvice(Object obj) {
		// TODO Auto-generated constructor stub
		this.obj = obj;
	}
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println("Before");
		method.invoke(obj, args);
		System.out.println("After");
		return null;
	}

	@Override
	public void setInstance(Object obj) {
		// TODO Auto-generated method stub
		this.obj = obj;
	}

}
