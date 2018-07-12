package znet.dataInterface.core;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.ls.LSInput;

public class Tool {

	/**
	 * 获取 要发送错误信息的格式
	 * @param msg
	 * @return
	 */
	public static String getError(String msg){
		String result = "_ERROR_,"+msg;
		return result;
	}
	
	/**
	 * 把list转换成 客户端认可的格式
	 * 外层数据用;连接  内层string数据用,连接
	 * @param list
	 * @return
	 */
	public static String getSendData(List<List<String>> lists){
		String result = "";
		for(int i =0 ; i < lists.size();i++){
			List<String> datas = lists.get(i);
			
			for(int j = 0; j< datas.size();j++){
				String data = datas.get(j);
				if(data == null || "null".equals(data)){
					data= "";
				}
				result = result+data;
				if(j != datas.size() - 1){
					result  = result+",";
				}
			}
			result = result + ";";
		}
		
		return result;
	}
	
	public static String nullToEmpty(String data){
		if(data == null){
			return "";
		}
		return data;
		
	}
	
	
}
