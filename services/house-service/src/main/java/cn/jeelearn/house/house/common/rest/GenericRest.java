package cn.jeelearn.house.house.common.rest;

import org.apache.commons.lang3.tuple.Pair;
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
    private RestTemplate plainRestTemplate;

    private static final String directFlag = "direct://";

    public <T> ResponseEntity<T> post(String url, Object reqBody, ParameterizedTypeReference<T> responseType){
        Pair<RestTemplate, String> pair = getRestTemplate(url);
        return pair.getLeft().exchange(pair.getRight(), HttpMethod.POST, new HttpEntity(reqBody), responseType);
    }

    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType){
        Pair<RestTemplate, String> pair = getRestTemplate(url);
        return pair.getLeft().exchange(pair.getRight(),HttpMethod.GET, HttpEntity.EMPTY,responseType);
    }

    private Pair<RestTemplate, String> getRestTemplate(String url){
        RestTemplate rest = lbRestTemplate;
        if (url.contains(directFlag)){
            url.replace(directFlag, "");
            rest = plainRestTemplate;
        }
        return Pair.of(rest, url);
    }

    public RestTemplate getPlainRest(){
        return plainRestTemplate;
    }

    public RestTemplate getLbRest(){
        return lbRestTemplate;
    }

}

