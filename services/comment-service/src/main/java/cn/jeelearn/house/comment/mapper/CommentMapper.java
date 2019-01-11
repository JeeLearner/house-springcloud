package cn.jeelearn.house.comment.mapper;

import cn.jeelearn.house.comment.common.LimitOffset;
import cn.jeelearn.house.comment.model.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/11
 * @Version:v1.0
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectComments(@Param("houseId") long houseId, @Param("size") int size);

    List<Comment> selectBlogComments(@Param("blogId") long blogId,@Param("size") int size);

    int insert(Comment comment);
}

