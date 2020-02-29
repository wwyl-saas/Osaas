package com.fate.api.merchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @program: parent
 * @description: 明细列表相关
 * @author: chenyixin
 * @create: 2019-08-24 01:49
 **/
@Data
@ApiModel
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticListDto implements Serializable {
    @ApiModelProperty("页码")
    private long pageIndex = 1;//页码，默认是第一页
    @ApiModelProperty("页长")
    private long pageSize = 10;//每页显示的记录数，默认是10
    @ApiModelProperty("总页数")
    private long totalPage;//总页数
    @ApiModelProperty("会员信息列表")
    private List<CustomerDto> customerList;
    @ApiModelProperty("预约信息列表")
    private List<AppointmentDto> appointmentList;
    @ApiModelProperty("充值记录列表")
    private List<ChargeDto> chargeList;
    @ApiModelProperty("订单列表")
    private List<OrderDto> orderList;
    @ApiModelProperty("统计记录")
    private List<StatisticDataDto> dataList;
}
