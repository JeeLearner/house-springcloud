package cn.jeelearn.house.comment.mapper;

import cn.jeelearn.house.comment.common.LimitOffset;
import cn.jeelearn.house.comment.model.Blog;
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
public interface BlogMapper {

    public List<Blog> selectBlogs(@Param("blog") Blog blog, @Param("pageParams")LimitOffset limitOffset);

    public Long selectBlogsCount(Blog query);
}

