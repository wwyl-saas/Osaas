package com.fate.api.merchant.controller.mobile;

import com.fate.api.merchant.dto.FeedbackDto;
import com.fate.api.merchant.dto.NoticeNumDto;
import com.fate.api.merchant.service.FeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: parent
 * @description: 获取反馈信息
 * @author: chenyixin
 * @create: 2019-09-21 16:08
 **/
@Api(value = "API - feedback", description = "反馈相关")
@RestController
@RequestMapping("/api/feedback")
@Slf4j
public class feedbackController {
    @Autowired
    FeedbackService feedbackService;

    @ApiOperation(value = "获取反馈详情", notes = "")
    @GetMapping("/detail")
    public FeedbackDto getNoticeNum(@RequestParam Long feedbackId) {
        return feedbackService.getDetail(feedbackId);
    }

}
