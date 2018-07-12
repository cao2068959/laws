package znet.dataInterface.core;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;

public class HttpTool {

	private static HttpEntity entity;

	public static String sendPost(String url,Map<String, String> params) throws Exception {
		CloseableHttpResponse response = null;
		CloseableHttpClient httpClient = null;
		try {
			 httpClient = HttpClients.createDefault();
			HttpPost post =new HttpPost(url);
			if(params != null){
				List<NameValuePair>kvList = new ArrayList<>();
				for (Entry<String, String> param:params.entrySet()) {
					String key = param.getKey();
					String value = param.getValue();
					kvList.add(new BasicNameValuePair(key,value));
				}
				StringEntity entity = new UrlEncodedFormEntity(kvList,"utf-8");
				post.setEntity(entity);
			}
			 response =httpClient.execute(post);
			 HttpEntity v = response.getEntity();
			 int statusCode = response.getStatusLine().getStatusCode();
			 if(statusCode != 200){
				 HttpException e = new HttpException(statusCode);
				 throw e;
			 }
			 
			String string = EntityUtils.toString(v);
			return string;
			
		}finally {
			try {
				response.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				httpClient.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public static List<Map<String,Object>> sendPost2lm(String url,Map<String, String> params) throws Exception{
		String resultStr = sendPost(url,params);
		List<Map<String,Object>> result = (List<Map<String,Object>>) JSONObject.parse(resultStr);
		return result;
	}
		
	public static void main(String[] args) throws Exception {
			System.out.println(HttpTool.sendPost("http://127.0.0.1:9997/lawInterface/getAllPoint",null));
	}
	
	

}
