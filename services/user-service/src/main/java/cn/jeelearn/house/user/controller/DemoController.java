package cn.jeelearn.house.user.controller;

import cn.jeelearn.house.user.common.RestResponse;
import cn.jeelearn.house.user.exception.IllegalParamsException;
import cn.jeelearn.house.user.exception.IllegalParamsException.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/27
 * @Version:v1.0
 */
@RestController
public class DemoController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoController.class);

    @Value("${server.port}")
    private int port;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("getUsername")
    public RestResponse<String> getUsername(Long id){
        LOGGER.info("Incoming Request...,my server port is " + port);
        if (id == null){
            //throw new IllegalParamsException(Type.WRONG_PAGE_NUM, "错误分页");
            throw  new NullPointerException();
        }
        return RestResponse.success("test-username,port=" + port);
    }

    @GetMapping("test/redis")
    public RestResponse<String> testRedis(Long id){
        LOGGER.info("Incoming Request...,my server port is " + port);
       redisTemplate.opsForValue().set("key1", "value1");
       LOGGER.info("Test Redis :" + redisTemplate.opsForValue().get("key1"));
        return RestResponse.success("test-username,port=" + port);
    }
}

