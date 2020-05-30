package org.pzy.opensource.pdf.domain.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * 字体信息
 *
 * @author pan
 * @date 5/30/20
 */
@Data
public class FontInfoBO implements Serializable {
    /**
     * 字体文件路径
     */
    private String fontFilePath;
    /**
     * 覆盖默认的字体名称(css中设置时使用该字体名称)[可选]
     */
    private String fontName;
    /**
     * 是否嵌入. 默认:false
     */
    private Boolean embedded;

    private FontInfoBO() {
        this.embedded = false;
    }

    public FontInfoBO(String fontFilePath, String fontName) {
        this();
        this.fontFilePath = fontFilePath;
        this.fontName = fontName;
    }

    public FontInfoBO(String fontFilePath) {
        this();
        this.fontFilePath = fontFilePath;
    }
}
