package znet.dataInterface;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.commons.lang.ArrayUtils;

import znet.dataInterface.bean.Command;
import znet.dataInterface.bean.MethodResult;

public class ClientTest {
	
	
	
	public void run(){
		try {
			Socket socket = new Socket("192.168.2.172", 35000);
			OutputStream os = socket.getOutputStream();
			InputStream in = socket.getInputStream();
			while(true){
				send(os);
				read(in);
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	private void send(OutputStream os) throws IOException{
		Scanner s = new Scanner(System.in);
		System.out.println("输入命令");
		String code =s.nextLine();
		System.out.println("输入参数");
		String param =s.nextLine();
		
		byte[] cc = code.getBytes();
		byte[] codeBuff = new byte[20];
		for(int i=0; i<codeBuff.length;i++){
			byte dd= 0;
			try {
				dd = cc[i]; 
			} catch (Exception e) {
				dd= 0;
			}
			codeBuff[i] = dd;
		}
		
		byte[] buffs=  param.getBytes("utf-8");
		
		int total = 24 + buffs.length;
		byte[] lenBuff = intToBytes(total);
		
		byte[] ban = ArrayUtils.addAll(lenBuff, codeBuff);
		byte[] result = ArrayUtils.addAll(ban, buffs);
		os.write(result);
	}
		
	public static void read(InputStream in) throws IOException{
		 DataInputStream dis = new DataInputStream(in);
		 Integer packageLen = dis.readInt();
		 Integer paramLen = packageLen - 4;
		 byte[] paramBuff = readByte(dis, paramLen);
		  String param = new String(paramBuff,"utf-8");
		  System.out.println(param);	
	}
	
	
	
	
	public static byte[] intToBytes(int a) {   
		return new byte[] {   
		        (byte) ((a >> 24) & 0xFF),   
		        (byte) ((a >> 16) & 0xFF),      
		        (byte) ((a >> 8) & 0xFF),      
		        (byte) (a & 0xFF)   
		    };   
		} 
	
	public static void main(String[] args) {
		new ClientTest().run();
		//byte[] lenBuff = intToBytes(17);

		
	}
	
	
	public static byte[] readByte(DataInputStream dis,Integer len){
		
		byte[] paramBuff = new byte[1024];
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			
			Integer readNum = 0;
			
			while(readNum <  len){
				int nextNum = len - readNum;
				if(nextNum > 1024){
					nextNum = 1024;
				}
				int n = dis.read(paramBuff,0,nextNum);
				readNum = readNum + n;
				bos.write(paramBuff, 0, n);
			}
			return bos.toByteArray();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				bos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return paramBuff;
	}
	

}
