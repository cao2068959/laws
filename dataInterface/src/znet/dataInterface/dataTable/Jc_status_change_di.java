package znet.dataInterface.dataTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class Jc_status_change_di {

	public static Map<String, String> head(){
		Map<String, String> result = new LinkedHashMap<>();
		result.put("point_id", "point_id");
		result.put("change_time", "change_time");
		result.put("current_status", "current_status");
		return result;
	}
	
	
}
