package cn.jeelearn.house.api.common.page;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Description:
 * @Auther: lyd
 * @Date: 2018/12/16
 * @Version:v1.0
 */
public class Pagination {

    public static final int DEFAULT_LIMIT      = 10;
    public static final int DEFAULT_PAGE       = 1;
    public static final int DEFAULT_OFFSET     = 0;
    public static final int DEFAULT_PAGE_SIZE  = 10;

    private int queryCount;
    private int pageNum;
    private  int pageSize;
    private long totalCount;
    private List<Integer> pages = Lists.newArrayList();

    public Pagination(Integer pageSize, Integer pageNum, Long totalCount, Integer queryCount){
        this.totalCount = totalCount;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.queryCount = queryCount;
        for (int i=1;i<=pageNum;i++){
            pages.add(i);
        }
        long pageCount = totalCount/pageSize + ((totalCount % pageSize ==0) ? 0: 1);
        if (pageCount > pageNum) {
            //这里把页码都展示，具有可扩展性。比如可以第10页后显示...
            for(int i= pageNum + 1; i<= pageCount ;i ++){
                pages.add(i);
            }
        }

    }


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<Integer> getPages() {
        return pages;
    }

    public void setPages(List<Integer> pages) {
        this.pages = pages;
    }
}

