package znet.dataInterface.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang.StringUtils;

import znet.dataInterface.bean.MethodResult;

public class ScanMapping {
	
	public static Map<String, Method> methodAnnotations = new HashMap<String,Method>();
	
 
	/*获取类加载器*/
	private static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	/*加载类*/
	private static Class<?> loadClass(String className, boolean isInitail) {
		Class<?> cls = null;
		try{
			cls = Class.forName(className, isInitail, getClassLoader());
		} catch(ClassNotFoundException e){
			throw new RuntimeException(e) ;
		}
		return cls;
	}
	/*加载文件夹的类*/
	private static void addClass(Set<Class<?>> classSet, String packagePath,
			String packageName) {
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			public boolean accept(File file) {
				//获取以.class后缀的文件,以及所有的文件目录
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
			}
		});
		
		for(File file : files) {
			String fileName = file.getName();
			if(file.isFile()) {
				//以.class结尾的类文件
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if (StringUtils.isNotEmpty(className)) {
					className = packageName + "." + className;
				}
				//加载类
				doAddClass(classSet, className);
			} else {
				String subPackagePath = fileName;
				//获取子路径
				if(StringUtils.isNotEmpty(subPackagePath)){
					subPackagePath = packagePath + "/" + subPackagePath;
				}
				//获取子包名
				String subPackageName = fileName;
				if(StringUtils.isNotEmpty(subPackageName)) {
					subPackageName = packageName + "." + subPackageName;
							
				}
				//递归加载子目录下的类
				addClass(classSet, subPackagePath, subPackageName);
			}
			
		}
		
	}
	private static void doAddClass(Set<Class<?>> classSet, String className) {
		//log.info(className);
		Class<?> cls = loadClass(className, false);
		classSet.add(cls);
	}
	private static Set<Class<?>> getClassSet(String packageName) {
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		
		try{
			Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
			Enumeration<URL> urls1 = getClassLoader().getResources(packageName.replace(".", "/"));
			while(urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if(null != url) {
					String protocol = url.getProtocol();
					if(protocol.equals("file")) {
						String packagePath = url.getPath().replaceAll("%20", " "); //替换所有的%20为空格
						addClass(classSet, packagePath, packageName); //加载文件夹,以及文件夹下面的类
					} else if(protocol.equals("jar")) {
						//解压jar包然后获取entry
						//entry格式如下org/apache/commons/lang3/time/FormatCache$MultipartKey.class 
						
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						if(null != jarURLConnection) {
							JarFile jarFile = jarURLConnection.getJarFile();
							if(null != jarFile) {
								Enumeration<JarEntry> jarEntries = jarFile.entries(); //获取所有的entry
								while(jarEntries.hasMoreElements()) {
									JarEntry jarEntry = jarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if(jarEntryName.endsWith(".class")) { //是否是类,是类进行加载
 
										String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
										doAddClass(classSet, className);
									}
								}
							}
						}
					}
					
				}
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		return classSet;
	}
	
	
	private static void scanAnnotation(Class clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Mapping.class)) {
				// ParseFlexData.methodMap.put(method.getName(), clazz);
				Mapping mapping = method.getAnnotation(Mapping.class);
				String[] mappingName = mapping.name();
				if (mappingName.length != 0) {
					methodAnnotations.put(mappingName[0], method);
				} 
			}
		}
	}
	
	
	
	public static void start(){
		
		Set<Class<?>> classes = getClassSet("znet.dataInterface.mapping");
		for(Class c : classes){
			scanAnnotation(c);
		}
		//System.out.println(methodAnnotations);
	} 
	
	
	
	public static MethodResult execMethod(String methodName,Object... param){
		Method m =  methodAnnotations.get(methodName);
		MethodResult result = new MethodResult();
		if(m == null){
			result.setDes("对应指令没有映射方法");
			return result;
		}
		
		try {
			String resultData = (String) m.invoke(m.getDeclaringClass().newInstance(), param);
			result.setSuccess(true);
			result.setData(resultData);
			return result;
		} catch (IllegalAccessException e) {
			result.setDes("权限异常");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			result.setDes("参数异常");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			result.setDes(e.getCause().getMessage());
		} catch (InstantiationException e) {
			result.setDes("实例化异常");
			e.printStackTrace();
		}catch(Exception e){
			result.setDes("其他异常");
		}
		
		return result;
	
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		
		
		
	}
	
}
