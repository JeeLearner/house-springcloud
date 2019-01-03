package cn.jeelearn.house.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    //@Autowired
    //private HouseService houseService;

    @RequestMapping("")
    public String index(ModelMap modelMap){
        return "redirect:/index";
    }

    /**
     * 1.首页展示
     * 2.新上房源
     * @auther: lyd
     * @date: 2018/12/18
     */
    @RequestMapping("index")
    public String accountsRegister(ModelMap modelMap){
        /*House house = new House();
        house.setSort("create_time");
        List<House> houses = houseService.queryAndSetImg(house, PageParams.build(8, 1));
        modelMap.put("recomHouses", houses);*/
        return "/homepage/index";
    }
}