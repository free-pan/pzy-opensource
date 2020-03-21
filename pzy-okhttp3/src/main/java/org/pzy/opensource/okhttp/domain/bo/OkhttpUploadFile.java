package org.pzy.opensource.okhttp.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 潘志勇
 * @date 2019-01-18
 */
@Setter
@Getter
@ToString
public class OkhttpUploadFile implements Serializable {
    
    private static final long serialVersionUID = 7183096136564172053L;
    /**
     * 服务器接受参数名
     */
    private String name;
    /**
     * 文件名
     */
    private String title;
    /**
     * 文件内容byte数组
     */
    private byte[] fileBytes;
}
