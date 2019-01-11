package cn.jeelearn.house.comment.service;

import cn.jeelearn.house.comment.common.BeanHelper;
import cn.jeelearn.house.comment.common.CommonConstants;
import cn.jeelearn.house.comment.dao.UserDao;
import cn.jeelearn.house.comment.mapper.CommentMapper;
import cn.jeelearn.house.comment.model.Comment;
import cn.jeelearn.house.comment.model.User;
import cn.jeelearn.house.comment.model.req.CommentReq;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/11
 * @Version:v1.0
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserDao userDao;
    @Autowired
    private StringRedisTemplate redisTemplate;

    public List<Comment> queryHouseComments(long houseId,int size) {
        List<Comment> comments = commentMapper.selectComments(houseId, size);
        comments.forEach(c -> {
            User user = userDao.getUserDetail(c.getUserId());
            c.setUserName(user.getName());
            c.setAvatar(user.getAvatar());
        });
        return comments;
    }

    public List<Comment> queryBlogComments(long blogId, int size) {
        List<Comment> comments = commentMapper.selectBlogComments(blogId, size);
        comments.forEach(comment -> {
            User user = userDao.getUserDetail(comment.getUserId());
            comment.setUserName(user.getName());
            comment.setAvatar(user.getAvatar());
        });
        return comments;
    }

    //------每次查询评论都要请求用户服务，效率极低，因此用redis进行优化
    public List<Comment> queryHouseComments(Long houseId,Integer size) {
        String key  ="house_comments" + "_" + houseId + "_" + size;
        String json = redisTemplate.opsForValue().get(key);
        List<Comment> lists = null;
        if(Strings.isNullOrEmpty(json)){
            lists = doGetHouseComments(houseId, size);
            redisTemplate.opsForValue().set(key, JSON.toJSONString(lists));
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        } else {
            lists = JSON.parseObject(json, new TypeReference<List<Comment>>(){});
        }
        return lists;
    }

    public List<Comment> queryBlogComments(Long blogId,Integer size) {
        String key  ="blog_comments" + "_" + blogId + "_" + size;
        String json = redisTemplate.opsForValue().get(key);
        List<Comment> lists = null;
        if(Strings.isNullOrEmpty(json)){
            lists = doGetBlogComments(blogId, size);
            redisTemplate.opsForValue().set(key, JSON.toJSONString(lists));
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        } else {
            lists = JSON.parseObject(json, new TypeReference<List<Comment>>(){});
        }
        return lists;
    }

    public List<Comment> doGetHouseComments(Long houseId, Integer size) {
        List<Comment>  comments = commentMapper.selectComments(houseId, size);
        comments.forEach(comment -> {
            User user = userDao.getUserDetail(comment.getUserId());
            comment.setAvatar(user.getAvatar());
            comment.setUserName(user.getName());
        });
        return comments;
    }

    public List<Comment> doGetBlogComments(Long blogId, Integer size) {
        List<Comment> comments = commentMapper.selectBlogComments(blogId, size);
        comments.forEach(comment -> {
            User user = userDao.getUserDetail(comment.getUserId());
            comment.setUserName(user.getName());
            comment.setAvatar(user.getAvatar());
        });
        return comments;
    }





    //--------------------新增评论--------------------------
    public void addHouseComment(Long houseId, String content, Long userId) {
        addComment(houseId, null, content, userId, CommonConstants.COMMENT_HOUST_TYPE);
    }

    public void addBlogComment(Integer blogId, String content, Long userId) {
        addComment(null,blogId, content, userId, CommonConstants.COMMENT_BLOG_TYPE);
    }

    private void addComment(Long houseId, Integer blogId, String content, Long userId, int type){
        String key = null;
        Comment comment = new Comment();
        if (type == 1) {
            comment.setHouseId(houseId);
        }else {
            comment.setBlogId(blogId);
        }
        comment.setContent(content);
        comment.setUserId(userId);
        comment.setType(type);
        BeanHelper.onInsert(comment);
        BeanHelper.setDefaultProp(comment, Comment.class);
        commentMapper.insert(comment);
    }
}

