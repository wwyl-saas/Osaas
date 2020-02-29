package com.fate.api.merchant.controller.admin;

import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.service.GoodsAttributeService;
import com.fate.common.entity.GoodsAttribute;
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
@Api(value = "API - goodsAttribute", description = "服务属性列表页面")
@RestController("adminGoodsAttributeController")
@RequestMapping("/api/admin/goodsAttribute")
@Slf4j
public class GoodAttributeController {
    @Autowired
    GoodsAttributeService goodsAttributeService;
    @GetMapping("/list")
    public PageDto<GoodsAttribute> getList(@RequestParam(required = false,defaultValue = "1") Integer pageIndex, @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        return this.goodsAttributeService.getGoodsAttributeList(pageIndex,pageSize);
    }

}
