package org.pzy.opensource.verifycode.support.springboot.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pan
 * @date 2019-06-21
 */
@Data
@ConfigurationProperties(prefix = "component.pic-verify-code", ignoreUnknownFields = true)
public class VerifyCodeConfigProperties {

    /**
     * 是否启用图片验证码. 默认false
     */
    private Boolean enable = false;

    /**
     * 需要进行图片验证码验证的url
     */
    private String[] filterUrls;

    /**
     * 验证码长度. 默认:5
     */
    private Integer length = 5;

    /**
     * client id签发者. 默认:pzy
     */
    private String issuer = "winter-pzy";

    /**
     * client id加密秘钥. 默认: 2011-06-22:pzy
     */
    private String secret = "2011-06-22:pzy";

    /**
     * client id过期时间. 单位:秒. 默认: 300秒
     */
    private Long expiresSeconds = 300L;

    /**
     * 验证码未通过验证时,跳转的url地址
     */
    private String errorRedirectUrl;

    /**
     * 图片宽度. 单位: px
     */
    private int width;
    /**
     * 图片高度. 单位: px
     */
    private int height;
    /**
     * 自定义的验证码宽高. key为前端传的值, value为自定义宽高
     */
    private Map<String, PicSize> customPicSize = new HashMap<>();

    public VerifyCodeConfigProperties() {
        this.width = 146;
        this.height = 33;
    }
}
