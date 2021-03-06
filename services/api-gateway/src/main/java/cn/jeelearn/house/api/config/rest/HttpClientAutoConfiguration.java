package cn.jeelearn.house.api.config.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/26
 * @Version:v1.0
 */
@Configuration
@ConditionalOnClass({HttpClient.class})
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfiguration {

    private final HttpClientProperties properties;

    public HttpClientAutoConfiguration(HttpClientProperties properties){
        this.properties = properties;
    }

    //logbook
    @Autowired
    private LogbookHttpRequestInterceptor logbookHttpRequestInterceptor;
    @Autowired
    private LogbookHttpResponseInterceptor logbookHttpResponseInterceptor;

    /**
     * httpClient bean 的定义
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient(){
        //构建RequestConfig
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(properties.getConnectTimeOut())
                .setSocketTimeout(properties.getSocketTimeOut())
                .build();
        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent(properties.getAgent())
                .setMaxConnPerRoute(properties.getMaxConnPerRoute())
                .setMaxConnTotal(properties.getMaxConnTotaol())
                //设置连接重用策略，默认长连接
                //NoConnectionReuseStrategy的话说明每次都是新连接
                //.setConnectionReuseStrategy(new NoConnectionReuseStrategy())
                //添加logbook拦截器配置
                .addInterceptorFirst(logbookHttpRequestInterceptor)
                .addInterceptorFirst(logbookHttpResponseInterceptor)
                .build();
        return httpClient;
    }
}

