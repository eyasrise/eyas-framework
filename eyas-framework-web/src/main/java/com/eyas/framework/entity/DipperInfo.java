package com.eyas.framework.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.util.Date;

@Data
public class DipperInfo {

  private Long id;
  private String code;
  private Integer status;
  private Integer type;
  private Date createTime;
  private Date updateTime;
  private Integer rowLock;
  private Integer rowStatus;
  private String remark;
  private String creator;
  private String operator;
  private String extAtt;
  private String tenantCode;
  @Version
//  @TableId(value = "1") // 第一次加数据时使其有个默认值1
  private Integer version;

}
