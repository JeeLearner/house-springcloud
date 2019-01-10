package cn.jeelearn.house.house.service;

import cn.jeelearn.house.house.common.BeanHelper;
import cn.jeelearn.house.house.common.HouseUserType;
import cn.jeelearn.house.house.common.LimitOffset;
import cn.jeelearn.house.house.dao.UserDao;
import cn.jeelearn.house.house.mapper.CityMapper;
import cn.jeelearn.house.house.mapper.HouseMapper;
import cn.jeelearn.house.house.model.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/8
 * @Version:v1.0
 */
@Service
public class HouseService {

    @Autowired
    private HouseMapper houseMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserDao userDao;

    @Value("${file.prefix}")
    private String imgPrefix;


    public List<House> queryAndSetImg(House query, LimitOffset pageParams) {
        List<House> houses = houseMapper.selectHouse(query, pageParams);
        houses.forEach(h -> {
            h.setFirstImg(imgPrefix + h.getFirstImg());
            h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
            h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
        });
        return houses;
    }


    public Pair<List<House>,Long> queryHouse(House query, LimitOffset pageParams) {
        List<House> houses = houseMapper.selectHouse(query, pageParams);
        House houseQuery = query;
        if (StringUtils.isNotBlank(query.getName())){
            Community community = new Community();
            community.setName(query.getName());
            List<Community> communities = houseMapper.selectCommunity(community);
            if (!CollectionUtils.isEmpty(communities)){
                houseQuery = new House();
                houseQuery.setCommunityId(communities.get(0).getId());
            }
        }
        houses = queryAndSetImg(houseQuery, pageParams);
        Long count = houseMapper.selectHouseCount(query);
        return ImmutablePair.of(houses, count);
    }


    public House queryOneHouse(long id) {
        House query = new House();
        query.setId(id);
        List<House> houses = queryAndSetImg(query, LimitOffset.build(1, 0));
        if (!houses.isEmpty()) {
            return houses.get(0);
        }
        return null;
    }

    /**
     * 向经纪人发送留言通知
     * @param userMsg
     */
    public void addUserMsg(UserMsg userMsg) {
        BeanHelper.onInsert(userMsg);
        BeanHelper.setDefaultProp(userMsg, UserMsg.class);
        houseMapper.insertUserMsg(userMsg);
        User user = userDao.getAgentDetail(userMsg.getAgentId());
        mailService.sendSimpleMail("来自用户" + userMsg.getEmail(), userMsg.getMsg(), user.getEmail());
    }

    public void updateRating(Long id, Double rating) {
        House house = queryOneHouse(id);
        Double oldRating = house.getRating();
        Double newRating = oldRating.equals(0D) ? rating : Math.min(Math.round(oldRating + rating)/2 , 5);
        House updateHouse =  new House();
        updateHouse.setId(id);
        updateHouse.setRating(newRating);
        houseMapper.updateHouse(updateHouse);
    }

    public List<Community> getAllCommunitys() {
        Community community = new Community();
        return houseMapper.selectCommunity(community);
    }

    public List<City> getAllCitys() {
        City city = new City();
        return cityMapper.selectCitys(city);
    }

    /**
     * 添加房屋图片
     * 添加户型图片
     * 插入房产信息
     * 绑定用户到房产的关系
     * @param house
     * @param userId
     */
    @Transactional(rollbackFor=Exception.class)
    public void addHouse(House house, Long userId) {
        BeanHelper.setDefaultProp(house, House.class);
        BeanHelper.onInsert(house);
        houseMapper.insert(house);
        bindUser2House(house.getId(),userId,HouseUserType.SALE);
    }

    /**
     * 注意这里逻辑做了修改:当售卖时只能将房产下架，不能解绑用户关系
     *                   当收藏时可以解除用户收藏房产这一关系
     * 解绑操作1.
     * @param houseId
     * @param userId
     * @param houseUserType
     */
    public void unbindUser2Houser(Long houseId, Long userId, HouseUserType houseUserType) {
        if (houseUserType.equals(HouseUserType.SALE)) {
            houseMapper.downHouse(houseId);
        }else {
            houseMapper.deleteHouseUser(houseId, userId, houseUserType.value);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void bindUser2House(Long houseId, Long userId, HouseUserType houseUserType) {
        HouseUser existHouseUser = houseMapper.selectHouseUser(userId,houseId, houseUserType.value);
        if (existHouseUser != null) {
            return;
        }
        HouseUser houseUser  = new HouseUser();
        houseUser.setHouseId(houseId);
        houseUser.setUserId(userId);
        houseUser.setType(houseUserType.value);
        BeanHelper.setDefaultProp(houseUser, HouseUser.class);
        BeanHelper.onInsert(houseUser);
        houseMapper.insertHouseUser(houseUser);
    }

    public List<House> getLastest() {
        House query = new House();
        query.setSort("create_time");
        return queryAndSetImg(query, LimitOffset.build(8, 0));
    }
}

