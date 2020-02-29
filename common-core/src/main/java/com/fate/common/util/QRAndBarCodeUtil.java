package com.fate.common.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Slf4j
public class QRAndBarCodeUtil {
	private static final String CHARSET = "UTF-8";  
    private static final String FORMAT_NAME = "JPG";  
    // 二维码尺寸  
    private static final int QRCODE_SIZE = 200;  
    // LOGO宽度  
    private static final int WIDTH = 60;  
    // LOGO高度  
    private static final int HEIGHT = 60; 
    
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:31:29
     * @param content 二维码内容
     * @param logoImgPath Logo
     * @param needCompress 是否压缩Logo
     * @return 返回二维码图片
     * @throws WriterException
     * @throws IOException
     * BufferedImage
     */
    public static BufferedImage createImage(String content, String logoImgPath, boolean needCompress) throws WriterException, IOException {
    	Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);  
        hints.put(EncodeHintType.MARGIN, 1);  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);  
        int width = bitMatrix.getWidth();  
        int height = bitMatrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);  
            }  
        }  
        if (logoImgPath == null || "".equals(logoImgPath)) {  
            return image;  
        }  
        
        // 插入图片  
        insertImage(image, logoImgPath, needCompress);
        return image;  
    }
    
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:30:09
     * @param source 二维码图片
     * @param logoImgPath Logo
     * @param needCompress 是否压缩Logo
     * @throws IOException
     * void
     */
    private static void insertImage(BufferedImage source, String logoImgPath, boolean needCompress) throws IOException{
    	File file = new File(logoImgPath);  
        if (!file.exists()) {  
            return;  
        }  
        
        Image src = ImageIO.read(new File(logoImgPath));  
        int width = src.getWidth(null);  
        int height = src.getHeight(null);  
        if (needCompress) { // 压缩LOGO  
            if (width > WIDTH) {  
                width = WIDTH;  
            }  
            
            if (height > HEIGHT) {  
                height = HEIGHT;  
            }  
            
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);  
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            Graphics g = tag.getGraphics();  
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();  
            src = image;  
        }  
        
        // 插入LOGO  
        Graphics2D graph = source.createGraphics();  
        int x = (QRCODE_SIZE - width) / 2;  
        int y = (QRCODE_SIZE - height) / 2;  
        graph.drawImage(src, x, y, width, height, null);  
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);  
        graph.setStroke(new BasicStroke(3f));  
        graph.draw(shape);  
        graph.dispose();  
    }
    
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:32:32
     * @param content 二维码内容
     * @param logoImgPath Logo
     * @param destPath 二维码输出路径
     * @param needCompress 是否压缩Logo
     * @throws Exception
     * void
     */
    public static void encode(String content, String logoImgPath, String destPath, boolean needCompress) throws Exception {  
        BufferedImage image = QRAndBarCodeUtil.createImage(content, logoImgPath, needCompress);
        mkdirs(destPath);
        ImageIO.write(image, FORMAT_NAME, new File(destPath));  
    }   
    
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:35:44
     * @param content 二维码内容
     * @param destPath 二维码输出路径
     * @throws Exception
     * void
     */
    public static void encode(String content, String destPath) throws Exception {  
        QRAndBarCodeUtil.encode(content, null, destPath, false);
    }  
    
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:36:58
     * @param content 二维码内容
     * @param logoImgPath Logo
     * @param output 输出流
     * @param needCompress 是否压缩Logo
     * @throws Exception
     * void
     */
    public static void encode(String content, String logoImgPath, OutputStream output, boolean needCompress) throws Exception {  
        BufferedImage image = QRAndBarCodeUtil.createImage(content, logoImgPath, needCompress);
        ImageIO.write(image, FORMAT_NAME, output);  
    }  
    
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:38:02
     * @param content 二维码内容
     * @param output 输出流
     * @throws Exception
     * void
     */
    public static void encode(String content, OutputStream output) throws Exception {  
        QRAndBarCodeUtil.encode(content, null, output, false);
    }  
    
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:39:10
     * @param file 二维码
     * @return 返回解析得到的二维码内容
     * @throws Exception
     * String
     */
    public static String decode(File file) throws Exception {  
        BufferedImage image;  
        image = ImageIO.read(file);  
        if (image == null) {  
            return null;  
        }  
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);  
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
        Result result;  
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();  
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);  
        result = new MultiFormatReader().decode(bitmap, hints);  
        String resultStr = result.getText();  
        return resultStr;  
    }  
    
    /**
     * user: Rex
     * date: 2016年12月29日  上午12:39:48
     * @param path 二维码存储位置
     * @return 返回解析得到的二维码内容
     * @throws Exception
     * String
     */
    public static String decode(String path) throws Exception {  
        return QRAndBarCodeUtil.decode(new File(path));
    }


    public static void mkdirs(String dir){
        if(StringUtils.isEmpty(dir)){
            return;
        }

        File file = new File(dir);
        if(file.isDirectory()){
            return;
        } else {
            file.mkdirs();
        }
    }

    /**
     * 生产CODE_128条形码
     * @param string
     * @return
     */
    public static BufferedImage getBarcode(String string){
        try {
            Code128Writer writer = new Code128Writer();
            Map<EncodeHintType,Object> hints=new HashMap<>();
            hints.put(EncodeHintType.MARGIN,0L);
            hints.put(EncodeHintType.CHARACTER_SET,"UTF-8");
            BitMatrix bar = writer.encode(string, BarcodeFormat.CODE_128, 290, 58, hints);
            bar=deleteWhite(bar);
//            MatrixToImageWriter.writeToStream(bar,"png", new FileOutputStream(new File("c://myfate/test.png")));
            return MatrixToImageWriter.toBufferedImage(bar);
        }
        catch (WriterException e){
            log.error("create bar code error",e);
            return null;
        }
    }

    /**
     * 去条形码二维码白边
     * @param matrix
     * @return
     */
    private static BitMatrix deleteWhite(BitMatrix matrix){
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2];
        int resHeight = rec[3];

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

}
