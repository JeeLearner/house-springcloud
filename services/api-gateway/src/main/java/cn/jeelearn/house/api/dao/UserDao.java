package cn.jeelearn.house.api.dao;

import cn.jeelearn.house.api.common.RestResponse;
import cn.jeelearn.house.api.config.rest.GenericRest;
import cn.jeelearn.house.api.config.rest.Rests;
import cn.jeelearn.house.api.model.Agency;
import cn.jeelearn.house.api.model.User;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/3
 * @Version:v1.0
 */
@Repository
public class UserDao {

    @Autowired
    private GenericRest rest;

    @Value("${user.service.name}")
    private String userServiceName;

    public List<User> selectUsers(User query) {
        String url = "http://" + userServiceName + "/user/list";
        ResponseEntity<RestResponse<List<User>>> resultEntity = rest.post(url, query, new ParameterizedTypeReference<RestResponse<List<User>>>() {});
        RestResponse<List<User>> restResponse = resultEntity.getBody();
        if (restResponse.getCode() == 0){
            return restResponse.getResult();
        } else {
            return Lists.newArrayList();
        }
    }

    public User addUser(User user) {
        String url = "http://" + userServiceName + "/user/add";
        ResponseEntity<RestResponse<User>> resultEntity = rest.post(url, user, new ParameterizedTypeReference<RestResponse<User>>() {});
        RestResponse<User> restResponse = resultEntity.getBody();
        if (restResponse.getCode() == 0){
            return restResponse.getResult();
        } else {
            throw new IllegalStateException("Can not add user");
        }
    }

    public boolean enable(String key) {
        String url = "http://" + userServiceName + "/user/enable?key=" + key;
        ResponseEntity<RestResponse<Object>> resultEntity = rest.get(url, new ParameterizedTypeReference<RestResponse<Object>>() {});
        RestResponse<Object> restResponse = resultEntity.getBody();
        if (restResponse.getCode() == 0){
            return true;
        } else{
            return false;
        }
    }

    public User login(User user) {
        String url = "http://" + userServiceName + "/user/login";
        ResponseEntity<RestResponse<User>> responseEntity =  rest.post(url, user, new ParameterizedTypeReference<RestResponse<User>>() {});
        RestResponse<User> response = responseEntity.getBody();
        if (response.getCode() == 0) {
            return response.getResult();
        }else {
            throw new IllegalStateException("Can not login user");
        }
    }

    public void logout(String token) {
        String url = "http://" + userServiceName + "/user/logout?token=" + token;
        rest.get(url, new ParameterizedTypeReference<RestResponse<Object>>() {});
    }

    public User getUserByToken(String token) {
        String url = "http://" + userServiceName + "/user/auth?token=" + token;
        ResponseEntity<RestResponse<User>> responseEntity =  rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {});
        RestResponse<User> response = responseEntity.getBody();
        if (response.getCode() == 0) {
            return response.getResult();
        }else {
            throw new IllegalStateException("Can not auth user");
        }
    }

    public User updateUser(User user) {
        return Rests.exc(() -> {
            String url = Rests.toUrl(userServiceName, "/user/update");
            ResponseEntity<RestResponse<User>> responseEntity = rest.post(url, user, new ParameterizedTypeReference<RestResponse<User>>() {});
            return responseEntity.getBody();
        }).getResult();
    }



    //-------------------------agency--------------------------
    public List<Agency> getAllAgency() {
        return Rests.exc(() ->{
            String url = Rests.toUrl(userServiceName, "/agency/list");
            ResponseEntity<RestResponse<List<Agency>>> responseEntity =
                    rest.get(url, new ParameterizedTypeReference<RestResponse<List<Agency>>>() {});
            return responseEntity.getBody();
        }).getResult();
    }

    public User getAgentById(Long userId) {
        return Rests.exc(() ->{
            String url = Rests.toUrl(userServiceName, "/agency/agentDetail?id=" +userId);
            ResponseEntity<RestResponse<User>> responseEntity =
                    rest.get(url, new ParameterizedTypeReference<RestResponse<User>>() {});
            return responseEntity.getBody();
        }).getResult();
    }
}

