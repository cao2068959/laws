package znet.dataInterface.dataTable;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import znet.dataInterface.core.DateUtil;


public class Jc_abnormal {

	public static Map<String, String> head(){
		Map<String, String> result = new LinkedHashMap<>();
		result.put("point_id", "point_id");
		result.put("point_no", "point_no");
		result.put("max_value", "max_value");
		result.put("max_time", "max_time");
		result.put("min_value", "min_value");
		result.put("min_time", "min_time");
		result.put("avg_value", "avg_value");
		result.put("abnormal_flag", "abnormal_flag");
		result.put("begin_time", "begin_time");
		result.put("end_time", "end_time");
		result.put("measure", "measure");
		result.put("measure_time", "measure_time");
		result.put("trail_type", "trail_type");

		return result;
	}
	
	/**
	 * 原始数据转 表jc_abnormal格式数据
	 * @param source 数据源
	 * @param flag  1报警 2断电 3断网
	 * @param isAnalog true模拟量 false 开关量
	 */
	public static Map<String, Object> map2abnoramlFormat(Map<String, Object> source,int flag,boolean isAnalog){
		Map<String, Object> result = new HashMap<>();
		result.put("point_id", source.get("sneosrId"));
		result.put("point_no", source.get("sensorNo"));
		result.put("abnormal_flag", flag);
		result.put("begin_time", DateUtil.format((Date) source.get("startTime")));
		result.put("end_time", DateUtil.format((Date) source.get("endTime")));
		result.put("measure", source.get("step"));
		result.put("measure_time", DateUtil.format((Date) source.get("stepTime")));
		result.put("trail_type", 0);
		
		if(isAnalog){
			result.put("max_value", source.get("maxData"));
			result.put("max_time", DateUtil.format((Date) source.get("maxTime")));
			//result.put("min_value", source.get(""));
			//result.put("min_time", source.get(""));
			result.put("avg_value", source.get("avg"));
		}
		
		return result;
	}
	
}
