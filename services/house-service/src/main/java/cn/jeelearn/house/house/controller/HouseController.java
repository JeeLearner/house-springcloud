package cn.jeelearn.house.house.controller;

import cn.jeelearn.house.house.common.*;
import cn.jeelearn.house.house.model.*;
import cn.jeelearn.house.house.model.req.HouseQueryReq;
import cn.jeelearn.house.house.model.req.HouseUserReq;
import cn.jeelearn.house.house.service.HouseService;
import cn.jeelearn.house.house.service.RecommendService;
import com.google.common.base.Objects;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/8
 * @Version:v1.0
 */
@RequestMapping("house")
@RestController
public class HouseController {

    @Autowired
    private HouseService houseService;
    @Autowired
    private RecommendService recommendService;

    /**
     * 房源列表  带分页
     * @param req
     * @return
     */
    @PostMapping("list")
    public RestResponse<ListResponse<House>> houseList(@RequestBody HouseQueryReq req){
        Integer limit  = req.getLimit();
        Integer offset = req.getOffset();
        House   query  = req.getQuery();
        Pair<List<House>, Long> pair = houseService.queryHouse(query, LimitOffset.build(limit, offset));
        return RestResponse.success(ListResponse.build(pair.getKey(), pair.getValue()));
    }

    @GetMapping("detail")
    public RestResponse<House> houseDetail(long id){
        House house = houseService.queryOneHouse(id);
        recommendService.increaseHot(id);
        return RestResponse.success(house);
    }

    /**
     * 用户留言
     * @param userMsg
     * @return
     */
    @PostMapping("addUserMsg")
    public RestResponse<Object> houseMsg(@RequestBody UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return RestResponse.success();
    }

    /**
     * 用户评分
     * @param rating
     * @param id
     * @return
     */
    @ResponseBody
    @GetMapping("rating")
    public RestResponse<Object> houseRate(Double rating,Long id){
        houseService.updateRating(id,rating);
        return RestResponse.success();
    }

    @GetMapping("allCommunitys")
    public RestResponse<List<Community>> addCommunitys(){
        List<Community> list = houseService.getAllCommunitys();
        return RestResponse.success(list);
    }

    @GetMapping("allCitys")
    public RestResponse<List<City>> allCitys(){
        List<City> list = houseService.getAllCitys();
        return RestResponse.success(list);
    }

    /**
     * 房产新增
     * @param house
     * @return
     */
    @PostMapping("add")
    public RestResponse<Object> doAdd(@RequestBody House house){
        house.setState(CommonConstants.HOUSE_STATE_UP);
        if (house.getUserId() == null) {
            return RestResponse.error(RestCode.ILLEGAL_PARAMS);
        }
        houseService.addHouse(house,house.getUserId());
        return RestResponse.success();
    }

    @PostMapping("bind")
    public RestResponse<Object> delsale(@RequestBody HouseUserReq req){
        Integer bindType = req.getBindType();
        HouseUserType houseUserType = Objects.equal(bindType, 1) ? HouseUserType.SALE : HouseUserType.BOOKMARK;
        if (req.isUnBind()) {
            houseService.unbindUser2Houser(req.getHouseId(),req.getUserId(),houseUserType);
        }else {
            houseService.bindUser2House(req.getHouseId(), req.getUserId(), houseUserType);
        }
        return RestResponse.success();
    }

    @GetMapping("hot")
    public RestResponse<List<House>> getHotHouse(Integer size){
        List<House> list =   recommendService.getHotHouse(size);
        return RestResponse.success(list);
    }

    @GetMapping("lastest")
    public RestResponse<List<House>> getLastest(){
        return RestResponse.success(houseService.getLastest());
    }
}

