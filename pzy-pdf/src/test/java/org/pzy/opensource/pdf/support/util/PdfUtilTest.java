package org.pzy.opensource.pdf.support.util;

import org.junit.Test;
import org.pzy.opensource.comm.util.InputStreamUtil;
import org.pzy.opensource.pdf.domain.bo.FontInfoBO;
import org.pzy.opensource.pdf.domain.bo.StampMarkConfigBO;
import org.pzy.opensource.pdf.domain.bo.TextMarkConfigBO;
import org.pzy.opensource.spring.util.ResourceFileLoadUtil;

import java.io.*;
import java.util.Arrays;

/**
 * PdfUtilTest
 *
 * @author pan
 * @date 5/30/20
 */
public class PdfUtilTest {

    @Test
    public void html2Pdf() throws IOException {
        File htmlFile = ResourceFileLoadUtil.loadAsFile("classpath:source.html");
        OutputStream pdfOutputStream = null;
        try {
            pdfOutputStream = new FileOutputStream(new File("target.pdf"));
            PdfUtil.html2Pdf(htmlFile, pdfOutputStream, Arrays.asList(new FontInfoBO("font/simsun.ttf"), new FontInfoBO("font/SourceHanSansCN-Regular.ttf", "SourceHanSansCN")));
        } finally {
            if (null != pdfOutputStream) {
                pdfOutputStream.close();
            }
        }
    }

    @Test
    public void addPdfMark() throws IOException {
        InputStream pdfInputStream = ResourceFileLoadUtil.loadAsInputStream("classpath:source.pdf");
        InputStream imageInputStream = ResourceFileLoadUtil.loadAsInputStream("classpath:water.jpg");
        OutputStream outputStream = null;
        TextMarkConfigBO textMarkConfig = null;
        try {
            outputStream = new FileOutputStream(new File("new-target.pdf"));
            textMarkConfig = new TextMarkConfigBO(new FontInfoBO("font/simsun.ttf"), "测试用例");
            PdfUtil.addTextMark(pdfInputStream, outputStream, textMarkConfig);
        } finally {
            outputStream.close();
            InputStreamUtil.closeInputStream(imageInputStream);
        }
    }

    @Test
    public void addStampMark() throws IOException {
        InputStream pdfInputStream = ResourceFileLoadUtil.loadAsInputStream("classpath:source.pdf");
        OutputStream outputStream = null;
        StampMarkConfigBO stampMarkConfigBO = null;

        outputStream = new FileOutputStream(new File("new-image-target.pdf"));
        stampMarkConfigBO = new StampMarkConfigBO(ResourceFileLoadUtil.loadAsInputStream("classpath:water.jpg"), 335, 630);
        stampMarkConfigBO.setOnlyLastPage(false);
        PdfUtil.addStampMark(pdfInputStream, outputStream, stampMarkConfigBO);
    }
}