package org.bigearpig.sys.module.es.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

@Slf4j
@Configuration
public class EsConfig {
    @Value("${zfj.es.host}")
    private String host;
    @Value("${zfj.es.port}")
    private Integer port;
    @Value("${zfj.es.username:elastic}")
    private String username;
    @Value("${zfj.es.password:TONG1!cheng}")
    private String password;
    
	@Bean
	public RestHighLevelClient getRestHighLevelClient() {
		HttpHost httpHost = new HttpHost(host, port);
		// 配置认证信息 
	    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		RestClientBuilder restClientBuilder = RestClient.builder(httpHost).setDefaultHeaders(new BasicHeader[]{
		        new BasicHeader("Authorization", "Basic " + 
		                Base64.getEncoder().encodeToString((username  + ":" + password).getBytes()))
		        });
		RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);
		log.info("RestHighLevelClient启动");
		return client;
	}
}
