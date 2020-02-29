package com.fate.api.merchant.controller;

import com.fate.common.model.StandardResponse;
import com.fate.common.util.QiNiuUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @program: parent
 * @description: 上传图片及文件
 * @author: chenyixin
 * @create: 2019-08-06 15:44
 **/
@Api(value = "API - upload", description = "上传图片及文件")
@RestController
@RequestMapping("/api/upload")
@Slf4j
public class UploadController {
    /**
     *
     * @param file
     * @return
     */
    @ApiOperation(value="上传图片", notes="参数为文件")
    @ApiImplicitParam(name = "file", value = "图片文件", required = true, dataType = "CommonsMultipartFile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping(value = "/img")
    public StandardResponse uploadFeedBackFile(@RequestParam("file") MultipartFile file) throws IOException {
        String path= QiNiuUtil.uploadInputStream(file.getInputStream());
        return StandardResponse.success(path);
    }

    /**
     *删除单个文件
     * @param hash
     * @return
     */
    @ApiOperation(value="删除图片", notes="参数为文件hash值")
    @ApiImplicitParam(name = "hash", value = "图片hash值", required = true, dataType = "String")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful — 请求已完成"),
            @ApiResponse(code = 404, message = "服务器找不到给定的资源；文档不存在"),
            @ApiResponse(code = 500, message = "服务器不能完成请求")}
    )
    @PostMapping(value = "/delete")
    public StandardResponse deleteOneFile(String hash) throws IOException {
        Assert.notNull(hash,"参数不能为空！");
        Assert.isTrue(QiNiuUtil.deleteFile(hash),"删除失败！");
        return StandardResponse.success();
    }



}
