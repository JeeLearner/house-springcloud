package cn.jeelearn.learn.fastjson.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/26
 * @Version:v1.0
 */
public class BaseData {

    public static Group getGroup(){
        Group group = new Group();
        group.setId(0L);
        group.setName("admin");

        User guestUser = new User();
        guestUser.setId(2L);
        guestUser.setName("guest");

        User rootUser = new User();
        rootUser.setId(3L);
        rootUser.setName("root");

        group.addUser(guestUser);
        group.addUser(rootUser);
        return group;
    }

    public static List<User> getUsers(){
        List<User> users = new ArrayList<>();
        User guestUser = new User();
        guestUser.setId(2L);
        guestUser.setName("guest");
        User rootUser = new User();
        rootUser.setId(3L);
        rootUser.setName("root");
        users.add(guestUser);
        users.add(rootUser);
        return users;
    }

    public static User getUserWithDate(){
        User guestUser = new User();
        guestUser.setId(2L);
        guestUser.setName("guest");
        guestUser.setCreateDate(new Date());
        return guestUser;
    }
}

