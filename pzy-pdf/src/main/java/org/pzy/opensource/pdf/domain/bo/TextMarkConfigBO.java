package org.pzy.opensource.pdf.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 文字水印配置
 *
 * @author pan
 * @date 5/30/20
 */
@Data
public class TextMarkConfigBO implements Serializable {
    /**
     * 水印文字字体
     */
    FontInfoBO fontInfo;
    /**
     * 水印内容
     */
    String text;
    /**
     * 水印文字大小
     */
    Integer fontSize;
    /**
     * 水印文字透明度. 0-1之间的值, 0表示100%透明, 1表示不透明
     */
    Float waterAlpha;

    private TextMarkConfigBO() {
        this.fontSize = 25;
        this.waterAlpha = 0.2F;
    }

    public TextMarkConfigBO(FontInfoBO fontInfo, String text) {
        this();
        this.fontInfo = fontInfo;
        this.text = text;
    }
}
