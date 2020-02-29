package com.fate.api.merchant.util.Excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;


import java.io.OutputStream;
import java.util.List;

public class ExcelWriteUtils {

    /**
     * 将数据写到excel中的第一个sheet页
     * @param fileName
     * @param clazz
     * @param list
     */
    public static void writeSimpleFileToFileName(String fileName, Class clazz, List<?> list){
        EasyExcel.write(fileName, clazz).sheet("模板").doWrite(list);

    }

    /**
     * 将数据写到excel中的第一个sheet页
     * @param outputStream
     * @param clazz
     * @param list
     */
    public static void writeSimpleFileByToOutputStream(OutputStream outputStream, Class clazz, List<?> list){
        EasyExcel.write(outputStream, clazz).sheet("模板").doWrite(list);

    }

    /**
     * 指定写入的列  写入到文件  写入到输出流
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 使用{@link ExcelProperty}注解指定写入的列
     * <p>
     * 3. 直接写即可
     */
    public  static void indexWriteToFile(String fileName, Class clazz, List<?> list){
        EasyExcel.write(fileName, clazz).sheet("模板").doWrite(list);

    }


    public static void indexWriteToOutputStream(OutputStream outputStream, Class clazz, List<?> list){
        EasyExcel.write(outputStream, clazz).sheet("模板").doWrite(list);

    }

    /**
     * 复杂头写入
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 使用{@link ExcelProperty}注解指定复杂的头
     * <p>
     * 3. 直接写即可
     */
    public static void complexHeadWriteToFile(String fileName,Class clazz, List<?> list) {
        //String fileName = TestFileUtil.getPath() + "complexHeadWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, clazz).sheet("模板").doWrite(list);
    }

    /**
     * 复杂头写入
     * <p>
     * 1. 创建excel对应的实体对象 参照{ComplexHeadData}
     * <p>
     * 2. 使用{@link ExcelProperty}注解指定复杂的头
     * <p>
     * 3. 直接写即可
     */
    public static void complexHeadWriteToOutputStream(String fileName,Class clazz, List<?> list) {
        //String fileName = TestFileUtil.getPath() + "complexHeadWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, clazz).sheet("模板").doWrite(list);
    }


    /**
     * 日期、数字或者自定义格式转换
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link }
     * <p>
     * 2. 使用{@link ExcelProperty}配合使用注解{@link DateTimeFormat}、{@link NumberFormat}或者自定义注解
     * <p>
     * 3. 直接写即可
     */
    public static void converterWriteToFile(String fileNme,Class converterClass,List<?> list) {

        // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileNme, converterClass).sheet("模板").doWrite(list);
    }


    /**
     * 自定义样式
     * <p>
     * 1. 创建excel对应的实体对象 参照{ DemoData}
     * <p>
     * 2. 创建一个style策略 并注册
     * <p>
     * 3. 直接写即可
     */
    public static void styleWriteToFile(String fileName,Class clazz,List<?> list) {
       // String fileName = TestFileUtil.getPath() + "styleWrite" + System.currentTimeMillis() + ".xlsx";
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)20);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short)20);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy horizontalCellStyleStrategy =
                new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        // 这里 需要指定写用哪个class去读，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName,clazz).registerWriteHandler(horizontalCellStyleStrategy).sheet("模板")
                .doWrite(list);
    }












}
