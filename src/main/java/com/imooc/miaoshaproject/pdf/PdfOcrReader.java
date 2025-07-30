package com.imooc.miaoshaproject.pdf;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/30
 */
import net.sourceforge.tess4j.*;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PdfOcrReader {

    /**
     * 从PDF提取文字内容
     *
     * @param pdfPath PDF文件路径
     * @param outputDir 临时图像输出目录
     * @param language 识别语言（如"eng","chi_sim"）
     * @return 识别后的完整文本
     */
    public static String extractTextFromPdf(String pdfPath, String outputDir, String language, int pageStart, int pageSize) {
        List<File> imageFiles = new ArrayList<>();
        StringBuilder result = new StringBuilder();

        try {
            // 1. 加载PDF文档
            PDDocument document = Loader.loadPDF(new File(pdfPath));
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            int pageEnd = Math.min(pageStart + pageSize, document.getNumberOfPages());

            // 2. 逐页转换为图像
            for (; pageStart < pageEnd; pageStart++) {
                // 设置DPI提高识别准确率（建议300-400），pdf字体不能太小，不然识别不到
                BufferedImage image = pdfRenderer.renderImageWithDPI(pageStart, 300, ImageType.RGB);

                // 保存为临时图像文件（可选）
                File tempImage = new File(outputDir, "page_" + (pageStart+1) + ".png");
                javax.imageio.ImageIO.write(image, "png", tempImage);
                imageFiles.add(tempImage);

                // 3. 执行OCR识别
                String pageText = doOcr(tempImage, language);
                System.out.println("pageText:"+pageText);
                result.append("--- 第 ").append(pageStart+1).append(" 页 ---\n")
                        .append(pageText).append("\n\n");
            }

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("PDF处理失败", e);
        } finally {
            // 清理临时文件（根据需求保留）
            deleteTempFiles(imageFiles);
        }

        return result.toString();
    }

    /**
     * 执行OCR识别
     */
    private static String doOcr(File imageFile, String language) throws TesseractException {
        ITesseract tesseract = new Tesseract();

        // 设置语言包位置（若在默认路径可省略）需要安装Tesseract-OCR引擎，https://github.com/UB-Mannheim/tesseract/wiki
        tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");

        // 设置识别语言（多语言用"+"连接）
        tesseract.setLanguage(language);

        // 优化识别配置
        tesseract.setPageSegMode(ITessAPI.TessPageSegMode.PSM_AUTO);
        tesseract.setOcrEngineMode(ITessAPI.TessOcrEngineMode.OEM_LSTM_ONLY);

        // 设置白名单（仅识别特定字符，提高准确率）
        // tesseract.setTessVariable("tessedit_char_whitelist", "0123456789");

        // 执行OCR
        return tesseract.doOCR(imageFile);
    }

    /**
     * 清理临时图像文件
     */
    private static void deleteTempFiles(List<File> files) {
        for (File file : files) {
            if (file.exists()) file.delete();
        }
    }
}
