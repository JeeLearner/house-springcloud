package cn.jeelearn.house.api.servie;

import cn.jeelearn.house.api.common.RestResponse;
import cn.jeelearn.house.api.config.GenericRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
@Repository
public class DemoService {

    @Autowired
    private GenericRest rest;

    public String getUsername(Long id){
        String url = "http://user-service/getUsername?id=" + id;
        //这里返回的是整个结果，不是我们要的  {"code":0,"msg":"OK","result":"test-username"}
        //String body = rest.get(url, new ParameterizedTypeReference<String>() {}).getBody();
        //ParameterizedTypeReference需要深度泛型
        RestResponse<String> response = rest.get(url, new ParameterizedTypeReference<RestResponse<String>>() {}).getBody();
        return response.getResult();
    }

}

