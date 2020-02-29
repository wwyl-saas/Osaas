package com.fate.api.merchant.controller.admin;

import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.service.GoodsIssueService;
import com.fate.common.entity.GoodsIssue;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: rest-merchant
 * @description: 服务问题相关
 * @author: xudongdong
 * @create: 2019-10-29 00:01
 **/
@Api(value = "API - goodsIssue", description = "服务问题列表页面")
@RestController("adminGoodsIssueController")
@RequestMapping("/api/admin/goodsIssue")
@Slf4j
public class GoodsIssueController {
    @Autowired
    GoodsIssueService goodsIssueService;
    @GetMapping("/list")
    public PageDto<GoodsIssue> getList(@RequestParam(required = false,defaultValue = "1") Integer pageIndex, @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return this.goodsIssueService.getGoodsIssueList(pageIndex,pageSize);
    }

}
