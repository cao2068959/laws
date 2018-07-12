package znet.dataInterface.core;

public class HttpException extends Exception{
	
	
	public HttpException(int code) {
		super("请求安监系统异常："+code);
	}
}
