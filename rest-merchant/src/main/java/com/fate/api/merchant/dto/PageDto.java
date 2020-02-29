package com.fate.api.merchant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * @program: parent
 * @description: 分页信息
 * @author: chenyixin
 * @create: 2019-07-23 11:22
 **/
@Getter
@ToString
@AllArgsConstructor
public class PageDto<T> implements Serializable {
    private long pageIndex = 1;//页码，默认是第一页
    private long pageSize = 10;//每页显示的记录数，默认是10
    private long totalRecord;//总记录数
    private long totalPage;//总页数
    private List<T> results;//对应的当前页记录
}
