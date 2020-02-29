package com.fate.api.merchant.controller;

import com.fate.api.merchant.util.Excel.*;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @program: parent
 * @description:
 * @create: 2019-08-06 15:44
 **/
@Api(value = "API - upload", description = "上传Excel或者下载")
@RestController
@RequestMapping("/api/excel/")
@Slf4j
public class WebExcelController {
    /**
     * 文件下载
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link DownloadData}
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @GetMapping("download")
    public void downloadExcel(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应下载的文件名不对。这个时候 请别使用swagger 他会有影响
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        Class clazz=DownloadData.class;
        String fileName = URLEncoder.encode("测试", "UTF-8");
        List<?> list=data();
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        ExcelWriteUtils.writeSimpleFileByToOutputStream(response.getOutputStream(), clazz,list);
       // EasyExcel.write(response.getOutputStream(), clazz).sheet("模板").doWrite(list);
    }

    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link UploadData}
     * <p>
     * 2. 由于默认异步读取excel，所以需要创建excel一行一行的回调监听器，参照{@link UploadDataListener}
     * <p>
     * 3. 直接读即可
     */
    @PostMapping("upload")
    @ResponseBody
    public String uploadExcel(MultipartFile file) throws IOException {
        Class clazz=UploadData.class;
        ExcelReadUtils.simpleReadByOutputStream(file.getInputStream(),clazz,new UploadDataListener());
        //EasyExcel.read(file.getInputStream(), UploadData.class, new UploadDataListener()).sheet().doRead();
        return "success";
    }

    private List<DownloadData> data() {
        List<DownloadData> list = new ArrayList<DownloadData>();
        for (int i = 0; i < 10; i++) {
            DownloadData data = new DownloadData();
            data.setString("字符串" + 0);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }



}
