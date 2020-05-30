package org.pzy.opensource.pdf.domain.bo;

import lombok.Data;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 图章水印配置
 *
 * @author pan
 * @date 5/30/20
 */
@Data
public class StampMarkConfigBO implements Serializable {
    /**
     * 图片的字节输入流
     */
    private InputStream imageInputStream;
    /**
     * 图片的插入位置: x轴坐标
     */
    private Integer x;
    /**
     * 图片的插入位置: y轴坐标
     */
    private Integer y;
    /**
     * 是否只在最后一页插入. 值为false则在每页都插入
     */
    private Boolean onlyLastPage;
    /**
     * 图片缩放比
     */
    private Float scalePercent;

    private StampMarkConfigBO() {
        this.onlyLastPage = true;
        this.scalePercent = 50F;
    }

    public StampMarkConfigBO(InputStream imageInputStream, Integer x, Integer y) {
        this();
        this.imageInputStream = imageInputStream;
        this.x = x;
        this.y = y;
    }
}
