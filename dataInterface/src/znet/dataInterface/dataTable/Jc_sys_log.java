package znet.dataInterface.dataTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class Jc_sys_log {

	public static Map<String, String> head(){
		Map<String, String> result = new LinkedHashMap<>();
		result.put("username", "username");
		result.put("operate_time", "operate_time");
		result.put("operate_type", "operate_type");
		result.put("operate_info", "operate_info");

		return result;
	}
	
	
}
