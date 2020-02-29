package com.fate.api.merchant.controller.mobile;

import com.fate.api.merchant.dto.*;
import com.fate.api.merchant.service.NoticeService;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: parent
 * @description: 通知相关
 * @author: chenyixin
 * @create: 2019-08-30 13:27
 **/
@Api(value = "API - notice", description = "通知相关")
@RestController
@RequestMapping("/api/message")
@Slf4j
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @ApiOperation(value = "获取未读消息数", notes = "")
    @GetMapping("/notice/num")
    public NoticeNumDto getNoticeNum() {
        return noticeService.getNoticeNum();
    }



    @ApiOperation(value = "获取通知消息列表", notes = "")
    @GetMapping("/notice/list")
    public List<NoticeDto> getNoticeList() {
        return noticeService.getNoticeList();
    }



    @ApiOperation(value = "获取公告列表", notes = "")
    @GetMapping("/announce/list")
    public List<AnnounceDto> getAnnounceList() {
        return noticeService.getAnnounceList();
    }


    @ApiOperation(value = "获取反馈列表", notes = "")
    @GetMapping("/feedback/list")
    public List<FeedbackDto> getFeedbackList() {
        return noticeService.getFeedbackList();
    }


    @ApiOperation(value = "设置通知已读", notes = "")
    @PostMapping("/update/notice")
    public StandardResponse setNoticeRead(@RequestParam Long noticeId) {
        noticeService.setNoticeRead(noticeId);
        return StandardResponse.success();
    }

    @ApiOperation(value = "设置公告已读", notes = "")
    @PostMapping("/update/announce")
    public StandardResponse setAnnounceRead(@RequestParam Long announceId) {
        noticeService.setAnnounceRead(announceId);
        return StandardResponse.success();
    }


    @ApiOperation(value = "设置通知已读", notes = "")
    @PostMapping("/update/feedback")
    public StandardResponse setFeedbackRead(@RequestParam Long feedbackId) {
        noticeService.setFeedbackRead(feedbackId);
        return StandardResponse.success();
    }


}
