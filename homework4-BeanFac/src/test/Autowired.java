package test;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME) // ��ʾע��������ʱ��Ȼ����
@Target(ElementType.TYPE_USE) // ��ʾע����Ա�ʹ���ڷ�����
public @interface Autowired {
	
}
