package cn.jeelearn.house.user.service;

import cn.jeelearn.house.user.mapper.UserMapper;
import cn.jeelearn.house.user.model.User;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/28
 * @Version:v1.0
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${file.prefix}")
    private String imgPrefix;


    public List<User> getUserByQuery(User user){
        List<User> users = userMapper.select(user);
        users.forEach(u -> {
            u.setAvatar(imgPrefix + u.getAvatar());
        });
        return users;
    }

    /**
     * 1.首先通过缓存获取
     * 2.不存在将从通过数据库获取用户对象
     *      并将用户对象写入缓存，设置缓存时间5分钟
     * 3.返回对象
     *
     * @param id
     * @return
     */
    public User getUserById(Long id) {
        String key = "user:" + id;
        String json = redisTemplate.opsForValue().get(key);
        User user = null;
        if (Strings.isNullOrEmpty(json)){
            user = userMapper.selectById(id);
            user.setAvatar(imgPrefix + user.getAvatar());

            String userStr = JSON.toJSONString(user);
            redisTemplate.opsForValue().set(key, userStr);
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        } else {
            user = JSON.parseObject(json, User.class);
        }
        return user;
    }
}

