package znet.dataInterface.core;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.csvreader.CsvWriter;


public class CsvTool {
	/**
	 * �������� ����CSV�ļ�
	 * @param head ��ͷ��key --- �����ַ���   value --- ��ͷ ��ʾ�ֶ�   ��ͷ��˳��������ݵ�˳��ʹ�� LinkedHashMap 
	 * @param datas ����  ����map -->  key---- �ͱ�ͷkey��Ӧ  value ----- �����ֶ�
	 * @param path svg��ַ
	 * @throws IOException 
	 */
	public static void writeCSV(Map<String, String> head,List<Map<String, Object>> datas,String path) throws IOException{
		CsvWriter csvWriter = new CsvWriter(path,',', Charset.forName("GBK"));
		
		try {
			int headSize = head.size();
			String[] headKey = new String[headSize];
			String[] headValue = new String[headSize];
			int headIndex = 0;
			for (Entry<String, String> entry : head.entrySet()) {
				headKey[headIndex] = entry.getKey();
				headValue[headIndex] = entry.getValue();
				headIndex++;
		    }
			//д��ͷ
			csvWriter.writeRecord(headValue);
			
			if(datas == null){
				return;
			}
			for(Map<String, Object> row : datas){
				String[] rowValue = new String[headSize];
				int rowIndex = 0;
				for(String headFleid:headKey){
					 String cowStr = "";
					 Object cow =  row.get(headFleid);
					 if(cow != null){
						 cowStr = cow+"";
					 }
					 rowValue[rowIndex] = cowStr;
					 rowIndex++;
				}
				
				//д����
				csvWriter.writeRecord(rowValue);
			}
		} catch (IOException e) {
			throw e;
		}finally{
			csvWriter.close();
		}
	}
	
	
	public static void checkDir(String path){
		
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		
	}
	
	
	
	public static void main(String[] args) {
		CsvTool c = new CsvTool();
		CsvTool.checkDir("D:\\lawToolFile\\");
		
	
	}

}
