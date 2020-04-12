package org.pzy.opensource.verifycode.support.springboot.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 图片尺寸
 *
 * @author pan
 * @date 2019-06-21
 */
@ApiModel
@Data
public class PicSize implements Serializable {

    private static final long serialVersionUID = -885078642553169979L;

    /**
     * 图片宽度. 单位:像素
     */
    @ApiModelProperty("图片宽度")
    private int width;
    /**
     * 图片高度. 单位:像素
     */
    @ApiModelProperty("图片高度")
    private int height;

}
