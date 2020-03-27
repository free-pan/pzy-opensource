package org.pzy.opensource.email.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 邮件服务器配置
 *
 * @author 潘志勇
 * @date 2019-02-03
 */
@Setter
@Getter
@ToString
public class EmailServerPropertiesBO implements Serializable {

    private static final long serialVersionUID = -4284562129807293806L;

    /**
     * 邮箱服务器地址. 如: smtp.163.com
     */
    private String mailServerHost;
    /**
     * 发件人邮箱地址. 如: test@163.com
     */
    private String mailSenderAddress;
    /**
     * 发件人邮箱账号. 如: test@163.com
     */
    private String mailSenderUsername;
    /**
     * 发件人邮箱的登录密码. 如: 123456
     */
    private String password;
}
