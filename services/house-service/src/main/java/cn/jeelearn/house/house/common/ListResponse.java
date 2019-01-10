package cn.jeelearn.house.house.common;

import java.util.List;

/**
 * @Description: 分页时可以返回总数
 * @Auther: lyd
 * @Date: 2018/12/29
 * @Version:v1.0
 */
public class ListResponse<T> {

    private List<T> list;

    private long count;

    public static <T> ListResponse<T> build(List<T> list,Long count) {
        ListResponse<T> response = new ListResponse<>();
        response.setCount(count);
        response.setList(list);
        return response;
    }


    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}

