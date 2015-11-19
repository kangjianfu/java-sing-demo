package com.woan.demo.zm;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.woan.demo.util.HttpUtil4AWS;
import com.woaner.security.utils.SignatureUtils;
import com.woaner.security.utils.StreamTools;

public class Test {
	public static void main(String[] args) {
		String xml="<root> <task> <input> <url>http://42.120.52.18/mp4/lrx.mp4</url></url> </input> <output tag=\"tr1\"> <extension>flv</extension> <codec-v>h264</codec-v> <codec-a>copy</codec-a> <size>480x180</size> </output> </task> </root>";
		//addAWSUser();
		testCreatTask(xml);
	}

	/**
	 * 获取当前服务码下正在直播的数据 AFM：服务码 f38e485a：签名密钥
	 * 
	 */
	public static void getTaskList() {
		String uri = "/api/20140928/task_list";
		String api_for_aws_by_getMethod = HttpUtil4AWS.getTaskListByAws(uri, "AFM", "f38e485a", "");
		System.err.println(api_for_aws_by_getMethod);
	}

	/**
	 * 调用直播云服务器提供的api 方法
	 * 
	 * @param postData
	 *            json 类型的字符串
	 * @return
	 */
	public static void addAWSUser() {
		String serviceCode = "AFM";
		String postData = "{\"function\": \"create_user\",\"params\": {\"service_code\": \"" + serviceCode + "\",\"user_name\": \"019\",\"password\": \"19pass\"}}";
		String key = "f38e485a";
		String api_for_aws = HttpUtil4AWS.api_for_aws(postData, serviceCode, key);
		System.err.println(api_for_aws);
	}

	/**
	 * 创建任务。 如果不需要可以不用
	 * 
	 * xml ： <root> <task> <input> <url>http://s3.cn-north-1.amazonaws.com.cn/developer.zhiboyun.com/v/won_640_360.flv</url> </input> <output tag="tr1"> <extension>flv</extension> <codec-v>h264</codec-v> <codec-a>copy</codec-a> <size>480x180</size> </output> </task> </root>
	 * 
	 * @param xml
	 */
	public static void testCreatTask(String xml) {
		String uri = "/api/20140928/create_task_with_conf";
		String api_for_aws_by_getMethod = HttpUtil4AWS.createTaskByAws(uri, "AFM", "f38e485a", xml);
		System.err.println(api_for_aws_by_getMethod);
	}

}
