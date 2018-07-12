package znet.dataInterface.dataTable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class Jc_point_config {

	
	public static Map<String, String> head(){
		Map<String, String> result = new LinkedHashMap<>();
		result.put("point_id", "point_id");
		result.put("point_no", "point_no");
		result.put("point_type", "point_type");
		result.put("unit", "unit");
		result.put("range_up", "range_up");
		result.put("range_down", "range_down");
		result.put("point_name", "point_name");
		result.put("equipment_place", "equipment_place");
		result.put("ch4_sensor_placeno", "ch4_sensor_placeno");
		result.put("cutout_area", "cutout_area");
		result.put("alarm_limit", "alarm_limit");
		result.put("relieve_alarm_value", "relieve_alarm_value");
		result.put("poweroff_limit", "poweroff_limit");
		result.put("poweron_limit", "poweron_limit");
		result.put("feed_sensor_id", "feed_sensor_id");
		result.put("config_date", "config_date");
		return result;
	}
	

	
}
