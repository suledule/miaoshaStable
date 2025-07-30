package com.imooc.miaoshaproject.pdf;

import org.junit.Test;

/**
 * @Description
 * @Author wuqiusheng
 * @Version 1.0
 * @Date 2025/7/30
 */
public class PdfTest {

    private final static String PATH = "C:\\Users\\suledule\\Desktop\\吴秋生\\长亮文档\\test2.pdf";

    private final static String OUTPUT_PATH = "src/main/resources";

    @Test
    public void pdfTest1() {
        System.out.println(PdfReaderUltimate.readPdfText(PATH));
    }

    @Test
    public void pdfTest2() {
        System.out.println(PdfReaderUltimate.readPdfText(PATH));
    }

    @Test
    public void pdfTest11() {
        PdfOcrReader.extractTextFromPdf("src/main/resources/test.pdf", OUTPUT_PATH
                , "chi_sim", 23, 24);
    }
}
