package cn.jeelearn.house.house.dao;

import cn.jeelearn.house.house.common.RestResponse;
import cn.jeelearn.house.house.common.rest.GenericRest;
import cn.jeelearn.house.house.common.rest.Rests;
import cn.jeelearn.house.house.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/8
 * @Version:v1.0
 */
@Repository
public class UserDao {

    @Value("${user.service.name}")
    private String userServiceName;

    @Autowired
    private GenericRest rest;

    public User getAgentDetail(Long agentId) {
        return Rests.exc(() -> {
            String url = Rests.toUrl(userServiceName, "/agency/agentDetail" + "?id=" + agentId);
            ResponseEntity<RestResponse<User>> responseEntity = rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {});
            return responseEntity.getBody();
        }).getResult();
    }
}

