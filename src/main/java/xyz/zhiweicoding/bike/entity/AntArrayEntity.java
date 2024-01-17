package xyz.zhiweicoding.bike.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Created by zhiwei on 2024/01/05.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AntArrayEntity<T> implements Serializable {

    private int current;
    private boolean success;
    private List<T> data;
    private long total;
    private int pageSize;

    public AntArrayEntity() {
    }

    /**
     * @param current  当前页
     * @param data     数据
     * @param pageSize 每页大小
     */
    public AntArrayEntity(int current, List<T> data, int pageSize, int total) {
        this.current = current;
        this.success = true;
        this.data = data;
        this.total = total;
        this.pageSize = pageSize;
    }

    /**
     * 判断bean 是否为空
     *
     * @return
     */
    public boolean getIsEmpty() {
        return data == null || data.isEmpty();
    }

}
