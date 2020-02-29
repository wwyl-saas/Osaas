package com.fate.api.customer.controller;


import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.query.FeedbackCreateQuery;
import com.fate.api.customer.service.FeedBackService;
import com.fate.common.enums.FeedBackType;
import com.fate.common.model.StandardResponse;
import com.fate.common.util.QiNiuUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import java.io.IOException;


/**
 * @program: parent
 * @description: 个人中心反馈意见相关
 * @author: songjinghuan
 * @create: 2019-05-25 23:12
 **/
@Api(value = "API - feedBack", description = "用户端个人中心反馈相关接口")
@RestController
@RequestMapping("/api/v1/feedback")
@Slf4j
public class FeedBackController {


    @Autowired
    FeedBackService feedBackService;


    @ApiOperation(value="提交意见反馈", notes="包含反馈问题类型，反馈意见，是否匿名")
    @ApiImplicitParam(name = "feedback", value = "提交数据", required = true, dataType = "FeedbackCreateQuery")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @PostMapping("/submit")
    public StandardResponse submitfeedBack(@Validated @RequestBody FeedbackCreateQuery feedback){
        //todo 增加formid用于消息交互
        feedBackService.createFeedback(feedback);
        return StandardResponse.success();
    }


    @ApiOperation(value="反馈提交图片", notes="参数为文件")
    @ApiImplicitParam(name = "file", value = "图片文件", required = true, dataType = "CommonsMultipartFile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @PostMapping(value = "/upload/img")
    public StandardResponse uploadFeedBackFile(@RequestParam("file") MultipartFile file) throws IOException {
        String path= QiNiuUtil.uploadInputStream(file.getInputStream());
        return StandardResponse.success(path);
    }


    @ApiOperation(value="获取反馈类型", notes="无参数")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @GetMapping(value = "/feedbacktype/get")
    public StandardResponse getFeedBackType() throws IOException {
        FeedBackType[] feedBackTypes=FeedBackType.values();
        return StandardResponse.success(feedBackTypes);
    }



}
