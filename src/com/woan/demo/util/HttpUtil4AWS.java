package com.woan.demo.util;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import com.woaner.security.utils.SignatureUtils;
import com.woaner.security.utils.StreamTools;

public class HttpUtil4AWS {

	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	
	private static String hostAddress="http://c.zhiboyun.com";
	
	/**
	 * get  方法获取 请求aws服务器 发起一个任务
	 * 
	 * @param  uri  例如: 创建任务 的 uri： /api/20140928/create_task_with_conf
	 * @param  code 对应服务码
	 * @return secret_key 签名密钥
	 * @param  queryString  转码的xml 文件
	 */
	public static String createTaskByAws(String uri,String code ,String secret_key,String queryString){
		String xvs_signature=null;
		try {
			String u1="";
			try {
				u1=URLEncoder.encode(queryString, "utf-8");
				u1=u1.replace("+", "%20");
				System.err.println("转义后的结果ul"+u1);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String aq="&config="+u1;
			String url = hostAddress+uri+"?"+"service_code="+code+aq;
			  // 实例化HTTP方法   
            HttpGet httpGet = new HttpGet(url);  
            System.err.println(url);
            String timeStamp=SignatureUtils.getTimeStamp();
            queryString="&config="+queryString;
            httpGet.setHeader("xvs-timestamp", timeStamp);
            xvs_signature=  SignatureUtils.getSignature(secret_key, uri, code, queryString,timeStamp);
            httpGet.setHeader("xvs-signature", xvs_signature);
            CloseableHttpResponse response = null;
            String result =null;
	        boolean flag_xh = true;
	        int i =10;
	       while (i>0&&flag_xh) {
	        	try {
					response = httpclient.execute(httpGet);
					flag_xh=false;
		            HttpEntity entity2 = response.getEntity();
		            result = StreamTools.readFromStream(entity2.getContent());
		            response.close();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					i--;
				} catch (IOException e) {
					i--;
					e.printStackTrace();
				}
				System.err.println("aws 请求结果" + result);
			}
	        return result;
		} catch (Exception e) {
			System.err.println("createTaskByAws方法 获取签名失败");
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * get  方法获取  task_list 信息
	 * @param  uri  例如: 获取 直播列表 的 uri： /api/20140928/task_list 
	 * @param  code 对应服务码
	 * @return secret_key 签名密钥
	 * @param  queryString 查询的内容的参数 内容  没有 可以写""
	 */
	public static String getTaskListByAws(String uri,String code ,String secret_key,String queryString){
		String xvs_signature=null;
		try {
			String url = hostAddress+uri+"?"+"service_code="+code+queryString;
            HttpGet httpGet = new HttpGet(url);  
            String timeStamp=SignatureUtils.getTimeStamp();
            httpGet.setHeader("xvs-timestamp", timeStamp);
            xvs_signature=  SignatureUtils.getSignature(secret_key, uri, code, queryString,timeStamp);
            httpGet.setHeader("xvs-signature", xvs_signature);
            CloseableHttpResponse response = null;
			  String result =null;
		        boolean flag_xh = true;
		        int i =10;
		        while (i>0&&flag_xh) {
		        	try {
						response = httpclient.execute(httpGet);
						flag_xh=false;
			            HttpEntity entity2 = response.getEntity();
			            result = StreamTools.readFromStream(entity2.getContent());
			            response.close();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
						i--;
					} catch (IOException e) {
						i--;
						e.printStackTrace();
					}
					System.err.println("getTaskListByAws 结果" + result);
				}
		        return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 调用直播云服务器提供的api 方法
	 * @param postData   json 类型的字符串 
	 * @return
	 */
	public  static String  api_for_aws(String postData,String serviceCode,String key){
		String timeStamp=SignatureUtils.getTimeStamp();
		String queryString ="service_code="+serviceCode;
		String data = postData;
		String xvs_signature = SignatureUtils.getSignature_for_management_post("/api/20140928/management", key,queryString, data,timeStamp);
		String prfAdd="http://c.zhiboyun.com";
		String uri ="/api/20140928/management";
		StringBuffer urlBuffer = new StringBuffer(prfAdd);
		urlBuffer.append(uri);
		urlBuffer.append("?");
		urlBuffer.append(queryString);
		String url = urlBuffer.toString();
		HttpPost httpPost  = new HttpPost(url);
		httpPost.setHeader("xvs-timestamp", timeStamp);
        httpPost.setHeader("xvs-signature", xvs_signature);
		//如果不编码  则以iso——8859——1
        StringEntity entity = new StringEntity(data,"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");    
        httpPost.setEntity(entity); 
        CloseableHttpResponse response=null;
        String result =null;
        boolean flag_xh = true;
        int i =10;
        while (i>0&&flag_xh) {
        	try {
				response = httpclient.execute(httpPost);
				flag_xh=false;
	            HttpEntity entity2 = response.getEntity();
	            result = StreamTools.readFromStream(entity2.getContent());
	            response.close();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				i--;
			} catch (IOException e) {
				i--;
				e.printStackTrace();
			}
		}
        return result;
        
	}
}
