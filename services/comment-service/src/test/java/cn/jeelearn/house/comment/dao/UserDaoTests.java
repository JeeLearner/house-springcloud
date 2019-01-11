package cn.jeelearn.house.comment.dao;

import cn.jeelearn.house.comment.model.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/11
 * @Version:v1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserDaoTests {
    @Autowired
    private UserDao userDao;

    @Test
    public void test(){
        User user = userDao.getUserDetail(7);
        System.out.println(JSON.toJSONString(user));
    }
}

