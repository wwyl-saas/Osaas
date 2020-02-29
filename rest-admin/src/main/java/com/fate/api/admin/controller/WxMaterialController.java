package com.fate.api.admin.controller;

import com.fate.api.admin.exception.BaseException;
import com.fate.common.cons.Const;
import com.fate.common.enums.ResponseInfo;
import com.fate.common.model.StandardResponse;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @program: parent
 * @description: 微信素材管理
 * @author: chenyixin
 * @create: 2019-09-06 14:42
 **/
@Api(value = "API -Material ", description = "微信公众号素材管理相关接口")
@RestController
@RequestMapping("/api/wx/material")
@Slf4j
public class WxMaterialController {
    @Autowired
    WxMpService wxMpService;
    /**
     * 开发者需调用该接口上传商户图标至微信服务器，获取相应logo_url/icon_list/image_url，用于卡券创建。
     * 1.上传的图片限制文件大小限制1MB，仅支持JPG、PNG格式。
     * 2.调用接口获取图片url仅支持在微信相关业务下使用。
     * @return
     */
    @PostMapping("/uploadimg")
    public StandardResponse uploading(@RequestParam("file") MultipartFile file) throws IOException, WxErrorException {
        long maxFileSize = 15 * 1024 * 1024;//5MB
        if (file == null || file.getSize() > maxFileSize) {
            throw new BaseException(ResponseInfo.IMAGE_TOO_LARGE_1M);
        }
        File targetFile = new File(Const.TEMP_PATH+file.getOriginalFilename());
        FileUtils.copyInputStreamToFile(file.getInputStream(), targetFile);
        WxMediaImgUploadResult wxMediaImgUploadResult=wxMpService.getMaterialService().mediaImgUpload(targetFile);
        return StandardResponse.success(wxMediaImgUploadResult.getUrl());
    }
}
