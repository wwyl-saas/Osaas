package com.fate.api.customer.controller;

import com.fate.api.customer.annotation.Auth;
import com.fate.api.customer.dto.CustomerDto;
import com.fate.api.customer.dto.PluginParamDto;
import com.fate.api.customer.service.WxMemberCardService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @program: parent
 * @description: 微信会员卡
 * @author: chenyixin
 * @create: 2019-09-07 19:56
 **/
@Api(value = "API - 个人中心", description = "小程序个人中心接口")
@RestController
@RequestMapping("/api/v1/wx/member/card")
@Slf4j
public class WxMemberCardController {
    @Autowired
    WxMemberCardService wxMemberCardService;

    /**
     * 获取插件跳转参数
     * @return
     * @throws WxErrorException
     */
    @ApiOperation(value="获取插件跳转参数", notes="")
    @ApiImplicitParam(name = "cardId", value = "商户会员卡套Id", required = true, dataType = "String", paramType = "query")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @GetMapping("/plugin/param")
    public PluginParamDto getPluginParam(@RequestParam String cardId) throws WxErrorException {
        return wxMemberCardService.getPluginParam(cardId);
    }

    /**
     * 激活会员卡并刷新个人中心页数据
     * @return
     * @throws WxErrorException
     */
    @ApiOperation(value="激活会员卡并刷新个人中心页数据", notes="")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cardId", value = "商户会员卡套Id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "activateTicket", value = "激活凭证", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "会员卡号", dataType = "String", paramType = "query")
    })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @Auth
    @PostMapping("/active/card")
    public CustomerDto activeCard(@RequestParam String activateTicket,
                                  @RequestParam String cardId,
                                  @RequestParam String code) throws WxErrorException {
        return wxMemberCardService.getActivateMemberCard(activateTicket,cardId,code);
    }
}
