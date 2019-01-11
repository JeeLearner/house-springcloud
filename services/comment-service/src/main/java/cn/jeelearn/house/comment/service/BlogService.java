package cn.jeelearn.house.comment.service;

import cn.jeelearn.house.comment.common.LimitOffset;
import cn.jeelearn.house.comment.mapper.BlogMapper;
import cn.jeelearn.house.comment.model.Blog;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2019/1/11
 * @Version:v1.0
 */
@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

    /**
     * 查询博客列表
     * 数据填空处理
     * @param query
     * @param limit
     * @param offset
     * @return
     */
    public Pair<List<Blog>, Long> list(Blog query, Integer limit, Integer offset){
        List<Blog> blogs = blogMapper.selectBlogs(query, LimitOffset.build(limit, offset));
        //处理blog
        populate(blogs);
        Long count = blogMapper.selectBlogsCount(query);
        return ImmutablePair.of(blogs, count);
    }

    public Blog queryOne(Integer id){
        Blog query = new Blog();
        query.setId(id);
        List<Blog> blogs = blogMapper.selectBlogs(query, LimitOffset.build(1, 0));
        if (!CollectionUtils.isEmpty(blogs)){
            return blogs.get(0);
        }
        return null;
    }

    private void populate(List<Blog> blogs) {
        if (!CollectionUtils.isEmpty(blogs)){
            blogs.stream().forEach(item -> {
                String stripped = Jsoup.parse(item.getContent()).text();
                item.setDigest(stripped.substring(0, Math.min(stripped.length(), 40)));
                String tags = item.getTags();
                item.getTagList().addAll(Lists.newArrayList(Splitter.on(",").split(tags)));
            });
        }
    }
}

