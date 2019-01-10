package cn.jeelearn.house.user.controller;

import cn.jeelearn.house.user.common.ListResponse;
import cn.jeelearn.house.user.common.PageParams;
import cn.jeelearn.house.user.common.RestResponse;
import cn.jeelearn.house.user.model.Agency;
import cn.jeelearn.house.user.model.User;
import cn.jeelearn.house.user.service.AgencyService;
import org.apache.commons.lang3.tuple.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/29
 * @Version:v1.0
 */
@RestController
@RequestMapping("agency")
public class AgencyController {

    @Autowired
    private AgencyService agencyService;

    /**
     * 查询经纪人分页列表
     * @param limit
     * @param offset
     * @return
     */
    @GetMapping("agentList")
    public RestResponse<ListResponse<User>> pageAgency(Integer limit, Integer offset){
        PageParams pageParams = new PageParams();
        pageParams.setLimit(limit);
        pageParams.setOffset(offset);
        Pair<List<User>, Long > pair = agencyService.getAllAgent(pageParams);
        ListResponse<User> response = ListResponse.build(pair.getKey(), pair.getValue());
        return RestResponse.success(response);
    }

    /**
     * 经纪人详情
     * @param id
     * @return
     */
    @GetMapping("agentDetail")
    public RestResponse<User> agentDetail(Long id) {
        User user = agencyService.getAgentDetail(id);
        return RestResponse.success(user);
    }

    @GetMapping("list")
    public RestResponse<List<Agency>> agencyList() {
        List<Agency> agencies = agencyService.getAllAgency();
        return RestResponse.success(agencies);
    }
}

