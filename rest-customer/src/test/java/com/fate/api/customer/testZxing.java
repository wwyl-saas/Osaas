package com.fate.api.customer;

import com.fate.common.util.QRAndBarCodeUtil;

import java.io.File;

public class testZxing {

    public static void main(String[] args)throws Exception {
        String dir = "D:/2.jpg";
        String content = "https://www.baidu.com";
        String logoImgPath = "C:/Users/Rex/Desktop/logo.jpg";
        File file = new File(dir);
        QRAndBarCodeUtil.encode(content, dir);
       //QRAndBarCodeUtil.encode(content, new FileOutputStream(file));
      //QRAndBarCodeUtil.encode(content, logoImgPath, dir, false);
     //QRAndBarCodeUtil.encode(content, logoImgPath, dir, true);
     //QRAndBarCodeUtil.encode(content, logoImgPath, new FileOutputStream(file), false);
     // QRAndBarCodeUtil.encode(content, logoImgPath, new FileOutputStream(file), true);
    }
}
