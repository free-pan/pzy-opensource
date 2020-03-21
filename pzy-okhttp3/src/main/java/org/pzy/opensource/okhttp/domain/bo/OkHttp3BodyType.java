package org.pzy.opensource.okhttp.domain.bo;

import lombok.Data;
import org.pzy.opensource.domain.GlobalConstant;
import org.pzy.opensource.okhttp.support.enums.BodyTypeEnum;

import java.io.InputStream;

/**
 * @author 潘志勇
 * @date 2019-02-01
 */
@Data
public class OkHttp3BodyType<T> {

    private BodyTypeEnum type;

    private Class<T> dataTypeCls;

    /**
     * 字符编码. 默认:UTF-8
     */
    private String encoding;

    private OkHttp3BodyType() {
    }

    public static <T> OkHttp3BodyType json(Class<T> dataTypeCls, String encoding) {
        OkHttp3BodyType<T> bodyType = new OkHttp3BodyType();
        bodyType.setType(BodyTypeEnum.JSON);
        bodyType.setDataTypeCls(dataTypeCls);
        bodyType.setEncoding(encoding == null ? GlobalConstant.DEFAULT_CHARSET : encoding);
        return bodyType;
    }

    public static <T> OkHttp3BodyType xml(Class<T> dataTypeCls, String encoding) {
        OkHttp3BodyType<T> bodyType = new OkHttp3BodyType();
        bodyType.setType(BodyTypeEnum.XML);
        bodyType.setDataTypeCls(dataTypeCls);
        bodyType.setEncoding(encoding == null ? GlobalConstant.DEFAULT_CHARSET : encoding);
        return bodyType;
    }

    public static OkHttp3BodyType string(String encoding) {
        OkHttp3BodyType<String> bodyType = new OkHttp3BodyType();
        bodyType.setType(BodyTypeEnum.String);
        bodyType.setDataTypeCls(String.class);
        bodyType.setEncoding(encoding == null ? GlobalConstant.DEFAULT_CHARSET : encoding);
        return bodyType;
    }

    public static OkHttp3BodyType inputStream() {
        OkHttp3BodyType<InputStream> bodyType = new OkHttp3BodyType();
        bodyType.setType(BodyTypeEnum.InputStream);
        bodyType.setDataTypeCls(InputStream.class);
        return bodyType;
    }
}
