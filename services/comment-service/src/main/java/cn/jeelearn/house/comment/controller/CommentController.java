package cn.jeelearn.house.comment.controller;

import cn.jeelearn.house.comment.common.RestResponse;
import cn.jeelearn.house.comment.model.Comment;
import cn.jeelearn.house.comment.model.req.CommentReq;
import cn.jeelearn.house.comment.service.CommentService;
import com.google.common.base.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/11
 * @Version:v1.0
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("list")
    public RestResponse<List<Comment>> list(@RequestBody CommentReq req){
        int type = req.getType();
        List<Comment> list = null;
        if(Objects.equal(1, type)){
            list = commentService.queryHouseComments(req.getHouseId(), req.getSize());
        } else {
            list = commentService.queryBlogComments(req.getBlogId(), req.getSize());
        }
        return RestResponse.success(list);
    }

    @PostMapping(value="add")
    public RestResponse<Object> leaveComment(@RequestBody CommentReq commentReq){
        Integer type = commentReq.getType();
        if (Objects.equal(1, type)) {
            commentService.addHouseComment(commentReq.getHouseId(),commentReq.getContent(),commentReq.getUserId());
        }else {
            commentService.addBlogComment(commentReq.getBlogId(),commentReq.getContent(),commentReq.getUserId());
        }
        return RestResponse.success();
    }
}

