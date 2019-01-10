package cn.jeelearn.house.api.service;

import cn.jeelearn.house.api.dao.UserDao;
import cn.jeelearn.house.api.model.Agency;
import cn.jeelearn.house.api.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/3
 * @Version:v1.0
 */
@Service
public class AgencyService {
    @Autowired
    private UserDao userDao;


    public List<Agency> getAllAgency(){
        return userDao.getAllAgency();
    }

    public User getAgentDetail(Long userId) {
        return userDao.getAgentById(userId);
    }
}

