package test;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // 表示注解在运行时依然存在
@Target(ElementType.TYPE_USE) // 表示注解可以被使用于方法上
public @interface Autowired {
	
}
