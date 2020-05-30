package org.pzy.opensource.pdf.support.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.*;
import org.pzy.opensource.comm.exception.PdfOperateException;
import org.pzy.opensource.comm.util.InputStreamUtil;
import org.pzy.opensource.pdf.domain.bo.FontInfoBO;
import org.pzy.opensource.pdf.domain.bo.StampMarkConfigBO;
import org.pzy.opensource.pdf.domain.bo.TextMarkConfigBO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * PdfUtil
 *
 * @author pan
 * @date 5/30/20
 */
public class PdfUtil {

    private PdfUtil() {
    }

    /**
     * html转pdf
     * <p><b>注意:</b> pdfOutputStream不会被自动关闭, 需要调用方自行关闭</p>
     *
     * @param htmlContent     html字符内容
     * @param pdfOutputStream pdf的输出流
     * @param fontInfoColl    html中用到的字体
     */
    public static void html2Pdf(String htmlContent, OutputStream pdfOutputStream, Collection<FontInfoBO> fontInfoColl) {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(htmlContent);
        setFontAndCreatePdf(renderer, pdfOutputStream, fontInfoColl);
    }

    /**
     * html转pdf
     * <p><b>注意:</b> pdfOutputStream不会被自动关闭, 需要调用方自行关闭</p>
     *
     * @param htmlFile        html文件
     * @param pdfOutputStream pdf的输出流
     * @param fontInfoColl    html中用到的字体
     */
    public static void html2Pdf(File htmlFile, OutputStream pdfOutputStream, Collection<FontInfoBO> fontInfoColl) {
        ITextRenderer renderer = new ITextRenderer();
        try {
            renderer.setDocument(htmlFile);
        } catch (IOException e) {
            throw new PdfOperateException("html转pdf,操作html源文件发生异常!", e);
        }
        setFontAndCreatePdf(renderer, pdfOutputStream, fontInfoColl);
    }

    /**
     * 设置字体并生成pdf
     *
     * @param renderer
     * @param pdfOutputStream pdf输出流
     * @param fontInfoColl    字体信息[可选]
     */
    private static void setFontAndCreatePdf(ITextRenderer renderer, OutputStream pdfOutputStream, Collection<FontInfoBO> fontInfoColl) {
        if (!CollectionUtils.isEmpty(fontInfoColl)) {
            ITextFontResolver fontResolver = renderer.getFontResolver();
            for (FontInfoBO fontInfoBO : fontInfoColl) {
                try {
                    if (StringUtils.isEmpty(fontInfoBO.getFontName())) {
                        fontResolver.addFont(fontInfoBO.getFontFilePath(), BaseFont.IDENTITY_H, fontInfoBO.getEmbedded());
                    } else {
                        fontResolver.addFont(fontInfoBO.getFontFilePath(), fontInfoBO.getFontName(), BaseFont.IDENTITY_H, fontInfoBO.getEmbedded(), null);
                    }
                } catch (Exception e) {
                    throw new PdfOperateException("html转pdf, 外部字体设置异常!", e);
                }
            }
        }
        renderer.layout();
        try {
            renderer.createPDF(pdfOutputStream);
        } catch (Exception e) {
            throw new PdfOperateException("html转pdf,将内容写入pdf输出流发生异常!", e);
        }
    }

    /**
     * 给pdf最后一页添加印章
     *
     * @param sourcePdfInputStream  源pdf输入流
     * @param targetPdfOutputStream 目标pdf输出流
     * @param stampMarkConfigBO     图章水印配置
     */
    public static void addStampMark(InputStream sourcePdfInputStream, OutputStream targetPdfOutputStream, StampMarkConfigBO stampMarkConfigBO) {
        PdfReader reader = null;
        PdfStamper stamper = null;
        com.itextpdf.text.Image img;
        try {
            reader = new PdfReader(sourcePdfInputStream);
            stamper = new PdfStamper(reader, targetPdfOutputStream);
            // 获取pdf总页数
            int total = reader.getNumberOfPages() + 1;
            img = com.itextpdf.text.Image.getInstance(InputStreamUtil.inputStream2byte(stampMarkConfigBO.getImageInputStream()));
            //印章位置
            img.setAbsolutePosition(stampMarkConfigBO.getX(), stampMarkConfigBO.getY());
            //印章大小
            img.scalePercent(stampMarkConfigBO.getScalePercent());
            if (stampMarkConfigBO.getOnlyLastPage()) {
                // 只在最后一页加图章水印
                PdfContentByte under = stamper.getOverContent(total-1);
                try {
                    under.addImage(img);
                } catch (DocumentException e) {
                    throw new PdfOperateException("pdf加图章水印异常!", e);
                }
            } else {
                // 每一页到加图章水印
                for (int i = 1; i < total; i++) {
                    PdfContentByte under = stamper.getOverContent(i);
                    under.addImage(img);
                }
            }
        } catch (Exception e) {
            throw new PdfOperateException("pdf加图章水印异常!", e);
        } finally {
            closeResource(reader, stamper);
        }
    }

    /**
     * 给pdf文件加水印文字
     *
     * @param sourcePdfInputStream  pdf文件
     * @param targetPdfOutputStream 水印文字文件的字节输入流
     * @param textMarkConfig        水印配置
     */
    public static void addTextMark(InputStream sourcePdfInputStream, OutputStream targetPdfOutputStream, TextMarkConfigBO textMarkConfig) {
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            reader = new PdfReader(sourcePdfInputStream);
            stamper = new PdfStamper(reader, targetPdfOutputStream);
            // 获取pdf总页数
            int total = reader.getNumberOfPages() + 1;

            BaseFont baseFont = BaseFont.createFont(textMarkConfig.getFontInfo().getFontFilePath(), BaseFont.IDENTITY_H, textMarkConfig.getFontInfo().getEmbedded());

            PdfGState gs = new PdfGState();
            gs.setFillOpacity(textMarkConfig.getWaterAlpha());
            gs.setStrokeOpacity(textMarkConfig.getWaterAlpha());

            // 计算文字的宽高
            JLabel label = new JLabel();
            label.setText(textMarkConfig.getText());
            int textH = 0;
            int textW = 0;
            FontMetrics metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());

            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                com.itextpdf.text.Rectangle rectangle = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(baseFont, textMarkConfig.getFontSize());

                int interval = -15;
                // 水印文字成20度角倾斜
                int rotation = 20;
                for (int height = interval + textH; height < rectangle.getHeight();
                     height = height + textH * 5) {
                    for (int width = interval + textW; width < rectangle.getWidth() + textW;
                         width = width + textW * 5) {
                        under.showTextAligned(Element.ALIGN_LEFT
                                , label.getText(), width - textW,
                                height - textH, rotation);
                    }
                }
                // 添加水印文字
                under.endText();
            }
        } catch (Exception e) {
            throw new PdfOperateException("pdf加图章水印异常!", e);
        } finally {
            closeResource(reader, stamper);
        }
    }

    /**
     * 资源关闭
     *
     * @param reader
     * @param stamper
     */
    private static void closeResource(PdfReader reader, PdfStamper stamper) {
        if (null != stamper) {
            try {
                stamper.close();
            } catch (Exception e) {
                throw new PdfOperateException("水印添加完毕, 关闭资源时发生异常!", e);
            }
        }
        if (null != stamper) {
            reader.close();
        }
    }
}
