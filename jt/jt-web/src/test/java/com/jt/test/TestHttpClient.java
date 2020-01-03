package com.jt.test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.util.HttpClientService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestHttpClient {

	/**
	 * 调用步骤:
	 * 	1.确定url的访问地址.
	 *  2.确定请求的方式类型 get/post
	 *  3.实例化httpClient对象.
	 *  4.发起请求. 获取响应response.
	 *  5.判断程序调用是否正确  200 302 400参数异常 406 参数转化异常
	 *  	404 500 502 504 访问超时...
	 *  6.获取返回值数据一般都是String.   JSON
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public void testGet() throws ClientProtocolException, IOException {
		String url = "https://www.baidu.com";
		HttpGet get = new HttpGet(url);
		HttpClient htClient = HttpClients.createDefault();
		HttpResponse response = htClient.execute(get);
		
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println(result);
		}
		
	}
	@Autowired
	private CloseableHttpClient htClient;
	@Autowired
	private RequestConfig requestConfig;
	
	@Test
	public void test02() throws ClientProtocolException, IOException {
		String url = "https://www.baidu.com";
		HttpGet get = new HttpGet(url);
		get.setConfig(requestConfig);
		
		HttpResponse response = htClient.execute(get);
		//获取状态码信息
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println(result);
		}
	}
	
	@Autowired
	private HttpClientService httpClient;
	
	@Test
	public void test03() {
		String url = "http://manage.jt.com/web/item/findItemDescById";
		Map<String, String> params = new HashMap<>();
		params.put("itemId", "1474391976");
		String result = httpClient.doGet(url, params);
		System.out.println(result);
	}
	
}
