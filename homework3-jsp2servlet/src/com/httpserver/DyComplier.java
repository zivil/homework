package com.httpserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
 
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
 
public class DyComplier {
	private String  directdir = null;
    public void textJavaCompiler(String src, String dir){
        // 编译程序
    	String classname = src.substring(src.lastIndexOf("/")+1, src.lastIndexOf("."));
    	System.out.println(classname);
    	File srcf = new File(src);
    	String dirf = srcf.getAbsolutePath().substring(0, srcf.getAbsolutePath().lastIndexOf("\\"));
    	directdir = dirf;
    	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    	if(compiler == null)
    		System.out.println(System.getProperty("java.home"));
    	int result = compiler.run(null, null, null, "-d", dirf,srcf.getAbsolutePath()); 
        System.out.println( result == 0 ? "恭喜编译成功" : "对不起编译失败");
        try {  
        	System.out.println(dirf);
        	File file = new File(dirf);
        	URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
        	Method add = URLClassLoader.class.getDeclaredMethod("doserve", new Class[]{URL.class});
        	add.setAccessible(true);
        	add.invoke(classloader, new Object[]{file.toURI().toURL()});
        	Class c = classloader.loadClass("test");
        	Object o = c.newInstance();
        	Method m = c.getDeclaredMethod("doserve");
        	m.invoke(o, new Object[]{new Request(), new Response()});
          
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        //System.out.println(flag == 0 ? "编译成功" : "编译失败");  
    }
     
    public void textStringWrite() throws Exception{
        JavaCompiler complier = ToolProvider.getSystemJavaCompiler();     
        StandardJavaFileManager sjf =   
                complier.getStandardFileManager(null, null, null);  
        Iterable it = sjf.getJavaFileObjects("D:/Hello.java");
        CompilationTask task = complier.getTask(null, sjf, null, null, null, it);  
        task.call();  //调用创建  ,创建class文件
        sjf.close();  
           
        URL urls[] = new URL[]{ new URL("file:/D:/")}; //储存文件目录的地址
        URLClassLoader uLoad = new URLClassLoader(urls);  //classloader从哪个目录找？ 
        Class c = uLoad.loadClass("Hello");  //找哪个class文件 注意不带后缀名  
         
        Method method = c.getMethod("printString");
        String string = method.invoke(c.newInstance()).toString();
        System.out.println(string);
    }
 
}