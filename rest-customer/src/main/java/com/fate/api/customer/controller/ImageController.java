package com.fate.api.customer.controller;

import cn.binarywang.wx.miniapp.api.WxMaQrcodeService;
import com.fate.api.customer.config.wx.WxMaConfiguration;
import com.fate.api.customer.service.ImageService;
import com.fate.api.customer.service.PayForAnotherService;
import com.fate.api.customer.service.PayService;
import com.fate.api.customer.util.CurrentApplicationUtil;
import com.fate.api.customer.util.CurrentCustomerUtil;
import com.fate.common.util.QRAndBarCodeUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URLDecoder;

/**
 * @program: parent
 * @description: 图片加载接口
 * @author: chenyixin
 * @create: 2019-09-25 23:39
 **/
@Api(value = "API -goods ", description = "商品相关接口")
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageController {
    @Autowired
    PayService payService;
    @Autowired
    PayForAnotherService payForAnotherService;
    @Autowired
    ImageService imageService;


    @ApiOperation(value="支付条形码", notes="生产支付条形码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationId", value = "应用id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "经过urlEncoder的token", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/pay/barcode")
    public void eneratePayBarCode(@RequestParam Long applicationId,
                                   @RequestParam String token,
                                   HttpServletResponse response)throws Exception {
        String tokenString= URLDecoder.decode(token,"utf-8");
        imageService.prepareApplicationContext(applicationId);
        imageService.prepareCustomerContext(applicationId,tokenString);
        String code = payService.takePayCode();
        BufferedImage image = QRAndBarCodeUtil.getBarcode(code);
        if (image!=null){
            ImageIO.write(image,"jpg",response.getOutputStream());
        }
    }


    @ApiOperation(value="代付二维码", notes="利用zxing生成代付二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationId", value = "应用id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "经过urlEncoder的token", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/another/pay/qrcode")
    public void payForAnotherQRCode(@RequestParam Long applicationId,
                                    @RequestParam String token,
                                    HttpServletResponse response)throws Exception {
        //todo 增加formId用来做支付时的通知
        // todo 增加有效时长
        String tokenString= URLDecoder.decode(token,"utf-8");
        imageService.prepareApplicationContext(applicationId);
        imageService.prepareCustomerContext(applicationId,tokenString);
        String code=payForAnotherService.payForAnotherQRCode();
        BufferedImage image = QRAndBarCodeUtil.createImage(code, "", false);
        if (image!=null){
            ImageIO.write(image,"jpg",response.getOutputStream());
        }
    }



    @ApiOperation(value="代付小程序码", notes="生成小程序码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationId", value = "应用id", required = true, dataType = "String", paramType = "query"),
    })
    @GetMapping("/applet/code")
    public void generateMiniCodeWithoutCustomer(@RequestParam Long applicationId,
                                 HttpServletResponse response)throws Exception {
        imageService.prepareApplicationContext(applicationId);
        String appId= CurrentApplicationUtil.getMerchantApplication().getAppId();
        WxMaQrcodeService wxMaQrcodeService=WxMaConfiguration.getMaService(appId). getQrcodeService();
        byte[] bytes= wxMaQrcodeService.createWxaCodeUnlimitBytes("payfor","pages/index/index",280, true, null, false);
        BufferedImage image= ImageIO.read(new ByteArrayInputStream(bytes));
        if (image!=null){
            ImageIO.write(image,"jpg",response.getOutputStream());
        }
    }


    @ApiOperation(value="商品小程序码", notes="生成小程序码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applicationId", value = "应用id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "goodsId", value = "商品id", required = true, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "token", value = "经过urlEncoder的token", required = true, dataType = "String", paramType = "query")
    })
    @GetMapping("/applet/code/goods")
    public void GenerateMiniCode(@RequestParam Long applicationId,
                                 @RequestParam Long goodsId,
                                 @RequestParam(required = false) String token,
                                 HttpServletResponse response)throws Exception {
        imageService.prepareApplicationContext(applicationId);
        String scene=imageService.createScene(goodsId,applicationId,token);
        String appId= CurrentApplicationUtil.getMerchantApplication().getAppId();
        WxMaQrcodeService wxMaQrcodeService=WxMaConfiguration.getMaService(appId). getQrcodeService();
        byte[] bytes= wxMaQrcodeService.createWxaCodeUnlimitBytes(scene,"pages/serverDetail/index",280, true, null, false);
        BufferedImage image= ImageIO.read(new ByteArrayInputStream(bytes));
        if (image!=null){
            ImageIO.write(image,"jpg",response.getOutputStream());
        }
    }
}
