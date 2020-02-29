package com.fate.api.admin.controller;

import com.fate.api.admin.annotation.AuthIgnore;
import com.fate.api.admin.exception.BaseException;
import com.fate.api.admin.service.WxMemberCardService;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.io.FileUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @program: parent
 * @description: 微信会员卡
 * @author: chenyixin
 * @create: 2019-09-05 12:51
 **/
@Api(value = "API -wxMemberCard ", description = "微信会员卡相关接口")
@RestController
@RequestMapping("/api/wx/member/card")
@Slf4j
public class WxMemberCardController {
    @Autowired
    WxMemberCardService wxMemberCardService;

    /**
     * 创建会员卡
     * @return
     * @throws WxErrorException
     * @throws IOException
     */
    @PostMapping("/create")
    public StandardResponse createMemberCard() throws WxErrorException, IOException {
        wxMemberCardService.createCard();
        return StandardResponse.success();
    }

    /**
     * 设置开卡参数
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/set/open/info")
    public StandardResponse queryAuditing(String cardId) throws WxErrorException {
        wxMemberCardService.setOpenInfo(cardId);
        return StandardResponse.success();
    }

    /**
     * 获取插件跳转参数
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/get/plugin/param")
    public StandardResponse getPluginParam() throws WxErrorException {
        return StandardResponse.success(wxMemberCardService.getPluginParam());
    }

    /**
     * 获取激活用户填写的信息
     * @return
     * @throws WxErrorException
     */
    @GetMapping("/get/active/temp/info")
    public StandardResponse getActivateTempInfo(String activateTicket) throws WxErrorException {
        return StandardResponse.success(wxMemberCardService.getActivateTempInfo(activateTicket));
    }

    /**
     * 激活会员卡
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/activate/member/card")
    public StandardResponse activateMemberCard() throws WxErrorException {
        wxMemberCardService.activateMemberCard();
        return StandardResponse.success();
    }

    /**
     * 设置卡券白名单
     * @param customerId
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/testwhitelist")
    public StandardResponse setWhiteList(Long customerId) throws WxErrorException {
        wxMemberCardService.setWhiteList(customerId);
        return StandardResponse.success();
    }

    @RequestMapping("/list")
    public StandardResponse getMemberCardList() {
        return StandardResponse.success();
    }


    /**
     * 删除会员卡
     * @param cardId
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/delete")
    public StandardResponse delete(String cardId) throws WxErrorException {
        wxMemberCardService.delete(cardId);
        return StandardResponse.success();
    }



    /**
     * 更新会员卡
     * @param cardId
     * @return
     * @throws WxErrorException
     */
    @PostMapping("/update")
    public StandardResponse update(String cardId) throws WxErrorException {
        wxMemberCardService.update(cardId);
        return StandardResponse.success();
    }


}
