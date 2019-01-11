package cn.jeelearn.house.api.controller;

import cn.jeelearn.house.api.common.CommonConstants;
import cn.jeelearn.house.api.common.ResultMsg;
import cn.jeelearn.house.api.common.page.PageData;
import cn.jeelearn.house.api.common.page.PageParams;
import cn.jeelearn.house.api.inteceptor.UserContext;
import cn.jeelearn.house.api.model.Comment;
import cn.jeelearn.house.api.model.House;
import cn.jeelearn.house.api.model.User;
import cn.jeelearn.house.api.model.UserMsg;
import cn.jeelearn.house.api.service.AgencyService;
import cn.jeelearn.house.api.service.CommentService;
import cn.jeelearn.house.api.service.HouseService;
import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
@Controller
@RequestMapping("/house")
public class HouseController {

    @Autowired
    private HouseService houseService;
    @Autowired
    private AgencyService agencyService;
    @Autowired
	private CommentService commentService;

    /**
     * 1.实现分页
     * 2.支持小区搜索、类型搜索
     * 3.支持排序
     * 4.支持展示图片、价格、标题、地址等信息
     * @auther: lyd
     * @date: 2018/12/16
     */
    @RequestMapping("/list")
    public String list(Integer pageSize, Integer pageNum, House query, ModelMap map){
        //房源列表
        PageData<House> housePageData = houseService.queryHouse(query, PageParams.build(pageSize, pageNum));
        //热门房源
        List<House> hotHouses =  houseService.getHotHouse(CommonConstants.RECOM_SIZE);
        map.put("recomHouses", hotHouses);
        map.put("ps", housePageData);
        map.put("vo", query);
        return "house/listing";
    }

    /**
     * 查看房屋详情
     * @auther: lyd
     * @date: 2018/12/17
     */
    @RequestMapping("/detail")
    public String detail(long id, ModelMap map){
        //房屋详情
        House house = houseService.selectOneHouse(id);
        //评论列表
        List<Comment> comments = commentService.getHouseComments(id);
		//热门房产
		List<House> rcHouses =  houseService.getHotHouse(CommonConstants.RECOM_SIZE);
		//房屋关联的经纪人及机构信息
		if (house.getUserId() != null){
			if (!Objects.equal(0L, house.getUserId())) {
				map.put("agent", agencyService.getAgentDetail(house.getUserId()));
			}
		}
        map.put("recomHouses", rcHouses);
        map.put("house", house);
        map.put("commentList", comments);
        return "/house/detail";
    }

    /**
     * 留言
     * @auther: lyd
     * @date: 2018/12/18
     */
    @RequestMapping("/leaveMsg")
    public String houseMsg(UserMsg userMsg){
        houseService.addUserMsg(userMsg);
        return "redirect:/house/detail?id=" + userMsg.getHouseId() + "&" + ResultMsg.successMsg("留言成功").asUrlParams();
    }
    
    @RequestMapping("/toAdd")
	public String toAdd(ModelMap modelMap) {
		modelMap.put("citys", houseService.getAllCitys());
		modelMap.put("communitys", houseService.getAllCommunitys());
		return "/house/add";
	}
    
    /**
     * 添加房产
     * @param house
     * @return
     */
    @RequestMapping("/add")
	public String doAdd(House house){
		User user = UserContext.getUser();
		houseService.addHouse(house,user);
		return "redirect:/house/ownlist?" +ResultMsg.successMsg("添加成功").asUrlParams();
	}
    
    /**
     * 展示个人房产列表
     * @param house
     * @param pageParams
     * @param modelMap
     * @return
     */
	@RequestMapping("/ownlist")
	public String ownlist(House house,PageParams pageParams,ModelMap modelMap){
		User user = UserContext.getUser();
		house.setUserId(user.getId());
		house.setBookmarked(false);
		modelMap.put("ps", houseService.queryHouse(house, pageParams));
		//pageType=own表示我的房产列表，另外还有房产收藏
		modelMap.put("pageType", "own");
		return "/house/ownlist";
	}
	
	/**
	 * 删除房产
	 * @param id
	 * @param pageType
	 * @return
	 */
	@RequestMapping(value="/del")
	public String delsale(Long id,String pageType){
	   User user = UserContext.getUser();
	   houseService.unbindUser2House(id,user.getId(),pageType.equals("own")?false:true);
	   return "redirect:/house/ownlist";
	}
	
	/**
	 * 评分
	 * @param rating
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/rating")
	public ResultMsg houseRate(Double rating,Long id){
		houseService.updateRating(id,rating);
		return ResultMsg.successMsg("ok");
	}
	
	/**
	 * 收藏
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/bookmark")
	public ResultMsg bookmark(Long id){
	  User user =	UserContext.getUser();
	  houseService.bindUser2House(id, user.getId(), true);
	  return ResultMsg.successMsg("ok");
	}
	
	/**
	 * 取消收藏
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/unbookmark")
	public ResultMsg unbookmark(Long id){
	  User user =	UserContext.getUser();
	  houseService.unbindUser2House(id,user.getId(),true);
	  return ResultMsg.successMsg("ok");
	}
	
	/**
	 * 收藏列表
	 * @param
	 * @return
	 */
	@RequestMapping("/bookmarked")
	public String bookmarked(House house,PageParams pageParams,ModelMap modelMap){
		User user = UserContext.getUser();
		house.setBookmarked(true);
		house.setUserId(user.getId());
		modelMap.put("ps", houseService.queryHouse(house, pageParams));
		modelMap.put("pageType", "book");
		return "/house/ownlist";
	}

}

