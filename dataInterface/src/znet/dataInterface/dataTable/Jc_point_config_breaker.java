package znet.dataInterface.dataTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class Jc_point_config_breaker {

	
	public static Map<String, String> head(){
		Map<String, String> result = new LinkedHashMap<>();
		result.put("point_id", "point_id");
		result.put("relation_id", "relation_id");
		result.put("config_time", "config_time");
		return result;
	}
	
	
}
