package com.fate.api.merchant.util.Excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;


import java.io.*;


public class ExcelReadUtils {
    /**
     * 简单读取excel中第一个sheet页数据并存储数据库
     * @param fileName
     * @param clazz
     * @param analysisEventListener
     */
   public static void simpleReadByFileName(String fileName, Class clazz, AnalysisEventListener analysisEventListener){
        EasyExcel.read(fileName,clazz, analysisEventListener).sheet().doRead();
    }

    /**
     * 简单读取excel中第一个sheet页数据并存储数据库
     * @param inputStream
     * @param clazz
     * @param analysisEventListener
     */
    public static void simpleReadByOutputStream(InputStream inputStream, Class clazz, AnalysisEventListener analysisEventListener){
        EasyExcel.read(inputStream,clazz, analysisEventListener).sheet().doRead();
    }

    /**
     * 通过列名或者sheet中列索引读取sheet
     * @param fileName
     * @param clazz
     * @param analysisEventListener
     */
    public static void indexOrNameReadByFileName(String fileName,Class clazz,AnalysisEventListener analysisEventListener) {

        // 这里默认读取第一个sheet
        EasyExcel.read(fileName, clazz, analysisEventListener).sheet().doRead();
    }

    /**
     * 通过列名或者sheet中列索引读取sheet
     * @param inputStream
     * @param clazz
     * @param analysisEventListener
     */
    public static void indexOrNameReadByFileNameByOutputStream(InputStream inputStream, Class clazz, AnalysisEventListener analysisEventListener) {
        // 这里默认读取第一个sheet
        EasyExcel.read(inputStream, clazz, analysisEventListener).sheet().doRead();
    }






}
