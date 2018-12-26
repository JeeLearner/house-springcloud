package cn.jeelearn.house.api.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter4;
import org.apache.http.client.HttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/26
 * @Version:v1.0
 */
@Configuration
public class RestAutoConfig {

    public static class RestTemplateConfig{

        /**
         * spring 对restTemplate bean进行定制，
         *     加入loadbalance拦截器进行ip:port的替换
         * @return
         */
        @Bean
        @LoadBalanced
        RestTemplate lbRestTemplate(HttpClient httpClient){
            RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("utf-8")));
            template.getMessageConverters().add(1, new FastJsonHttpMessageConverter4());
            return template;
        }

        /**
         * 非定制化的restTemplate bean
         * @return
         */
        @Bean
        RestTemplate directRestTemplate(HttpClient httpClient){
            RestTemplate template = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            template.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("utf-8")));
            template.getMessageConverters().add(1, new FastJsonHttpMessageConverter4());
            return template;
        }
    }
}

