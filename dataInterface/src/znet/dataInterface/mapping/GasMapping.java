package znet.dataInterface.mapping;

import java.io.IOException;
import java.util.List;

import znet.dataInterface.Entrance;
import znet.dataInterface.bean.Command;
import znet.dataInterface.core.CsvTool;
import znet.dataInterface.core.Mapping;
import znet.dataInterface.core.Tool;

public class GasMapping {

	

	
	@Mapping(name="CONNECT_TEST")
	public String connectTest(Command command){
		return "CONNECT_SUCCEED";
	}
	
	
	
	@Mapping(name="FETCH_ALL_POINT")
	public String fetchAllPoint (Command command) throws Exception{
		DataRequest service = new DataRequest();
		List<List<String>> list = service.fetchAllPoint();
		String result = Tool.getSendData(list);
		return result;
	}
	
	@Mapping(name="EXPORT_DATA_FILE")
	public String exportDataFile (Command command) throws Exception{	
		boolean isSuccess = command.paramFormat();
		if(!isSuccess){
			return Tool.getError("≤Œ ˝“Ï≥£");
		}
		CsvTool.checkDir(Entrance.path);
		DataRequest service = new DataRequest();
		service.generatePointConfig();
		service.generateGetHistoryData(command.getIds(),command.getStartDate(),command.getEndDate());
		return Entrance.path;
	}
	
	
	public static void main(String[] args) throws IOException {
		GasMapping s = new GasMapping();
		try {
			s.exportDataFile(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
