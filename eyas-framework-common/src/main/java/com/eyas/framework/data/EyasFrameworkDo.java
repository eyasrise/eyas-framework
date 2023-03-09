package com.eyas.framework.data;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Created by yixuan on 2019/6/20.
 */
@Data
public class EyasFrameworkDo implements Serializable {

    private static final long serialVersionUID = -7779110672848186411L;

    /**
     * 行号
     */
    @ApiModelProperty(value = "行号")
    private Long id;

    /**
     * 业务主键
     */
    @ApiModelProperty(value = "业务主键")
    private String code;

    /**
     * 数据业务状态
     */
    @ApiModelProperty(value = "数据业务状态")
    private Integer status;

    /**
     * 数据类型
     */
    @ApiModelProperty(value = "数据类型")
    private Integer type;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Date updateTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creator;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String operator;

    /**
     * 行锁
     */
    @ApiModelProperty(value = "行锁")
    private Integer rowLock;

    /**
     * 行状态-0正常 -1删除
     */
    @ApiModelProperty(value = "行状态-0正常 -1删除")
    private Integer rowStatus;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 备用字段
     */
    @ApiModelProperty(value = "备用字段")
    private String extAtt;

    /**
     * 租户编码code
     */
    @ApiModelProperty(value = "租户编码code")
    private Long tenantCode;


    /**
     * 批量操作数据
     */
    private List<Long> idList;

}
