package cn.jeelearn.house.user.mapper;


import cn.jeelearn.house.user.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/11
 * @Version:v1.0
 */
@Mapper
public interface UserMapper {
    List<User> select(User user);
    User selectById(Long id);
    User selectByEmail(String email);

    int insert(User account);
    int update(User account);
    int delete(String email);
}

