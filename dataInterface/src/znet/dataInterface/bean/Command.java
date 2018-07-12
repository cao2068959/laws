package znet.dataInterface.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import znet.dataInterface.core.DateUtil;


public class Command {

	
	private String code;
	private String param;
	private String[] params;
	
	private Date startDate;
	private Date endDate;
	private List<Integer> ids;
	
	

	public boolean paramFormat(){
		if(params == null){
			return false;
		}
		if(params.length < 3){
			return false;
		}
		
		startDate =  DateUtil.parse(params[0], "yyyy-MM-dd");
		if(startDate == null){
			return false;
		}
		endDate =  DateUtil.parse(params[1], "yyyy-MM-dd");
		if(endDate == null){
			return false;
		}
		ids = new ArrayList<>();
		for(int i = 2 ;i < params.length;i++){
			try {
				Integer id =Integer.parseInt(params[i].trim());
				ids.add(id);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
	
	
	
	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public List<Integer> getIds() {
		return ids;
	}


	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}


	
	public String[] getParams() {
		return params;
	}
	public void setParams(String[] params) {
		this.params = params;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code =  code.trim();
	}
	public String getParam() {
		return param;
	}
	public void setParam(String param) {
		this.param = param;
		if(param != null){
			String[] ps = param.split(",");
			params = ps;
		}
	}
	
	
	
	
	
	@Override
	public String toString() {
		return "Command [code=" + code + ", param=" + param + "]";
	}
}
