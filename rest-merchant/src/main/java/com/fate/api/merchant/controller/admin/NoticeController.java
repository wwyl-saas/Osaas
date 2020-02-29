package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.FeedbackDto;
import com.fate.api.merchant.dto.NoticeDto;
import com.fate.api.merchant.dto.PageDto;
import com.fate.api.merchant.query.NoticeCreateQuery;
import com.fate.api.merchant.service.NoticeService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: parent
 * @description: 内部通知相关
 * @author:sjh
 * @create: 2019-07-18 17:01
 **/
@Api(value = "API - notice", description = "内部通知相关")
@RestController("adminNoticeController")
@RequestMapping("/api/admin/notice")
@Slf4j
public class NoticeController {

    @Autowired
    NoticeService noticeService;

    /*
     * admin创建通知
     */
    @ApiOperation(value="创建内部", notes="创建内部通知")
    @ApiImplicitParam(name = "noticeQuery", value = "内部通知", required = true,
            dataType = "NoticeCreateQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping("/create")
    public StandardResponse createNotice(@RequestBody @Validated NoticeCreateQuery noticeQuery){
        noticeService.createNotice(noticeQuery);
        return StandardResponse.success();
    }

    /*
     * admin某个商户的通知列表
     */
    @ApiOperation(value="admin查询某个商户的通知列表", notes="admin某个商户的通知列表")
    @ApiImplicitParam(name = "merchantId", value = "商户ID", required = true, dataType = "Long")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/get")
    public List<NoticeDto> getNoticeListByMerchant(@RequestParam(required = true) Long merchantId){
        return noticeService.getNoticeListByMerchant(merchantId);

    }





}
