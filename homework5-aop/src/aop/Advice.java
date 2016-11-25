package aop;

import java.lang.reflect.InvocationHandler;
import java.util.List;

public interface Advice extends InvocationHandler{
	public void setInstance(Object obj);
}
