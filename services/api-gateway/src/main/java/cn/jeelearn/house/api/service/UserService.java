package cn.jeelearn.house.api.service;

import cn.jeelearn.house.api.dao.UserDao;
import cn.jeelearn.house.api.model.User;
import cn.jeelearn.house.api.service.base.FileService;
import cn.jeelearn.house.api.utils.BeanHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/3
 * @Version:v1.0
 */
@Service
public class UserService {

    @Value("${domain.name}")
    private String domainName;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserDao userDao;


    public boolean isExist(String email) {
        return getUser(email) != null;
    }

    public boolean addAccount(User account) {
        if (account.getAvatarFile() != null){
            List<String> imags = fileService.getImgPaths(Lists.newArrayList(account.getAvatarFile()));
            account.setAvatar(imags.get(0));
        }
        account.setEnableUrl("http://"+domainName+"/accounts/verify");
        BeanHelper.setDefaultProp(account, User.class);
        userDao.addUser(account);
        return true;
    }

    public boolean enable(String key) {
        userDao.enable(key);
        return true;
    }

    /**
     * 校验用户名密码并返回用户对象
     */
    public User login(String username, String password) {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return null;
        }
        User user = new User();
        user.setEmail(username);
        user.setPasswd(password);
        try {
            user = userDao.login(user);
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    public void logout(String token) {
        userDao.logout(token);
    }

    public User updateUser(User user) {
        BeanHelper.onUpdate(user);
        return  userDao.updateUser(user);
    }


    private User getUser(String email) {
        User queryUser = new User();
        queryUser.setEmail(email);
        List<User> users =  getUserByQuery(queryUser);
        if (!CollectionUtils.isEmpty(users)){
            return users.get(0);
        }
        return null;
    }

    private List<User> getUserByQuery(User query) {
        List<User> users =  userDao.selectUsers(query);
        return users;
    }



}

