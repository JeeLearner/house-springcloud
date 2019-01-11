package cn.jeelearn.house.comment.dao;

import cn.jeelearn.house.comment.common.RestResponse;
import cn.jeelearn.house.comment.common.rest.GenericRest;
import cn.jeelearn.house.comment.common.rest.Rests;
import cn.jeelearn.house.comment.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/11
 * @Version:v1.0
 */
@Repository
public class UserDao {

    @Autowired
    private GenericRest rest;

    @Value("${user.service.name}")
    private String userServiceName;

    public User getUserDetail(long userId){
        return Rests.exc(() -> {
            String url = Rests.toUrl(userServiceName, "/user/info/" + userId);
            return rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {}).getBody();
        }).getResult();
    }

}

