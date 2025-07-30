package com.imooc.miaoshaproject.pdf;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PdfReaderUltimate {

    /**
     * 读取PDF基础文本内容
     */
    public static String readPdfText(String filePath) {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            // 创建PDF文本提取器
            PDFTextStripper stripper = new PDFTextStripper();

            // 设置提取配置
            // 按页面位置排序
            stripper.setSortByPosition(true);
            // 不跳过重叠文本
            stripper.setSuppressDuplicateOverlappingText(false);
            // 设置行分隔符
            stripper.setLineSeparator("\n");

            // 设置提取范围（例如：只提取前5页）
            stripper.setStartPage(1);
            stripper.setEndPage(2);
            //stripper.setEndPage(Math.min(5, document.getNumberOfPages()));

            return stripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 高级：逐字提取文本（保留位置信息）
     */
    public static List<TextPosition> extractTextPositions(String filePath) {
        List<TextPosition> allTextPositions = new ArrayList<>();

        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void writeString(String text, List<TextPosition> textPositions) {
                    // 收集当前行所有文本位置
                    allTextPositions.addAll(textPositions);
                }
            };

            stripper.getText(document);
            return allTextPositions;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提取特定区域的文本
     */
    public static String extractAreaText(String filePath, Rectangle rect, int pageNum) {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            // 创建区域文本提取器
            PDFTextStripperByArea stripper = new PDFTextStripperByArea();

            // 设置区域（矩形区域：x, y, width, height）
            stripper.addRegion("customArea", rect);

            // 获取指定页面
            PDPageTree pages = document.getPages();
            PDPage page = pages.get(pageNum - 1);

            // 提取区域文本
            stripper.extractRegions(page);
            return stripper.getTextForRegion("customArea");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提取PDF元数据
     */
    public static void printPdfMetadata(String filePath) {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            PDDocumentInformation info = document.getDocumentInformation();

            System.out.println("==== PDF 元数据 ====");
            System.out.println("标题: " + info.getTitle());
            System.out.println("作者: " + info.getAuthor());
            System.out.println("主题: " + info.getSubject());
            System.out.println("创建者: " + info.getCreator());
            System.out.println("生产者: " + info.getProducer());
            System.out.println("创建日期: " + info.getCreationDate());
            System.out.println("修改日期: " + info.getModificationDate());
            System.out.println("页数: " + document.getNumberOfPages());
            System.out.println("是否加密: " + document.isEncrypted());

            AccessPermission permission = document.getCurrentAccessPermission();
            System.out.println("打印权限: " + permission.canPrint());
            System.out.println("复制权限: " + permission.canExtractContent());
            System.out.println("修改权限: " + permission.canModify());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将PDF页面渲染为图片
     */
    public static void renderPdfToImage(String filePath, int pageNum, String outputPath, int dpi) {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            PDFRenderer renderer = new PDFRenderer(document);

            // 获取指定页面索引（0-based）
            int pageIndex = pageNum - 1;

            // 以高DPI渲染
            BufferedImage image = renderer.renderImageWithDPI(pageIndex, dpi);

            // 保存为PNG文件
            String outputFile = outputPath.endsWith(".png") ?
                    outputPath : outputPath + ".png";
            ImageIO.write(image, "PNG", new File(outputFile));

            System.out.println("页面已保存为: " + outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理中文字符编码
     */
    public static String readChineseText(String filePath) throws Exception {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            // 自定义文本提取器处理中文编码
            PDFTextStripper stripper = new PDFTextStripper() {
                @Override
                protected void processTextPosition(TextPosition text) {
                    // 特别处理中文编码
                    String unicodeText = text.getUnicode();

                    // 特殊字符处理
                    if (unicodeText == null || unicodeText.isEmpty()) {
                        // 尝试使用字体解码
                        try {
                            String decoded = new String(
                                    "UTF-8"
                            );
                            writeString(decoded);
                        } catch (Exception e) {
                            // 使用后备方案
                            try {
                                writeString(text.toString());
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    } else {
                        try {
                            writeString(unicodeText);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            };

            return stripper.getText(document);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提取PDF中的超链接
     */
    public static void extractHyperlinks(String filePath) {
        try (PDDocument document = Loader.loadPDF(new File(filePath))) {
            for (PDPage page : document.getPages()) {
                // 获取页面中的所有注释（包括超链接）
                page.getAnnotations().forEach(annotation -> {
                    // 处理链接类型注解
                    if (annotation.getSubtype().equals("Link")) {
                        System.out.println("在页面 " + (document.getPages().indexOf(page) + 1) + " 找到超链接");
                        System.out.println("链接位置: " + annotation.getRectangle().toString());
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "src/main/resources/test1.pdf";

//        // 1. 基本文本提取
//        System.out.println("==== 基本文本提取 ====");
//        System.out.println(readPdfText(filePath));

//        // 2. 元数据提取
//        System.out.println("\n==== 元数据提取 ====");
//        printPdfMetadata(filePath);
//
//        // 3. 特定区域文本提取（例如左上角300x100区域）
//        System.out.println("\n==== 特定区域文本 ====");
//        Rectangle headerRect = new Rectangle(0, 0, 300, 100);
//        System.out.println(extractAreaText(filePath, headerRect, 1));
//
//        // 4. 处理中文文档
//        System.out.println("\n==== 中文文档处理 ====");
//        System.out.println(readChineseText(filePath));
//
//        // 5. 转换为图片
//        System.out.println("\n==== 页面渲染 ====");
//        renderPdfToImage(filePath, 1, "output/page1", 150);
//
//        // 6. 超链接提取
//        System.out.println("\n==== 超链接提取 ====");
//        extractHyperlinks(filePath);
    }
}
