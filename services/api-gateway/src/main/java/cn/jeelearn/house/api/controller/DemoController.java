package cn.jeelearn.house.api.controller;

import cn.jeelearn.house.api.common.RestResponse;
import cn.jeelearn.house.api.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("test/getusername")
    public RestResponse<String> getUsername(Long id){
        return RestResponse.success(demoService.getUsername(id));
    }
}

