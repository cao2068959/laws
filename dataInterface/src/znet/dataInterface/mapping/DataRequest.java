package znet.dataInterface.mapping;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;

import znet.dataInterface.Entrance;
import znet.dataInterface.core.CsvTool;
import znet.dataInterface.core.DateUtil;
import znet.dataInterface.core.HttpTool;
import znet.dataInterface.dataTable.Jc_abnormal;
import znet.dataInterface.dataTable.Jc_history_ai;
import znet.dataInterface.dataTable.Jc_point_config;
import znet.dataInterface.dataTable.Jc_point_config_breaker;
import znet.dataInterface.dataTable.Jc_status_change_di;
import znet.dataInterface.dataTable.Jc_sys_log;

public class DataRequest {

	
	
	
	public List<List<String>> fetchAllPoint() throws Exception{
		String url = Entrance.urlRoot + "getAllPoint";
		String dataStr = HttpTool.sendPost(url, null);
		List<List<String>>  result =  (List<List<String>>) JSON.parse(dataStr);
		return result;
	}
	
	public void generatePointConfig() throws Exception{
		String url = Entrance.urlRoot + "getpointConfig";
		String dataStr = HttpTool.sendPost(url, null);
		Map<String, List<Map<String, Object>>>  queryData =  (Map<String, List<Map<String, Object>>>) JSON.parse(dataStr);
		if(queryData != null){
			List<Map<String, Object>> pointConfig = queryData.get("jc_point_config");
			CsvTool.writeCSV(Jc_point_config.head(), pointConfig, Entrance.path+"/jc_point_config.csv");
			List<Map<String, Object>> pointConfigBreaker = queryData.get("jc_point_config_breaker");
			CsvTool.writeCSV(Jc_point_config_breaker.head(), pointConfigBreaker, Entrance.path+"jc_point_config_breaker.csv");						
		}
		
	}	
	
	
	public void generateGetHistoryData(List<Integer> list, Date start, Date end) throws Exception{
		String url = Entrance.urlRoot + "getHistoryData";
		String ids = "";
		for(int i =0 ;i < list.size();i++){
			if(i != 0){
				ids = ids + ",";
			}
			ids = ids +  list.get(i);
		}
		String startStr = DateUtil.format(start);
		String endtStr = DateUtil.format(end);
		
		Map<String, String> param = new HashMap<>();
		param.put("ids", ids);
		param.put("start", startStr);
		param.put("end", endtStr);
		String dataStr = HttpTool.sendPost(url, param);
		Map<String, List<Map<String, Object>>>  result = (Map<String, List<Map<String, Object>>>) JSON.parse(dataStr);
		if(result != null){
			List<Map<String, Object>> hisai = result.get("jc_history_ai");
			CsvTool.writeCSV(Jc_history_ai.head(), hisai, Entrance.path+"jc_history_ai.csv");
			
			List<Map<String, Object>> statusChange = result.get("jc_status_change_di");
			CsvTool.writeCSV(Jc_status_change_di.head(), statusChange, Entrance.path+"jc_status_change_di.csv");
			
			List<Map<String, Object>> abnormalData = result.get("jc_abnormal");
			CsvTool.writeCSV(Jc_abnormal.head(), abnormalData, Entrance.path+"jc_abnormal.csv");
			
			CsvTool.writeCSV(Jc_sys_log.head(), null, Entrance.path+"jc_sys_log.csv");
		}
		
	
	}
	
	public void generateSysLog(Date start, Date end) throws Exception{
		String url = Entrance.urlRoot + "getSysLog";

		String startStr = DateUtil.format(start);
		String endtStr = DateUtil.format(end);
		Map<String, String> param = new HashMap<>();
		param.put("start", startStr);
		param.put("end", endtStr);
		String dataStr = HttpTool.sendPost(url, param);
		List<Map<String, Object>>  result = (List<Map<String, Object>>) JSON.parse(dataStr);
		CsvTool.writeCSV(Jc_sys_log.head(), result, Entrance.path+"jc_sys_log.csv");
	}
	
	
}
