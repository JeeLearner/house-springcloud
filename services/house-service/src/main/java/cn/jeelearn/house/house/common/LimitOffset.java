package cn.jeelearn.house.house.common;

/**
 * @Description: 分页参数
 * @Auther: lyd
 * @Date: 2019/1/7
 * @Version:v1.0
 */
public class LimitOffset {
    private Integer limit;
    private Integer offset;

    public static LimitOffset build(Integer limit,Integer offset){
        LimitOffset limitOffset = new LimitOffset();
        limitOffset.setLimit(limit);
        limitOffset.setOffset(offset);
        return limitOffset;
    }



    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }
}

