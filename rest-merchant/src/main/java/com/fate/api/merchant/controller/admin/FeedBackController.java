package com.fate.api.merchant.controller.admin;


import com.fate.api.merchant.dto.FeedbackDto;
import com.fate.api.merchant.service.FeedbackService;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @program: parent
 * @description: 反馈相关
 * @author:
 * @create: 2019-07-18 17:01
 **/
@Api(value = "API - notice", description = "反馈相关")
@RestController("adminFeedBackController")
@RequestMapping("/api/admin/feedback")
@Slf4j
public class FeedBackController {


    @Autowired
    FeedbackService feedbackService;

    /*
     * admin某个商户的反馈列表查询
     */
    @ApiOperation(value="反馈查询", notes="查询某个商户的反馈列表")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @GetMapping("/get")
    public List<FeedbackDto> getFeedBackListByMerchant(){
        return feedbackService.getFeedbackListByMerchant();

    }







}
