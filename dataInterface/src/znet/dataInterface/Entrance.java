package znet.dataInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import znet.dataInterface.core.ScanMapping;
import znet.dataInterface.core.SocketHandle;

public class Entrance {

	private String port = null;
	public static String path = null;
	public static String urlRoot = null;
	
	
	public Entrance() {
		Properties pro = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream("config.properties");
			pro.load(in);
			port = pro.getProperty("tool_port", "35000");
			path = pro.getProperty("csv_path", "");
			
			String aj_ip = pro.getProperty("aj_ip", "");
			String aj_port = pro.getProperty("aj_port", "");
			String aj_virtual = pro.getProperty("aj_virtual", "");
			urlRoot = "http://"+aj_ip+":"+aj_port+aj_virtual+"/lawInterface/";
			System.out.println("当前端口号:" + port);
			System.out.println("csv生成路径:" + path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				in.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	
	//@PostConstruct
	public void start(){
		final Integer portInt = Integer.parseInt(port);
		startService(portInt);
	}
	public void startService(int port){
		
		try {
			ScanMapping.start();
			ServerSocket serverSocket = new ServerSocket(port);
			File f =new File("");
			System.out.println(f.getAbsolutePath());
			System.out.println("服务开始");
			while(true){
				Socket socket = serverSocket.accept();
				System.out.println("新用户登录");
				SocketHandle socketHandle  = new SocketHandle(socket);
				Thread thread = new Thread(socketHandle);
				thread.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Entrance e =new Entrance();
		e.start();
	}
	
	
	
	
}
