package com.fate.common.util;

import com.alibaba.fastjson.JSON;
import com.fate.common.cons.Const;
import com.fate.common.enums.QiNiuUploadType;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;


import java.io.*;


/**
 * @program: parent
 * @description: 七牛云工具
 * @author: chenyixin
 * @create: 2019-06-08 22:52
 **/
@Slf4j
public class QiNiuUtil {
    private static String BUCKET = "image";
    private static String accessKey = "cqnXD4S10vAJmsOD4uqyVZRUt2AOEekp98cPAcgs";
    private static String secretKey = "oLrP0z-Vy4KV02uOABckYgXd7TJzy2fnAEO-I_yB";
    private static UploadManager uploadManager;
    private static BucketManager bucketManager;
    private static Auth auth;
    private static Configuration cfg;

    static {
        cfg = new Configuration(Region.autoRegion());
        uploadManager = new UploadManager(cfg);
        auth = Auth.create(accessKey, secretKey);
        bucketManager = new BucketManager(auth, cfg);
    }


    /**
     * 上传文件到七牛云
     *
     * @param localFilePath
     * @return
     */
    public static String uploadFile(String localFilePath) {
        try {
            return upload(localFilePath,QiNiuUploadType.PATH);
        } catch (QiniuException ex) {
            log.error("文件上传CDN出错", ex);
            return null;
        } catch (Exception e) {
            log.error("返回结果异常", e);
            return null;
        }
    }

    /**
     * 上传字节数组到七牛云
     *
     * @param array
     * @return
     */
    public static String uploadByteArray(byte[] array) {
        try {
            return upload(array,QiNiuUploadType.ARRAY);
        } catch (QiniuException ex) {
            log.error("文件上传CDN出错", ex);
            return null;
        } catch (Exception e) {
            log.error("返回结果异常", e);
            return null;
        }
    }

    /**
     * 上传输入流到七牛云
     *
     * @param inputStream
     * @return
     */
    public static String uploadInputStream(InputStream inputStream) {
        try {
            return upload(inputStream,QiNiuUploadType.STREAM);
        } catch (QiniuException ex) {
            log.error("文件上传CDN出错", ex);
            return null;
        } catch (Exception e) {
            log.error("返回结果异常", e);
            return null;
        }
    }

    /**
     * 上传
     * @param data
     * @param type
     * @return
     * @throws QiniuException
     */
    private static String upload(Object data, QiNiuUploadType type) throws QiniuException {
        String upToken = auth.uploadToken(BUCKET);
        Response response =null;
        switch (type){
            case PATH:
                response = uploadManager.put((String) data, null, upToken);
                break;
            case ARRAY:
                response = uploadManager.put((byte[])data, null, upToken);
                break;
            case STREAM:
                response = uploadManager.put((InputStream) data,null,upToken,null, null);
                break;
        }
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        log.info("七牛云返回：" + JSON.toJSONString(putRet));
        return putRet.hash;
    }


    /**
     * 删除CDN文件
     * @param hash
     * @return
     */
    public static boolean deleteFile(String hash){
        try {
            bucketManager.delete(BUCKET, hash);
            return true;
        } catch (QiniuException ex) {
            log.error("删除CDN文件出错", ex);
            return false;
        }
    }

    /**
     * 批量删除CDN文件(七牛云对同一张图片多次上传进行了聚合，删除一处会导致多处全部删除)
     * @param hashs
     * @return
     */
    public static boolean deleteFileList(String[] hashs){
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(BUCKET, hashs);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < hashs.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = hashs[i];
                if (status.code == 200) {
                    log.info("key = {} delete success",key);
                } else {
                    log.error("key="+key+" ,"+status.data.error);
                }
            }
            return true;
        } catch (QiniuException ex) {
            log.error("删除CDN文件出错", ex);
            return false;
        }
    }

    /**
     * 下载文件为字节流（临时目录，用完关闭字节流）
     * @param url
     * @param isPrivate
     * @return
     */
    public static InputStream downLoadToStream(String url,boolean isPrivate){
        String finalUrl=url;
        if (isPrivate){
            long expireInSeconds = 360;//6分钟，可以自定义链接过期时间
            finalUrl = auth.privateDownloadUrl(url, expireInSeconds);
        }
        OkHttpClient okHttpClient=new OkHttpClient.Builder().build();
        Request req = new Request.Builder().url(finalUrl).build();
        try {
            okhttp3.Response resp = okHttpClient.newCall(req).execute();
            if (resp.isSuccessful()) {
                ResponseBody body = resp.body();
                return body.byteStream();
            }else {
                log.error("下载请求失败:{}",resp.toString());
            }
        } catch (IOException e) {
            log.error("下载请求报错:",e);
        }
        return null;
    }

    /**
     * 返回本地文件路径（临时目录，用完请删除）
     * @param url
     * @param isPrivate
     * @return
     */
    public static File downLoadToFile(String url,boolean isPrivate){
        String filePath= Const.TEMP_PATH;
        File file = new File(filePath);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdir();
        }
        File tempFile = new File(filePath +  System.currentTimeMillis());
        InputStream in =downLoadToStream(url,isPrivate);
        try {
            byte [] bytes =IOUtils.toByteArray(in);
            FileUtils.writeByteArrayToFile(tempFile,bytes);
        } catch (IOException e) {
            log.error("下载文件报错",e);
        }finally {
            try {
                in.close();
            } catch (IOException e) {
                log.error("关闭流失败",e);
            }
        }
        return tempFile;

    }

    /**
     * 对上传的图片设置过期时间
     * @param key
     * @param days 单位天
     */
    public static void deleteAfterDays(String key,int days){
        try {
            bucketManager.deleteAfterDays(BUCKET,key,days);
        } catch (QiniuException e) {
            e.printStackTrace();
            log.error("删除文件报错",e);
        }
    }



    public static void main(String[] args) throws UnsupportedEncodingException {
//        String hash=uploadFile("c:\\ftp\\favicon.jpg");
//        log.info("返回结果"+hash);
//        byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
//        String hash1=uploadByteArray(uploadBytes);
//        log.info("返回结果"+hash1);
//        ByteArrayInputStream byteInputStream=new ByteArrayInputStream(uploadBytes);
//        String hash2=uploadInputstream(byteInputStream);
//        log.info("返回结果"+hash2);

//        String[] keyList = new String[]{
//                "FoEib1YNIh0VYNz0WMl9-JwXU1-J",
//                "Fu3P9AgpOo_0q_4KnJdn9ZXVimfD"
//        };
//        boolean flag=deleteFileList(keyList);
//        log.info(""+flag);


    }

}
