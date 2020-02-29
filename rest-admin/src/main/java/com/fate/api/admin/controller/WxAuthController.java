package com.fate.api.admin.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.open.api.WxOpenService;
import me.chanjar.weixin.open.bean.result.WxOpenAuthorizerInfoResult;
import me.chanjar.weixin.open.bean.result.WxOpenQueryAuthResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: parent
 * @description: 微信授权
 * @author: chenyixin
 * @create: 2019-10-21 15:28
 **/
@RestController
@RequestMapping("/api/bind")
@Slf4j
public class WxAuthController {
    @Value("${server.domain}")
    private String domian;
    @Autowired
    private WxOpenService wxOpenService;
    
    
    @GetMapping("/auth/show")
    @ResponseBody
    public String gotoPreAuthUrlShow(){
        return "<a href='url'>go</a>";
    }


    @GetMapping("/auth/url")
    public void gotoPreAuthUrl(HttpServletRequest request, HttpServletResponse response){
        String host = request.getHeader("host");
        String url = "https://"+host+"/api/bind/auth/jump";
        try {
            url = wxOpenService.getWxOpenComponentService().getPreAuthUrl(url);
            response.sendRedirect(url);
        } catch (WxErrorException | IOException e) {
            log.error("gotoPreAuthUrl", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * {
     *     "code": 0,
     *     "msg": "成功",
     *     "time": "1571646429116",
     *     "data": {
     *         "authorizationInfo": {
     *             "authorizerAppid": "wx2229b06a20b4b02b",
     *             "authorizerAccessToken": "26_phYzBbCFJY-CLFsvRn6JfTfKDOxo9CAvM3QywqVU5NlSUPSOBGLGFbtV5XZv17817O1HUahp_2TRsfT_GIAUEpgkeQltHE9P2Rxq2c-BOkYJiGlj0bhKAfwj5uay5YJXJ0s4tT-f8rDL57IfFHIdADDIKL",
     *             "expiresIn": 7199,
     *             "authorizerRefreshToken": "refreshtoken@@@ifaKrAwWZUDwN1L6DzEUIsw2yahzS6854Mk1ttZi79w",
     *             "funcInfo": [
     *                 1,
     *                 15,
     *                 4,
     *                 7,
     *                 2,
     *                 3,
     *                 11,
     *                 6,
     *                 5,
     *                 8,
     *                 13,
     *                 9,
     *                 10,
     *                 12,
     *                 22,
     *                 23,
     *                 24,
     *                 26,
     *                 27,
     *                 33,
     *                 34,
     *                 35,
     *                 44,
     *                 46,
     *                 47
     *             ]
     *         }
     *     }
     * }
     * @param authorizationCode
     * @return
     */

    @GetMapping("/auth/jump")
    @ResponseBody
    public WxOpenQueryAuthResult jump(@RequestParam("auth_code") String authorizationCode){
        try {
            WxOpenQueryAuthResult queryAuthResult = wxOpenService.getWxOpenComponentService().getQueryAuth(authorizationCode);
            log.info("getQueryAuth", queryAuthResult);
            return queryAuthResult;
        } catch (WxErrorException e) {
            log.error("gotoPreAuthUrl", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * {
     *     "code": 0,
     *     "msg": "成功",
     *     "time": "1571646681022",
     *     "data": {
     *         "authorizationInfo": {
     *             "authorizerAppid": "wx2229b06a20b4b02b",
     *             "expiresIn": 0,
     *             "authorizerRefreshToken": "refreshtoken@@@ifaKrAwWZUDwN1L6DzEUIsw2yahzS6854Mk1ttZi79w",
     *             "funcInfo": [
     *                 1,
     *                 15,
     *                 4,
     *                 7,
     *                 2,
     *                 3,
     *                 11,
     *                 6,
     *                 5,
     *                 8,
     *                 13,
     *                 9,
     *                 10,
     *                 12,
     *                 22,
     *                 23,q
     *                 24,
     *                 26,
     *                 27,
     *                 33,
     *                 34,
     *                 35,
     *                 44,
     *                 46,
     *                 47
     *             ]
     *         },
     *         "authorizerInfo": {
     *             "nickName": "万物优联",
     *             "headImg": "http://wx.qlogo.cn/mmopen/ynJ3Gib5StoL67dMFPJC2nPexQYozktkrec6W2F7B3Spmkv2hYAjnRBwUfCxT7Flrp2kk8PISeaZCQQUUXF3oKd5icQia2ptEJ7/0",
     *             "serviceTypeInfo": 2,
     *             "verifyTypeInfo": 0,
     *             "userName": "gh_e358755ebd4d",
     *             "principalName": "北京万物优联科技有限公司",
     *             "businessInfo": {
     *                 "open_pay": 1,
     *                 "open_shake": 0,
     *                 "open_scan": 0,
     *                 "open_card": 1,
     *                 "open_store": 0
     *             },
     *             "alias": "wanwuyoulian",
     *             "qrcodeUrl": "http://mmbiz.qpic.cn/mmbiz_jpg/Bl39GIbRIyKRHFibiaEj7jR9DTcXpVzHm5nwWd5yic1r3WQUxOtbQFNXo0gfOU1NcSevMkayj1mydm6n5blC8fsRA/0",
     *             "signature": "致力于为线下门店提供管理咨询、技术支持、营销推广、渠道运营等服务。"
     *         },
     *         "miniProgram": false
     *     }
     * }
     * @param appId
     * @return
     */
    @GetMapping("/auth/info")
    @ResponseBody
    public WxOpenAuthorizerInfoResult getAuthorizerInfo(@RequestParam String appId){
        try {
            return wxOpenService.getWxOpenComponentService().getAuthorizerInfo(appId);
        } catch (WxErrorException e) {
            log.error("getAuthorizerInfo", e);
            throw new RuntimeException(e);
        }
    }
}
