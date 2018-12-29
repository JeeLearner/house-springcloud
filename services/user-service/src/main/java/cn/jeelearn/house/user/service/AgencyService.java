package cn.jeelearn.house.user.service;

import cn.jeelearn.house.user.common.PageParams;
import cn.jeelearn.house.user.mapper.AgencyMapper;
import cn.jeelearn.house.user.model.Agency;
import cn.jeelearn.house.user.model.User;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/29
 * @Version:v1.0
 */
@Service
public class AgencyService {

    @Autowired
    private AgencyMapper agencyMapper;

    @Value("${file.prefix}")
    private String imgPrefix;

    public Pair<List<User>,Long> getAllAgent(PageParams pageParams) {
        List<User> agents =  agencyMapper.selectAgent(new User(),pageParams);
        setImg(agents);
        Long count = agencyMapper.selectAgentCount(new User());
        return ImmutablePair.of(agents, count);
    }




    public void setImg(List<User> users){
        users.forEach(u -> {
            u.setAvatar(imgPrefix + u.getAvatar());
        });
    }

    public User getAgentDetail(Long id) {
        User user = new User();
        user.setId(id);
        user.setType(2);
        List<User> list = agencyMapper.selectAgent(user, new PageParams(1, 1));
        if (!CollectionUtils.isEmpty(list)){
            setImg(list);
            User agent = list.get(0);
            //将经纪人关联的经纪机构也一并查询出来
            Agency agency = new Agency();
            agency.setId(agent.getAgencyId().intValue());
            List<Agency> agencyList = agencyMapper.select(agency);
            if (!CollectionUtils.isEmpty(agencyList)){
                agent.setAgencyName(agencyList.get(0).getName());
            }
            return agent;
        }
        return null;
    }
}

