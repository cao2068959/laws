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
	
 
	/*��ȡ�������*/
	private static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	/*������*/
	private static Class<?> loadClass(String className, boolean isInitail) {
		Class<?> cls = null;
		try{
			cls = Class.forName(className, isInitail, getClassLoader());
		} catch(ClassNotFoundException e){
			throw new RuntimeException(e) ;
		}
		return cls;
	}
	/*�����ļ��е���*/
	private static void addClass(Set<Class<?>> classSet, String packagePath,
			String packageName) {
		File[] files = new File(packagePath).listFiles(new FileFilter() {
			public boolean accept(File file) {
				//��ȡ��.class��׺���ļ�,�Լ����е��ļ�Ŀ¼
				return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
			}
		});
		
		for(File file : files) {
			String fileName = file.getName();
			if(file.isFile()) {
				//��.class��β�����ļ�
				String className = fileName.substring(0, fileName.lastIndexOf("."));
				if (StringUtils.isNotEmpty(className)) {
					className = packageName + "." + className;
				}
				//������
				doAddClass(classSet, className);
			} else {
				String subPackagePath = fileName;
				//��ȡ��·��
				if(StringUtils.isNotEmpty(subPackagePath)){
					subPackagePath = packagePath + "/" + subPackagePath;
				}
				//��ȡ�Ӱ���
				String subPackageName = fileName;
				if(StringUtils.isNotEmpty(subPackageName)) {
					subPackageName = packageName + "." + subPackageName;
							
				}
				//�ݹ������Ŀ¼�µ���
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
						String packagePath = url.getPath().replaceAll("%20", " "); //�滻���е�%20Ϊ�ո�
						addClass(classSet, packagePath, packageName); //�����ļ���,�Լ��ļ����������
					} else if(protocol.equals("jar")) {
						//��ѹjar��Ȼ���ȡentry
						//entry��ʽ����org/apache/commons/lang3/time/FormatCache$MultipartKey.class 
						
						JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
						if(null != jarURLConnection) {
							JarFile jarFile = jarURLConnection.getJarFile();
							if(null != jarFile) {
								Enumeration<JarEntry> jarEntries = jarFile.entries(); //��ȡ���е�entry
								while(jarEntries.hasMoreElements()) {
									JarEntry jarEntry = jarEntries.nextElement();
									String jarEntryName = jarEntry.getName();
									if(jarEntryName.endsWith(".class")) { //�Ƿ�����,������м���
 
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
			result.setDes("��Ӧָ��û��ӳ�䷽��");
			return result;
		}
		
		try {
			String resultData = (String) m.invoke(m.getDeclaringClass().newInstance(), param);
			result.setSuccess(true);
			result.setData(resultData);
			return result;
		} catch (IllegalAccessException e) {
			result.setDes("Ȩ���쳣");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			result.setDes("�����쳣");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			result.setDes(e.getCause().getMessage());
		} catch (InstantiationException e) {
			result.setDes("ʵ�����쳣");
			e.printStackTrace();
		}catch(Exception e){
			result.setDes("�����쳣");
		}
		
		return result;
	
	}
	
	
	
	
	
	
	public static void main(String[] args) {
		
		
		
	}
	
}
