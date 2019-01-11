package cn.jeelearn.house.comment.controller;

import cn.jeelearn.house.comment.common.ListResponse;
import cn.jeelearn.house.comment.common.RestResponse;
import cn.jeelearn.house.comment.model.Blog;
import cn.jeelearn.house.comment.model.req.BlogQueryReq;
import cn.jeelearn.house.comment.service.BlogService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/11
 * @Version:v1.0
 */
@RestController
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping("list")
    public RestResponse<ListResponse<Blog>> list(@RequestBody BlogQueryReq req){
        Pair<List<Blog>, Long> pair = blogService.list(req.getBlog(), req.getLimit(), req.getOffset());
        return RestResponse.success(ListResponse.build(pair.getLeft(), pair.getRight()));
    }

    @GetMapping("detail")
    public RestResponse<Blog> one(Integer id){
        Blog blog = blogService.queryOne(id);
        return RestResponse.success(blog);
    }
}

