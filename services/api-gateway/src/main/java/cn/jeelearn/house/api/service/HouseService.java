package cn.jeelearn.house.api.service;

import cn.jeelearn.house.api.common.ListResponse;
import cn.jeelearn.house.api.common.page.PageData;
import cn.jeelearn.house.api.common.page.PageParams;
import cn.jeelearn.house.api.dao.HouseDao;
import cn.jeelearn.house.api.model.House;
import cn.jeelearn.house.api.model.User;
import cn.jeelearn.house.api.model.UserMsg;
import cn.jeelearn.house.api.service.base.FileService;
import cn.jeelearn.house.api.utils.BeanHelper;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/9
 * @Version:v1.0
 */
@Service
public class HouseService {

    @Autowired
    private HouseDao houseDao;
    @Autowired
    private FileService fileService;


    public PageData<House> queryHouse(House query, PageParams build) {
        ListResponse<House> houseListResponse = houseDao.queryHouses(query, build.getLimit(), build.getOffset());
        return PageData.buildPage(houseListResponse.getList(), houseListResponse.getCount(), build.getPageSize(), build.getPageNum());
    }

    public House selectOneHouse(long id) {
        return houseDao.getOneHouse(id);
    }


    public List<House> getHotHouse(Integer recomSize) {
        return houseDao.getHotHouse(recomSize);
    }

    public Object getAllCitys() {
        return houseDao.getAllCitys();
    }

    public Object getAllCommunitys() {
        return houseDao.getAllCommunitys();
    }

    public void addUserMsg(UserMsg userMsg) {
        houseDao.addUserMsg(userMsg);
    }

    public void addHouse(House house, User user) {
        if (house.getHouseFiles() != null && !house.getHouseFiles().isEmpty()) {
            List<MultipartFile> files = house.getHouseFiles();
            List<String> imgPaths = fileService.getImgPaths(files);
            String imgs = Joiner.on(",").join(imgPaths);
            house.setImages(imgs);
        }
        if (house.getFloorPlanFiles() != null && !house.getFloorPlanFiles().isEmpty()) {
            List<MultipartFile> files = house.getFloorPlanFiles();
            String floorPlans = Joiner.on(",").join(fileService.getImgPaths(files));
            house.setFloorPlan(floorPlans);
        }
        BeanHelper.setDefaultProp(house, House.class);
        BeanHelper.onInsert(house);
        house.setUserId(user.getId());
        houseDao.addHouse(house);
    }

    public void bindUser2House(Long houseId, Long userId, boolean bookmark) {
        houseDao.bindUser2House(houseId,userId,bookmark);
    }

    public void unbindUser2House(Long id, Long id2, boolean b) {
        houseDao.unbindUser2House(id,id2,b);
    }

    public void updateRating(Long id, Double rating) {
        houseDao.rating(id, rating);
    }
}

