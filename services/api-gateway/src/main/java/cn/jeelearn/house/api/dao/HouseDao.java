package cn.jeelearn.house.api.dao;

import cn.jeelearn.house.api.common.ListResponse;
import cn.jeelearn.house.api.common.RestResponse;
import cn.jeelearn.house.api.config.rest.GenericRest;
import cn.jeelearn.house.api.config.rest.Rests;
import cn.jeelearn.house.api.model.*;
import cn.jeelearn.house.api.model.req.HouseQueryReq;
import cn.jeelearn.house.api.model.req.HouseUserReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/10
 * @Version:v1.0
 */
@Repository
public class HouseDao {

    @Autowired
    private GenericRest rest;

    @Value("${house.service.name}")
    private String houseServiceName;

    public ListResponse<House> queryHouses(House query, Integer limit, Integer offset){
        return Rests.exc(() -> {
            HouseQueryReq req = new HouseQueryReq();
            req.setLimit(limit);
            req.setOffset(offset);
            req.setQuery(query);
            String url = Rests.toUrl(houseServiceName, "/house/list");
            return rest.post(url, req, new ParameterizedTypeReference<RestResponse<ListResponse<House>>>() {}).getBody();
        }).getResult();
    }

    public House getOneHouse(long id) {
        return Rests.exc(() ->{
            String url = Rests.toUrl(houseServiceName, "/house/detail?id=" + id);
            ResponseEntity<RestResponse<House>> responseEntity =  rest.get(url, new ParameterizedTypeReference<RestResponse<House>>() {});
            return responseEntity.getBody();
        }).getResult();
    }

    public List<House> getHotHouse(Integer recomSize) {
        return Rests.exc(() ->{
            String url = Rests.toUrl(houseServiceName, "/house/hot" + "?size="+recomSize);
            ResponseEntity<RestResponse<List<House>>> responseEntity =  rest.get(url, new ParameterizedTypeReference<RestResponse<List<House>>>() {});
            return responseEntity.getBody();
        }).getResult();
    }

    public List<House> getLastest() {
        RestResponse<List<House>> resp = Rests.exc(() -> {

            String url = Rests.toUrl(houseServiceName, "/house/lastest");
            ResponseEntity<RestResponse<List<House>>> responseEntity = rest.get(url,new ParameterizedTypeReference<RestResponse<List<House>>>() {});
            return responseEntity.getBody();
        });
        return resp.getResult();
    }

    public List<City> getAllCitys() {
        RestResponse<List<City>> resp = Rests.exc(() ->{
            String url = Rests.toUrl(houseServiceName, "/house/allCitys" );
            ResponseEntity<RestResponse<List<City>>> responseEntity = rest.get(url,new ParameterizedTypeReference<RestResponse<List<City>>>() {});
            return responseEntity.getBody();
        });
        return resp.getResult();
    }

    public List<Community> getAllCommunitys() {
        RestResponse<List<Community>> resp = Rests.exc(() -> {
            String url = Rests.toUrl(houseServiceName, "/house/allCommunitys" );
            ResponseEntity<RestResponse<List<Community>>> responseEntity = rest.get(url,new ParameterizedTypeReference<RestResponse<List<Community>>>() {});
            return responseEntity.getBody();
        });
        return resp.getResult();
    }

    public void addHouse(House house) {
        Rests.exc(()  ->{
            String url = Rests.toUrl(houseServiceName, "/house/add" );
            ResponseEntity<RestResponse<Object>> responseEntity = rest.post(url,house,new ParameterizedTypeReference<RestResponse<Object>>() {});
            return responseEntity.getBody();
        });
    }

    public void rating(Long id, Double rating) {
        Rests.exc(() ->{
            String url = Rests.toUrl(houseServiceName, "/house/rating?id=" + id + "&rating=" + rating );
            ResponseEntity<RestResponse<Object>> responseEntity = rest.get(url,new ParameterizedTypeReference<RestResponse<Object>>() {});
            return responseEntity.getBody();
        });
    }

    public void addUserMsg(UserMsg userMsg) {
        Rests.exc(() ->{
            String url = Rests.toUrl(houseServiceName, "/house/addUserMsg" );
            ResponseEntity<RestResponse<Object>> responseEntity = rest.post(url,userMsg,new ParameterizedTypeReference<RestResponse<Object>>() {});
            return responseEntity.getBody();
        });
    }

    public void bindUser2House(Long houseId, Long userId, boolean bookmark) {
        HouseUserReq req = new HouseUserReq();
        req.setUnBind(false);
        req.setBindType(HouseUserType.BOOKMARK.value);
        req.setUserId(userId);
        req.setHouseId(houseId);
        bindOrInBind(req);
    }

    public void unbindUser2House(Long houseId, Long userId, boolean book) {
        HouseUserReq req = new HouseUserReq();
        req.setUnBind(true);
        if (book) {
            req.setBindType(HouseUserType.BOOKMARK.value);
        }else {
            req.setBindType(HouseUserType.SALE.value);
        }
        req.setUserId(userId);
        req.setHouseId(houseId);
        bindOrInBind(req);
    }

    private void bindOrInBind(HouseUserReq req) {
        Rests.exc(() ->{
            String url = Rests.toUrl(houseServiceName, "/house/bind");
            ResponseEntity<RestResponse<Object>> responseEntity = rest.post(url,req,new ParameterizedTypeReference<RestResponse<Object>>() {});
            return responseEntity.getBody();
        });
    }

}

