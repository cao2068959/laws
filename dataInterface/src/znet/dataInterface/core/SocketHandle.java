package znet.dataInterface.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.apache.commons.lang.ArrayUtils;

import znet.dataInterface.bean.Command;
import znet.dataInterface.bean.MethodResult;
import znet.dataInterface.core.ScanMapping;
import znet.dataInterface.core.Tool;

public class SocketHandle implements Runnable{

	private Socket socket;
	
	
	
    public SocketHandle(Socket socket) {
		this.socket = socket;
	} 
	
	
	
	@Override
	public void run() {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		 try {
			 inputStream = socket.getInputStream();
			 outputStream = socket.getOutputStream();
			 int totalLen = 0;

			 DataInputStream dis = new DataInputStream(inputStream);
			 Integer packageLen = 0;
			 //���ȶ�ȡ�����
			 while((packageLen = dis.readInt()) !=0){
				Integer paramLen = packageLen - 24;
				//�ҷ� ���߲���
				if(paramLen < 0 ){
					break;
				}
				Command command = new Command(); 
				//��ȡ��������
				//byte[] codeBuff =new byte[20]; 
				//dis.read(codeBuff);
				byte[] codeBuff = readByte(dis, 20);
				String code = new String(codeBuff);
				command.setCode(code);
				//��ȡʣ�µ���������
				//byte[] paramBuff = new byte[paramLen];
				//dis.read(paramBuff);
				byte[] paramBuff = readByte(dis, paramLen);
				
				String param = new String(paramBuff,"utf-8");
				command.setParam(param);
				//ִ�ж�Ӧ����
				MethodResult methodResult = excCommand(command);
				//�������������Ľ��
				sendProxy(methodResult,outputStream);
				
			 }
		
			 
		} catch (EOFException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				inputStream.close();
 			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				outputStream.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			try {
				socket.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("�ͻ�������");
		}
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
	
	
	/**
	 * ���� methodResult �еĽ�� �жϷ������ݣ����Ǵ�����Ϣ
	 * @param methodResult
	 * @param outputStream
	 */
	public void sendProxy(MethodResult methodResult,OutputStream outputStream){
		Boolean isSuccess = methodResult.getSuccess();
		String data= null;
		if(isSuccess){
			 data = methodResult.getData();
			
		}else{
			 data = Tool.getError(methodResult.getDes());
		}
		
		if(data != null){
			sendData(data,outputStream);
		}
		
	}
	
	
	
	
	
	/**
	 * �����ܳ��ȣ�����������
	 * @param data
	 * @param outputStream
	 */
	public void sendData(String data,OutputStream outputStream){
		try {
			byte[] buffs=  data.getBytes("utf-8");
			int total = 4 + buffs.length;
			byte[] lenBuff = intToBytes(total);			
			byte[] resultBuff = ArrayUtils.addAll(lenBuff, buffs);
			outputStream.write(resultBuff);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	/**
	 * ����ִ�ж�Ӧ����
	 * @param com
	 * @return
	 */
	public MethodResult excCommand(Command com){
		if(com == null || com.getCode() == null || com.getCode() == ""){
			return null;
		}
		String code = com.getCode();
		MethodResult result = ScanMapping.execMethod(code, com);
		return result;
	}
	
	/**
	 * 4����ת int
	 * @param b
	 * @return
	 */
	public static int byteToInt2(byte[] b) {

        int mask=0xff;
        int temp=0;
        int n=0;
        for(int i=0;i<b.length;i++){
           n<<=8;
           temp=b[i]&mask;
           n|=temp;
       }
        return n;
}
	/**
	 * intת��4byte
	 * @param a
	 * @return
	 */
	public static byte[] intToBytes(int a) {   
		return new byte[] {   
		        (byte) ((a >> 24) & 0xFF),   
		        (byte) ((a >> 16) & 0xFF),      
		        (byte) ((a >> 8) & 0xFF),      
		        (byte) (a & 0xFF)   
		    };   
		} 
	
	
	
}
