package org.pzy.opensource.okhttp.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 潘志勇
 * @date 2019-01-15
 */
@Setter
@Getter
@ToString
public class DownloadFileInfo implements Serializable {

    private static final long serialVersionUID = -4632949942739114255L;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件的字节数组
     */
    private byte[] content;
}
