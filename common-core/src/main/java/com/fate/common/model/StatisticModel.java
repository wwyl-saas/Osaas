package com.fate.common.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @program: parent
 * @description: 商铺用户评价
 * @author: chenyixin
 * @create: 2019-09-26 13:04
 **/
@Data
public class StatisticModel implements Serializable {
    private Long shopId;
    private Long merchantUserId;
    private BigDecimal dataValue;
}
