package org.bigearpig.base.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.whois.WhoisClient;

import java.io.IOException;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
public class MyHttpUtil {
	
	private static Pattern pattern;
	private Matcher matcher;
	private static final String WHOIS_SERVER_PATTERN = "Whois Server:\\s(.*)";
	static {
		pattern = Pattern.compile(WHOIS_SERVER_PATTERN);
	}
	
	private String getWhoisServer(String whois) {
		 
		String result = "";
 
		matcher = pattern.matcher(whois);
 
		// get last whois server
		while (matcher.find()) {
			result = matcher.group(1);
		}
		return result;
	}

	private String queryWithWhoisServer(String domainName, String whoisServer) {
		 
		String result = "";
		WhoisClient whois = new WhoisClient();
		try {
 
			whois.connect(whoisServer);
			result = whois.query(domainName);
			whois.disconnect();
 
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		return result;
 
	}

	public String getWhois(String domainName) {
		 
		StringBuilder result = new StringBuilder("");
 
		WhoisClient whois = new WhoisClient();
		try {
 
		  whois.connect(WhoisClient.DEFAULT_HOST);
 
		  // whois =google.com
		  String whoisData1 = whois.query("=" + domainName);
 
		  // append first result
		  result.append(whoisData1);
		  whois.disconnect();
 
		  // get the google.com whois server - whois.markmonitor.com
		  String whoisServerUrl = getWhoisServer(whoisData1);
		  if (!whoisServerUrl.equals("")) {
 
			// whois -h whois.markmonitor.com google.com
			String whoisData2 = 
                            queryWithWhoisServer(domainName, whoisServerUrl);
 
			// append 2nd result
			result.append(whoisData2);
		  }
 
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		return result.toString();
 
	}

	
	
	public static String getUrlContent(String path) {
		HttpRequest request = HttpUtil.createGet(path);
		request.setFollowRedirects(true);
		request.setMaxRedirectCount(6);
		request.setConnectionTimeout(10000);
		request.setReadTimeout(60000);
		HttpResponse response = null;
		try {
			response = request.execute();
		} catch (Exception e) {
			log.error("获取url对应内容失败"+e.getLocalizedMessage(),e);
		}
		if (ObjectUtil.isNotNull(response)) {
			if (HttpStatus.HTTP_OK == response.getStatus()) {
				return response.body();
			} else {
				return "";
			}
		} else {
			return "";
		}

	}
	
	
}
