package org.pzy.opensource.email.domain.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.mail.EmailAttachment;

import java.io.Serializable;
import java.util.List;

/**
 * 邮件消息
 *
 * @author 潘志勇
 * @date 2019-01-28
 */
@Setter
@Getter
@ToString
public class EmailMessageBO implements Serializable {

    private static final long serialVersionUID = -7003148455077610309L;

    /**
     * 邮件接收方
     */
    private List<String> toAddr;
    /**
     * 邮件内容
     */
    private String content;

    /**
     * 邮件标题
     */
    private String title;
    /**
     * 抄送人地址
     */
    private List<String> copyAddress;
    /**
     * 密送地址
     */
    private List<String> secretAddress;
    /**
     * 附件信息
     */
    private List<EmailAttachment> attachments = null;

}
