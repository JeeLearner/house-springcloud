package cn.jeelearn.house.user.service;

import cn.jeelearn.house.user.exception.UserException;
import cn.jeelearn.house.user.exception.UserException.Type;
import cn.jeelearn.house.user.mapper.UserMapper;
import cn.jeelearn.house.user.model.User;
import cn.jeelearn.house.user.utils.BeanHelper;
import cn.jeelearn.house.user.utils.HashUtils;
import cn.jeelearn.house.user.utils.JwtHelper;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.List;
import java.util.Map;
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
    private MailService mailService;

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

    public boolean addAccount(User user, String enableUrl){
        user.setPasswd(HashUtils.encryPassword(user.getPasswd()));
        BeanHelper.onInsert(user);
        userMapper.insert(user);
        //发送注册邮件
        registerNotify(user.getEmail(),enableUrl);
        return true;
    }

    /**
     * 发送注册激活邮件
     * @param email
     * @param enableUrl
     */
    private void registerNotify(String email, String enableUrl) {
        String randomKey = HashUtils.hashString(email) + RandomStringUtils.randomAlphabetic(10);
        redisTemplate.opsForValue().set(randomKey, email);
        redisTemplate.expire(email, 1, TimeUnit.HOURS);
        String content = enableUrl +"?key="+  randomKey;
        mailService.sendSimpleMail("房产平台激活邮件", content, email);
    }

    public boolean enable(String key) {
        String email = redisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(email)){
            throw new UserException(UserException.Type.USER_NOT_FOUND, "无效的key");
        }
        User user = new User();
        user.setEmail(email);
        user.setEnable(1);
        userMapper.update(user);
        return true;
    }

    /**
     * 校验用户名密码、生成token并返回用户对象
     * @param email
     * @param passwd
     * @return
     */
    public User login(String email, String passwd) {
        if (StringUtils.isBlank(email) || StringUtils.isBlank(passwd)) {
            throw new UserException(Type.USER_AUTH_FAIL,"User Auth Fail");
        }
        User user = new User();
        user.setEmail(email);
        user.setPasswd(HashUtils.encryPassword(passwd));
        user.setEnable(1);
        List<User> dbUsers = userMapper.select(user);
        if (!CollectionUtils.isEmpty(dbUsers)){
            User retUser = dbUsers.get(0);
            //设置token
            onLogin(retUser);
            return retUser;
        }
        throw new UserException(Type.USER_AUTH_FAIL,"User Auth Fail");
    }

    /**
     * 鉴权 通过token查user
     * @param token
     * @return
     */
    public User getLoginedUserByToken(String token) {
        Map<String, String> map = null;
        try {
            map = JwtHelper.verifyToken(token);
        } catch (Exception e) {
            throw new UserException(Type.USER_NOT_LOGIN,"User not login");
        }
        String email = map.get("email");
        Long expire = redisTemplate.getExpire(email);
        //登出后expire为-1
        if(expire > 0L){
            renewToken(token, email);
            User user = userMapper.selectByEmail(email);
            return user;
        }
        throw new UserException(Type.USER_NOT_LOGIN,"user not login");
    }

    /**
     * 登出  注销
     * @param token
     */
    public void invalidate(String token) {
        Map<String, String> map = JwtHelper.verifyToken(token);
        //delete后过期时间变为-1
        redisTemplate.delete(map.get("email"));
    }

    /**
     * 设置token
     */
    private void onLogin(User user) {
        String token =  JwtHelper.genToken(ImmutableMap.of("email", user.getEmail(), "name", user.getName(),"ts",Instant.now().getEpochSecond()+""));
        //重置token过期时间
        renewToken(token, user.getEmail());
        user.setToken(token);
    }

    /**
     * 重置过期时间
     */
    private String renewToken(String token, String email) {
        redisTemplate.opsForValue().set(email, token);
        redisTemplate.expire(email, 30, TimeUnit.MINUTES);
        return token;
    }

}

