package cn.jeelearn.house.house.service;

import cn.jeelearn.house.house.common.LimitOffset;
import cn.jeelearn.house.house.model.House;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/8
 * @Version:v1.0
 */
@Service
public class RecommendService {

    private static final String HOT_HOUSE_KEY = "_hot_house";

    @Autowired
    private HouseService houseService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 增加分数
     * @param id
     */
    public void increaseHot(long id) {
        redisTemplate.opsForZSet().incrementScore(HOT_HOUSE_KEY, ""+id, 1.0D);
        redisTemplate.opsForZSet().removeRange(HOT_HOUSE_KEY, 0, -11);
    }

    public List<House> getHotHouse(Integer size) {
        //  Set<String> idSet =  redisTemplate.opsForZSet().range(HOT_HOUSE_KEY, 0, -1);
        //bug修复，根据热度从高到底排序
        Set<String> idSet =  redisTemplate.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0, -1);
        List<Long> ids = idSet.stream().map(b -> Long.parseLong(b)).collect(Collectors.toList());
        House query = new House();
        query.setIds(ids);
        //return houseService.queryAndSetImg(query, LimitOffset.build(size, 0));

        //bug解决：返回时没有按照热度排序
        final List<Long> houseIds = ids;
        List<House> houses = houseService.queryAndSetImg(query, LimitOffset.build(size, 0));
        Ordering<House> houseSort = Ordering.natural().onResultOf(hs -> {
            return houseIds.indexOf(hs.getId());
        });
        return houseSort.sortedCopy(houses);
    }
}

