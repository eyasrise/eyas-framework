package com.eyas.framework.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Created by yixuan on 2019/9/11.
 */
@Data
public class EyasFrameworkBaseQuery implements Serializable {

    private static final long serialVersionUID = -7779110672848186411L;

    /**
     * 页面大小
     */
    @ApiModelProperty(value = "页面大小")
    private Integer pageSize;

    /**
     * 当前页
     */
    @ApiModelProperty(value = "当前页")
    private Integer currentPage;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数")
    private Integer totalRecord;

    /**
     * 排序字段
     */
    @ApiModelProperty(value = "排序字段")
    private String sort;

    /**
     * 最大页面大小
     */
    private static Integer MAX_PAGE_SIZE = 2000;

    /**
     * 默认页面大小
     */
    private static Integer DEFAULT_PAGE_SIZE = 20;

    /**
     *
     */
    public static final String ASC = "ASC";

    /**
     *
     */
    public static final String DESC = "DESC";

    public Integer getPageSize() {
        return this.pageSize != null && this.pageSize >= 1 ? this.pageSize : DEFAULT_PAGE_SIZE;
    }

    public void setPageSize(Integer pageSize) {
        if (pageSize == null) {
            this.pageSize = DEFAULT_PAGE_SIZE;
        } else if (pageSize > MAX_PAGE_SIZE) {
            this.pageSize = MAX_PAGE_SIZE;
        } else {
            this.pageSize = pageSize;
        }
    }

    public Integer getCurrentPage() {
        return this.currentPage != null && this.currentPage >= 1 ? this.currentPage : 1;
    }


    public Integer getPageTotal() {
        if (this.totalRecord == null) {
            return 0;
        } else {
            int tp = this.totalRecord / this.getPageSize();
            if (this.totalRecord % this.getPageSize() > 0) {
                ++tp;
            }

            return tp;
        }
    }

    public Integer getOffset() {
        return (this.getCurrentPage() - 1) * this.getPageSize();
    }

}
