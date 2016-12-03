package MVC;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspContext;

import org.apache.jasper.JspC;
import org.apache.jasper.servlet.JasperLoader;
import org.apache.jasper.servlet.JspServlet;


/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ServletContext context;
    public DispatcherServlet() {
        super();
    }

    @Override
    public void init() throws ServletException {           
        super.init();
        
    }  
    
    
    public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
    	context = config.getServletContext();
	}
    
	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Servlet#getServletConfig()
	 */
	public ServletConfig getServletConfig() {
		
		return null;
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ModelAndView mv = new ModelAndView();
		mv.setRequest(request);
		String url = request.getRequestURI().replaceFirst(request.getContextPath(), "");
		try {
			String anStr = "@MVC.RequestMapping(method=[], value=["+url+"])";
			Set<Class<?>> s = getClasses("test");
			for (Class<?> c : s) {  
				Class<?> obj = Class.forName(c.getName());
				Method[] mths = obj.getMethods();
				for(Method m : mths){
					Annotation[] annos = m.getAnnotations();
					for(Annotation anno : annos){
						if(anStr.equals(anno.toString())){
							mv = (ModelAndView) m.invoke(c.newInstance(), mv);
						}
					}
				}
			}  
			request.getRequestDispatcher(mv.getView()).forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return;
	}
	
	public static Set<Class<?>> getClasses(String pack) {  
		  
        // ��һ��class��ļ���  
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();  
        // �Ƿ�ѭ������  
        boolean recursive = true;  
        // ��ȡ�������� �������滻  
        String packageName = pack;  
        String packageDirName = packageName.replace('.', '/');  
        // ����һ��ö�ٵļ��� ������ѭ�����������Ŀ¼�µ�things  
        Enumeration<URL> dirs;  
        try {  
            dirs = Thread.currentThread().getContextClassLoader().getResources(  
                    packageDirName);  
            // ѭ��������ȥ  
            while (dirs.hasMoreElements()) {  
                // ��ȡ��һ��Ԫ��  
                URL url = dirs.nextElement();  
                // �õ�Э�������  
                String protocol = url.getProtocol();  
                // ��������ļ�����ʽ�����ڷ�������  
                if ("file".equals(protocol)) {
                    // ��ȡ��������·��  
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");  
                    // ���ļ��ķ�ʽɨ���������µ��ļ� ����ӵ�������  
                    findAndAddClassesInPackageByFile(packageName, filePath,  
                            recursive, classes);  
                }
            }
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
        return classes;  
    }  
        public static void findAndAddClassesInPackageByFile(String packageName,  
                String packagePath, final boolean recursive, Set<Class<?>> classes) {  
            // ��ȡ�˰���Ŀ¼ ����һ��File  
            File dir = new File(packagePath);  
            // ��������ڻ��� Ҳ����Ŀ¼��ֱ�ӷ���  
            if (!dir.exists() || !dir.isDirectory()) {  
                // log.warn("�û�������� " + packageName + " ��û���κ��ļ�");  
                return;  
            }  
            // ������� �ͻ�ȡ���µ������ļ� ����Ŀ¼  
            File[] dirfiles = dir.listFiles(new FileFilter() {  
                // �Զ�����˹��� �������ѭ��(������Ŀ¼) ��������.class��β���ļ�(����õ�java���ļ�)  
                public boolean accept(File file) {  
                    return (recursive && file.isDirectory())  
                            || (file.getName().endsWith(".class"));  
                }  
            });  
            // ѭ�������ļ�  
            for (File file : dirfiles) {  
                // �����Ŀ¼ �����ɨ��  
                if (file.isDirectory()) {  
                    findAndAddClassesInPackageByFile(packageName + "."  
                            + file.getName(), file.getAbsolutePath(), recursive,  
                            classes);  
                } else {  
                    // �����java���ļ� ȥ�������.class ֻ��������  
                    String className = file.getName().substring(0,  
                            file.getName().length() - 6);  
                    try {  
                        // ��ӵ�������ȥ  
                        //classes.add(Class.forName(packageName + '.' + className));  
                                             //�����ظ�ͬѧ�����ѣ�������forName��һЩ���ã��ᴥ��static������û��ʹ��classLoader��load�ɾ�  
                                            classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName + '.' + className));    
                                    } catch (ClassNotFoundException e) {  
                        // log.error("����û��Զ�����ͼ����� �Ҳ��������.class�ļ�");  
                        e.printStackTrace();  
                    }  
                }  
            }  
        }  
}