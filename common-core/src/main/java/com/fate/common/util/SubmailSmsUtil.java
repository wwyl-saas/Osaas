package com.fate.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fate.common.enums.SmsEnum;
import com.fate.common.util.http.OkResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @program: parent
 * @description: 邮赛短信工具
 * @author: chenyixin
 * @create: 2019-09-11 19:16
 **/
@Slf4j
public class SubmailSmsUtil {
    public static final String APPID="40777";
    public static final String APPKEY="3c4fd2595d72ec45563538f857759c0e";
    /**
     * 时间戳接口配置
     */
    public static final String TIMESTAMP = "https://api.mysubmail.com/service/timestamp";
    private static final String SEND_SMS="https://api.mysubmail.com/message/send.json";


    /**
     * 创建SHA1签名
     * @return SHA1签名
     */
    public static String createSubmailSignature(Map<String,String> paramMap) {
        String paramString= SignUtils.sortParams(paramMap);
        StringBuilder signParams=new StringBuilder().append(APPID).append(APPKEY)
                .append(paramString).append(APPID).append(APPKEY);
        System.out.println(signParams);
        return SignUtils.sign(SignUtils.TYPE_SHA1,signParams.toString());
    }


    /**
     * 发送单独短信
     * @param mobile
     * @param content
     */
    public static void sendSafeSms(String mobile,String content){
        Map<String,String> params=new HashMap<>();
        params.put("appid",APPID);
        params.put("to",mobile);
        params.put("content",content);
        params.put("timestamp",getTimestamp());
        params.put("sign_type",SignUtils.TYPE_SHA1);
        params.put("signature",createSubmailSignature(params));

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        ContentType contentType = ContentType.create("text/plain", "UTF-8");
        Iterator iterator = params.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Object> entry = (Map.Entry)iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            builder.addTextBody(key, String.valueOf(value), contentType);
        }

        HttpPost httpPost = new HttpPost(SEND_SMS);
        httpPost.addHeader("charset", "UTF-8");
        httpPost.setEntity(builder.build());

        try {
            CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
            HttpResponse response = closeableHttpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if (httpEntity != null) {
                String jsonStr = EntityUtils.toString(httpEntity, "UTF-8");
                JSONObject jsonObject=JSON.parseObject(jsonStr);
                Assert.isTrue(jsonObject.getString("status").equals("success"),"submail发送短信失败，result:"+jsonStr);
                log.info("{}短信{}发送成功",mobile,content);
            }
        } catch (ClientProtocolException e) {
            log.error("发送短信失败1,mobile:{},content:{}",mobile,content,e);
        } catch (IOException e) {
            log.error("发送短信失败2,mobile:{},content:{}",mobile,content,e);
        }catch (Exception e){
            log.error("发送短信失败3,mobile:{},content:{}",mobile,content,e);
        }
    }

    /**
     * 发送单独短信
     * @param mobile
     * @param content
     */
    @Deprecated
    public static void sendSms(String mobile,String content){
        Map<String,String> params=new HashMap<>();
        params.put("appid",APPID);
        params.put("to",mobile);
        params.put("content",content);
        params.put("signature",APPKEY);

        OkResponseResult result=OkHttpUtil.getIntance().doPost(SEND_SMS,new HashMap<>(),params,"vcode");
        if (result.isSuccessful()){
            JSONObject jsonObject=JSON.parseObject(result.getResponseBody());
            Assert.isTrue(jsonObject.getString("status").equals("success"),"submail发送短信失败，result:"+result.getResponseBody());
        }else{
            log.error("发送短信失败,mobile:{},content:{}",mobile,content);
        }
    }


    /**
     * 获取时间戳
     * @return
     */
    private static String getTimestamp(){
        String timestamp="";
        OkResponseResult result=OkHttpUtil.getIntance().doGet(TIMESTAMP,new HashMap<>());
        if (result.isSuccessful()){
            JSONObject json = JSON.parseObject(result.getResponseBody());
            timestamp= json.getString("timestamp");
        }else{
            log.error("submail报错:",result.getResponseBody());
        }
        return timestamp;
    }

    public static void main(String[] args) {
        Map<String,Object> paramap=new HashMap<>();
        paramap.put("code",111111);
        paramap.put("minutes",5);
        String content=TemplateUtil.render(SmsEnum.CAPTCHA.getContext(),paramap);
        sendSafeSms("17710800540",content);
    }

}
