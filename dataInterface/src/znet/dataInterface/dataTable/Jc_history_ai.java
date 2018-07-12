package znet.dataInterface.dataTable;

import java.util.LinkedHashMap;
import java.util.Map;

public class Jc_history_ai {

	public static Map<String, String> head(){
		Map<String, String> result = new LinkedHashMap<>();
		result.put("point_id", "point_id");
		result.put("point_no", "point_no");
		result.put("max_value", "max_value");
		result.put("max_time", "max_time");
		result.put("min_value", "min_value");
		result.put("min_time", "min_time");
		result.put("avg_value", "avg_value");
		result.put("the_time", "the_time");
		return result;
	}
	
	
}
