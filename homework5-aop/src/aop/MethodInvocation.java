package aop;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

public interface MethodInvocation {

	Method getMethod();

	Object[] getArguments();

	Object proceed() throws Throwable;

	Object getThis();

	AccessibleObject getStaticPart();

}
