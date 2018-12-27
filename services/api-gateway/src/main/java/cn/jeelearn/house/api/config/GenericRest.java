package cn.jeelearn.house.api.config;

import cn.jeelearn.house.api.common.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: 服务调用的封装
 *      既支持直连又支持服务发现的调用
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
@Service
public class GenericRest {

    @Autowired
    private RestTemplate lbRestTemplate;

    @Autowired
    private RestTemplate directRestTemplate;

    private static final String directFlag = "direct://";

    public <T> ResponseEntity<T> post(String url, Object reqBody, ParameterizedTypeReference<T> responseType){
        RestTemplate template = getRestTemplate(url);
        return template.exchange(url, HttpMethod.POST, new HttpEntity(reqBody), responseType);
    }

    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType){
        RestTemplate template = getRestTemplate(url);
        return template.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType);
    }

    private RestTemplate getRestTemplate(String url){
        if (url.contains(directFlag)){
            url.replace(directFlag, "");
            return directRestTemplate;
        } else {
            return lbRestTemplate;
        }
    }

}

